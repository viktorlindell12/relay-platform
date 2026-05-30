package com.relay.message.service;

import com.relay.message.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

/**
 * Periodically removes unpinned messages whose 24-hour expiry deadline has passed.
 */
@Component
public class MessageCleanupScheduler {

    private static final Logger log = LoggerFactory.getLogger(MessageCleanupScheduler.class);

    private final MessageRepository messageRepository;

    public MessageCleanupScheduler(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    /** Runs every hour and deletes unpinned messages older than 24 hours. */
    @Scheduled(fixedRate = 3_600_000)
    @Transactional
    public void deleteExpiredMessages() {
        int count = messageRepository.deleteExpiredUnpinned(Instant.now());
        if (count > 0) {
            log.info("Deleted {} expired unpinned message(s)", count);
        }
    }
}