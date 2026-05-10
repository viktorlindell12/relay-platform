package com.relay.message.controller;

import com.relay.message.dto.CreateMessageRequest;
import com.relay.message.dto.MessageResponse;
import com.relay.message.service.MessageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Internal REST endpoints for message operations.
 * All routes require the {@code X-Internal-Key} header — they are not exposed publicly.
 */
@Validated
@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * Sends a message to a channel and publishes a {@code message.published} event.
     *
     * @param request sender, channel, and content
     * @return 201 Created with the persisted message
     */
    @PostMapping
    public ResponseEntity<MessageResponse> send(@Valid @RequestBody CreateMessageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(messageService.send(request));
    }

    /**
     * Returns a single message by its internal ID.
     *
     * @param id the message ID
     * @return 200 OK with the message, or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<MessageResponse> getById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(messageService.getById(id));
    }

    /**
     * Returns all messages in the given channel ordered by creation time ascending.
     *
     * @param channelId the channel to fetch messages for
     * @return 200 OK with the ordered list
     */
    @GetMapping
    public ResponseEntity<List<MessageResponse>> getByChannel(@RequestParam @Positive Long channelId) {
        return ResponseEntity.ok(messageService.getByChannel(channelId));
    }

    /**
     * Deletes a message by its internal ID.
     *
     * @param id the message ID
     * @return 204 No Content, or 404 if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Positive Long id) {
        messageService.delete(id);
        return ResponseEntity.noContent().build();
    }
}