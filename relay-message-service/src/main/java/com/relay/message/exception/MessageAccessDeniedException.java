package com.relay.message.exception;

/** Thrown when a user attempts to modify a message they do not own. */
public class MessageAccessDeniedException extends RuntimeException {

    public MessageAccessDeniedException() {
        super("You can only pin your own messages");
    }
}