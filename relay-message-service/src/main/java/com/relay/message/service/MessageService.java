package com.relay.message.service;

import com.relay.message.dto.CreateMessageRequest;
import com.relay.message.dto.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Business logic contract for message operations.
 */
public interface MessageService {

    /**
     * Persists a new message and publishes a {@code message.published} event after commit.
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
     * Returns a page of messages in the given channel ordered by creation time ascending.
     *
     * @param channel  the channel name to filter by (e.g. "general")
     * @param pageable pagination and sort parameters
     * @return one page of matching messages
     */
    Page<MessageResponse> getByChannel(String channel, Pageable pageable);

    /**
     * Deletes a message by its internal ID.
     *
     * @param id the message ID
     * @throws com.relay.message.exception.MessageNotFoundException if no message exists with that ID
     */
    void delete(Long id);

    /**
     * Toggles the pinned state of a message.
     * Pinning clears the expiry deadline; unpinning resets it to 24 hours from now.
     *
     * @param messageId   the message to toggle
     * @param requesterId authUserId of the caller — must match the message sender
     * @return the updated message
     * @throws com.relay.message.exception.MessageNotFoundException     if no message exists with that ID
     * @throws com.relay.message.exception.MessageAccessDeniedException if the caller is not the sender
     */
    MessageResponse togglePin(Long messageId, Long requesterId);
}