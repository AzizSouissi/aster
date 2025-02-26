package com.batchie.service.handler;

import com.batchie.dto.ShipmentEventDto;

public interface WebhookHandler {
    void handleWebhook(String rawRequest, String contentType);
}
