package com.chatop.controller;

import com.chatop.dto.MessageRequestDTO;
import com.chatop.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/messages")
@Tag(name = "Messages", description = "Envoi de messages liés aux annonces")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping
    @Operation(summary = "Envoyer un message", description = "Envoie un message à propos d’une annonce (location).")
    public ResponseEntity<Map<String, String>> sendMessage(
            @RequestBody MessageRequestDTO dto,
            Authentication authentication
    ) {
        String email = (String) authentication.getPrincipal();
        messageService.sendMessage(email, dto);
        return ResponseEntity.ok(Map.of("message", "Message sent with success !"));
    }
}
