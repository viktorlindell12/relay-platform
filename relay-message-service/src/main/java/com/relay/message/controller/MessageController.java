package com.relay.message.controller;

import com.relay.message.dto.CreateMessageRequest;
import com.relay.message.dto.MessageResponse;
import com.relay.message.service.MessageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
     * Sends a message to a channel and publishes a {@code message.published} event after commit.
     *
     * @param request sender, channel name, and content
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
     * Returns a page of messages in the given channel ordered by creation time ascending.
     * Default page size is 20; override with {@code ?page=0&size=50&sort=createdAt,asc}.
     *
     * @param channel  channel name to filter by (e.g. "general")
     * @param pageable pagination and sort parameters resolved from query string
     * @return 200 OK with a page of messages
     */
    @GetMapping
    public ResponseEntity<Page<MessageResponse>> getByChannel(
            @RequestParam @NotBlank String channel,
            @PageableDefault(size = 20, sort = {"createdAt", "id"}) Pageable pageable) {
        return ResponseEntity.ok(messageService.getByChannel(channel, pageable));
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