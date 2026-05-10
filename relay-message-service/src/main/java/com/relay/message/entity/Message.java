package com.relay.message.entity;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * Stores a single message sent in a channel.
 * {@code senderId} is a foreign key by convention only — no DB-level FK constraint
 * because the referenced user entity lives in a separate service.
 */
@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    @Column(nullable = false, length = 100)
    private String channel;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    private void prePersist() {
        createdAt = Instant.now();
    }

    /** @return internal message ID */
    public Long getId() { return id; }

    /** @return authUserId of the message sender */
    public Long getSenderId() { return senderId; }

    /** @param senderId authUserId of the message sender */
    public void setSenderId(Long senderId) { this.senderId = senderId; }

    /** @return name of the channel this message belongs to (e.g. "general") */
    public String getChannel() { return channel; }

    /** @param channel name of the channel this message belongs to */
    public void setChannel(String channel) { this.channel = channel; }

    /** @return message text */
    public String getContent() { return content; }

    /** @param content message text */
    public void setContent(String content) { this.content = content; }

    /** @return UTC timestamp when this message was persisted */
    public Instant getCreatedAt() { return createdAt; }
}