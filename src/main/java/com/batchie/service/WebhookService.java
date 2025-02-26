package com.batchie.service;

import com.batchie.service.handler.WebhookHandler;
import com.batchie.service.handler.WebhookHandlerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebhookService {
    private final WebhookHandlerFactory webhookHandlerFactory;

    @Autowired
    public WebhookService(WebhookHandlerFactory webhookHandlerFactory) {
        this.webhookHandlerFactory = webhookHandlerFactory;
    }

    public void processWebhook(String rawRequest, String contentType) {
        try {
            WebhookHandler handler = webhookHandlerFactory.getHandler(contentType);
            handler.handleWebhook(rawRequest, contentType);
        } catch (UnsupportedOperationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to process webhook", e);
        }
    }
}