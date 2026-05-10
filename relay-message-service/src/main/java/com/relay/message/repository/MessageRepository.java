package com.relay.message.repository;

import com.relay.message.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Persistence operations for {@link Message} entities.
 */
public interface MessageRepository extends JpaRepository<Message, Long> {

    /**
     * Returns a page of messages in the given channel ordered by the {@link Pageable} sort.
     *
     * @param channel  the channel name to filter by (e.g. "general")
     * @param pageable pagination and sort parameters
     * @return one page of matching messages
     */
    Page<Message> findByChannel(String channel, Pageable pageable);
}