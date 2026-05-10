package com.relay.message.service;

import com.relay.message.dto.CreateMessageRequest;
import com.relay.message.dto.MessageResponse;

import java.util.List;

/**
 * Business logic contract for message operations.
 */
public interface MessageService {

    /**
     * Persists a new message and publishes a {@code message.published} event.
     *
     * @param request sender, channel, and content
     * @return the persisted message
     */
    MessageResponse send(CreateMessageRequest request);

    /**
     * Returns a single message by its internal ID.
     *
     * @param id the message ID
     * @return the matching message
     * @throws com.relay.message.exception.MessageNotFoundException if no message exists with that ID
     */
    MessageResponse getById(Long id);

    /**
     * Returns all messages in the given channel ordered by creation time ascending.
     *
     * @param channelId the channel to fetch messages for
     * @return ordered list of messages, empty if the channel has no messages
     */
    List<MessageResponse> getByChannel(Long channelId);

    /**
     * Deletes a message by its internal ID.
     *
     * @param id the message ID
     * @throws com.relay.message.exception.MessageNotFoundException if no message exists with that ID
     */
    void delete(Long id);
}