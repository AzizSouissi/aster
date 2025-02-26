package com.batchie.controller;

import com.batchie.service.WebhookService;
import com.batchie.service.handler.TrackingMoreWebhookHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class WebhookController {

    private final WebhookService webhookService;
    private final TrackingMoreWebhookHandler trackingMoreWebhookHandler;

    @PostMapping("/trackingmore")
    public ResponseEntity<String> handleTrackingMoreWebhook(
            @RequestHeader(value = "Content-Type", defaultValue = "application/json") String contentType,
            @RequestHeader(value = "X-TrackingMore-Signature", required = false) String signature,
            @RequestBody String rawRequest,
            HttpServletRequest request) {

        log.info("Received TrackingMore webhook");

        try {
            if (signature != null && !trackingMoreWebhookHandler.validateWebhook(signature, rawRequest)) {
                log.warn("Invalid TrackingMore webhook signature");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid signature");
            }

            webhookService.processWebhook(rawRequest, contentType + ";source=trackingmore");
            return ResponseEntity.ok("Webhook processed successfully");
        } catch (UnsupportedOperationException e) {
            log.error("Unsupported media type: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(e.getMessage());
        } catch (Exception e) {
            log.error("Error processing webhook: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to process webhook: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<String> handleWebhook(
            @RequestHeader("Content-Type") String contentType,
            @Valid @RequestBody String rawRequest) {

        log.info("Received generic webhook with content type: {}", contentType);

        try {
            webhookService.processWebhook(rawRequest, contentType);
            return ResponseEntity.ok("Webhook received successfully");
        } catch (UnsupportedOperationException e) {
            log.error("Unsupported media type: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(e.getMessage());
        } catch (Exception e) {
            log.error("Error processing webhook: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to process request: " + e.getMessage());
        }
    }
}