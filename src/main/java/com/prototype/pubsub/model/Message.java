package com.prototype.pubsub.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

public class Message {
    @JsonProperty("id")
    private String id;

    @JsonProperty("content")
    private String content;

    @JsonProperty("timestamp")
    private Instant timestamp;

    public Message() {
        this.id = java.util.UUID.randomUUID().toString();
        this.timestamp = Instant.now();
    }

    public Message(String content) {
        this();
        this.content = content;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return String.format("Message{id='%s', content='%s', timestamp=%s}",
                id, content, timestamp);
    }
}
