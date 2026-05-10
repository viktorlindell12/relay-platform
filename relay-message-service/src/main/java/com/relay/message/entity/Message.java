package com.relay.message.entity;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * Stores a single message sent in a channel.
 * {@code senderId} and {@code channelId} are foreign keys by convention only —
 * no DB-level FK constraints because the referenced entities live in separate services.
 */
@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    @Column(name = "channel_id", nullable = false)
    private Long channelId;

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

    /** @return ID of the channel this message belongs to */
    public Long getChannelId() { return channelId; }

    /** @param channelId ID of the channel this message belongs to */
    public void setChannelId(Long channelId) { this.channelId = channelId; }

    /** @return message text */
    public String getContent() { return content; }

    /** @param content message text */
    public void setContent(String content) { this.content = content; }

    /** @return timestamp when this message was persisted */
    public Instant getCreatedAt() { return createdAt; }
}