package com.relay.message.exception;

/**
 * Thrown when a message cannot be found by the given ID.
 */
public class MessageNotFoundException extends RuntimeException {

    /**
     * @param id the message ID that was not found
     */
    public MessageNotFoundException(Long id) {
        super("Message not found");
    }
}