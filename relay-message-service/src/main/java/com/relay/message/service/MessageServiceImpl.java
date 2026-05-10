package com.relay.message.service;

import com.relay.message.dto.CreateMessageRequest;
import com.relay.message.dto.MessageResponse;
import com.relay.message.entity.Message;
import com.relay.message.event.MessageEventPublisher;
import com.relay.message.exception.MessageNotFoundException;
import com.relay.message.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        message.setChannelId(request.channelId());
        message.setContent(request.content());

        Message saved = messageRepository.save(message);
        log.debug("Persisted message id={} in channel={}", saved.getId(), saved.getChannelId());

        eventPublisher.publishMessageSent(saved);
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
    public List<MessageResponse> getByChannel(Long channelId) {
        return messageRepository.findByChannelIdOrderByCreatedAtAsc(channelId)
                .stream()
                .map(this::toResponse)
                .toList();
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
                message.getChannelId(),
                message.getContent(),
                message.getCreatedAt()
        );
    }
}