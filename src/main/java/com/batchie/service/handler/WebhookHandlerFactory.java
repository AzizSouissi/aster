package com.batchie.service.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class WebhookHandlerFactory {

    private final Map<String, WebhookHandler> handlers;

    @Autowired
    public WebhookHandlerFactory(Map<String, WebhookHandler> handlers) {
        this.handlers = handlers;
    }

    public WebhookHandler getHandler(String contentType) {
        if (contentType.contains("trackingmore")) {
            return handlers.get("trackingMoreWebhookHandler");
        } else {
            throw new UnsupportedOperationException("Unsupported content type: " + contentType);
        }
    }
}
