package com.relay.message.repository;

import com.relay.message.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.util.Optional;

import java.time.Instant;

/**
 * Persistence operations for {@link Message} entities.
 */
public interface MessageRepository extends JpaRepository<Message, Long> {

    /**
     * Returns a page of non-expired messages in the given channel.
     * Pinned messages are always included; unpinned messages are included only if their
     * expiry deadline has not yet passed.
     *
     * @param channel  the channel name to filter by (e.g. "general")
     * @param now      the current instant — unpinned messages with {@code expiresAt <= now} are excluded
     * @param pageable pagination and sort parameters
     * @return one page of active messages
     */
    @Query("SELECT m FROM Message m WHERE m.channel = :channel AND (m.pinned = true OR m.expiresAt > :now)")
    Page<Message> findActiveByChannel(@Param("channel") String channel, @Param("now") Instant now, Pageable pageable);

    /**
     * Acquires a row-level write lock on the message for the duration of the transaction.
     * Used by the pin-toggle path to prevent lost updates under concurrent requests.
     *
     * @param id the message ID
     * @return the locked message, or empty if not found
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT m FROM Message m WHERE m.id = :id")
    Optional<Message> findByIdForUpdate(@Param("id") Long id);

    /**
     * Bulk-deletes unpinned messages whose expiry deadline has passed.
     *
     * @param now messages with {@code expiresAt <= now} are deleted
     * @return the number of deleted rows
     */
    @Modifying
    @Query("DELETE FROM Message m WHERE m.pinned = false AND m.expiresAt IS NOT NULL AND m.expiresAt <= :now")
    int deleteExpiredUnpinned(@Param("now") Instant now);
}