package com.relay.message.service;

import com.relay.message.dto.CreateMessageRequest;
import com.relay.message.dto.MessageResponse;
import com.relay.message.entity.Message;
import com.relay.message.event.MessageEventPublisher;
import com.relay.message.exception.MessageNotFoundException;
import com.relay.message.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Default implementation of {@link MessageService}.
 */
@Service
public class MessageServiceImpl implements MessageService {

    private static final Logger log = LoggerFactory.getLogger(MessageServiceImpl.class);

    private final MessageRepository messageRepository;
    private final MessageEventPublisher eventPublisher;

    public MessageServiceImpl(MessageRepository messageRepository, MessageEventPublisher eventPublisher) {
        this.messageRepository = messageRepository;
        this.eventPublisher = eventPublisher;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public MessageResponse send(CreateMessageRequest request) {
        Message message = new Message();
        message.setSenderId(request.senderId());
        message.setChannel(request.channel());
        message.setContent(request.content());

        Message saved = messageRepository.save(message);
        log.debug("Persisted message id={} in channel={}", saved.getId(), saved.getChannel());

        // Publish only after the DB transaction commits — prevents consumers from receiving
        // an event for a message that was never durably stored due to a rollback.
        // Errors are caught so a broker failure does not fail an already-committed request.
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                try {
                    eventPublisher.publishMessageSent(saved);
                } catch (Exception e) {
                    log.error("Failed to publish message.published event for message id={}", saved.getId(), e);
                }
            }
        });

        return toResponse(saved);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public MessageResponse getById(Long id) {
        return messageRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new MessageNotFoundException(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MessageResponse> getByChannel(String channel, Pageable pageable) {
        return messageRepository.findByChannel(channel, pageable)
                .map(this::toResponse);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void delete(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new MessageNotFoundException(id));
        messageRepository.delete(message);
        log.debug("Deleted message id={}", id);
    }

    private MessageResponse toResponse(Message message) {
        return new MessageResponse(
                message.getId(),
                message.getSenderId(),
                message.getChannel(),
                message.getContent(),
                message.getCreatedAt()
        );
    }
}