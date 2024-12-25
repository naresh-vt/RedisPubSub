package com.prototype.pubsub.publisher;

import com.prototype.pubsub.config.RedisConfig;
import com.prototype.pubsub.model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class MessagePublisher {
    private static final Logger logger = LoggerFactory.getLogger(MessagePublisher.class);
    private final JedisPool jedisPool;
    private final ObjectMapper objectMapper;
    private static final String CHANNEL = "notifications";

    public MessagePublisher() {
        this.jedisPool = RedisConfig.getJedisPool();
        this.objectMapper = new ObjectMapper();
        // Register the JavaTimeModule with our ObjectMapper, it adds these serializers, allowing Jackson to
        // properly convert the Instant timestamp to JSON and back
        this.objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
    }

    public void publish(Message message) {
        try (Jedis jedis = jedisPool.getResource()) {
            String jsonMessage = objectMapper.writeValueAsString(message);
            jedis.publish(CHANNEL, jsonMessage);
            logger.info("Published message: {}", message);
        } catch (Exception e) {
            logger.error("Error publishing message: {}", e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        MessagePublisher publisher = new MessagePublisher();

        // Publish a message every 2 seconds
        while (true) {
            try {
                Message message = new Message("Message at " + java.time.LocalDateTime.now());
                publisher.publish(message);
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                logger.error("Publisher interrupted: {}", e.getMessage(), e);
                break;
            }
        }

        RedisConfig.closeJedisPool();
    }
}