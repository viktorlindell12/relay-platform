package com.relay.bff.controller;

import com.relay.bff.client.MessageServiceClient;
import com.relay.bff.dto.message.MessageResponse;
import com.relay.bff.dto.message.SendMessageRequest;
import com.relay.bff.filter.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageServiceClient messageServiceClient;

    public MessageController(MessageServiceClient messageServiceClient) {
        this.messageServiceClient = messageServiceClient;
    }

    @PostMapping
    public ResponseEntity<MessageResponse> send(@Valid @RequestBody SendMessageRequest request,
                                                HttpServletRequest httpRequest) {
        Long senderId = (Long) httpRequest.getAttribute(JwtAuthenticationFilter.USER_ID_ATTRIBUTE);
        if (senderId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(messageServiceClient.send(senderId, request));
    }
}