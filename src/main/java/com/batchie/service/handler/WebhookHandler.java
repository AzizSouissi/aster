package com.batchie.service.handler;

public interface WebhookHandler {
    void handleWebhook(String rawRequest, String contentType);
}
