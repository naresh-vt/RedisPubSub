package com.prototype.pubsub.subscriber;

import com.prototype.pubsub.config.RedisConfig;
import com.prototype.pubsub.model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

public class MessageSubscriber {
    private static final Logger logger = LoggerFactory.getLogger(MessageSubscriber.class);
    private final JedisPool jedisPool;
    private final ObjectMapper objectMapper;
    private static final String CHANNEL = "notifications";

    public MessageSubscriber() {
        this.jedisPool = RedisConfig.getJedisPool();
        this.objectMapper = new ObjectMapper();
        // Register the JavaTimeModule with our ObjectMapper, it adds these serializers, allowing Jackson to
        // properly convert the Instant timestamp to JSON and back
        this.objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
    }

    public void subscribe() {
        try (Jedis jedis = jedisPool.getResource()) {
            logger.info("Subscribing to channel: {}", CHANNEL);

            JedisPubSub jedisPubSub = new JedisPubSub() {
                @Override
                public void onMessage(String channel, String message) {
                    try {
                        Message msg = objectMapper.readValue(message, Message.class);
                        processMessage(msg);
                    } catch (Exception e) {
                        logger.error("Error processing message: {}", e.getMessage(), e);
                    }
                }

                @Override
                public void onSubscribe(String channel, int subscribedChannels) {
                    logger.info("Subscribed to channel: {} (Total channels: {})",
                            channel, subscribedChannels);
                }
            };

            jedis.subscribe(jedisPubSub, CHANNEL);
        } catch (Exception e) {
            logger.error("Error in subscriber: {}", e.getMessage(), e);
        }
    }

    private void processMessage(Message message) {
        logger.info("Processing message: {}", message);
        // Add your business logic here
    }

    public static void main(String[] args) {
        MessageSubscriber subscriber = new MessageSubscriber();
        subscriber.subscribe();

        // The subscribe() method blocks, but if we get here, close the pool
        RedisConfig.closeJedisPool();
    }
}
