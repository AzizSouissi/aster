package com.batchie.controller;

import com.batchie.service.handler.WebhookHandler;
import com.batchie.service.handler.WebhookHandlerFactory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class WebhookController {

    private final WebhookHandlerFactory webhookHandlerFactory;

    @PostMapping
    public ResponseEntity<String> handleWebhook(@RequestHeader("Content-Type") String contentType, @Valid @RequestBody String rawRequest) {
        try {
            WebhookHandler handler = webhookHandlerFactory.getHandler(contentType);
            handler.handleWebhook(rawRequest, contentType);
            return ResponseEntity.ok("Webhook received successfully");
        } catch (UnsupportedOperationException e) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to process request");
        }
    }
}
