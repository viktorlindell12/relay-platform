package com.relay.message.repository;

import com.relay.message.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Persistence operations for {@link Message} entities.
 */
public interface MessageRepository extends JpaRepository<Message, Long> {

    /**
     * Returns all messages in the given channel ordered by creation time ascending.
     *
     * @param channelId the channel to fetch messages for
     * @return ordered list of messages, empty if none exist
     */
    List<Message> findByChannelIdOrderByCreatedAtAsc(Long channelId);
}