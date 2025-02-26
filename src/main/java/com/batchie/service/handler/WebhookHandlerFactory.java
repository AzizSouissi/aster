package com.batchie.service.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class WebhookHandlerFactory {

    private final Map<String, WebhookHandler> handlers;

    @Autowired
    public WebhookHandlerFactory(Map<String, WebhookHandler> handlers) {
        this.handlers = handlers;
    }

    public WebhookHandler getHandler(String contentType) {
        log.debug("Selecting handler for content type: {}", contentType);

        // Check for TrackingMore specific markers
        if ((contentType.contains("application/json") || contentType.contains("text/plain")) &&
                (contentType.contains("trackingmore") || contentType.contains("tracking-more"))) {

            WebhookHandler handler = handlers.get("trackingMoreWebhookHandler");
            if (handler != null) {
                log.debug("Selected trackingMoreWebhookHandler");
                return handler;
            } else {
                log.warn("trackingMoreWebhookHandler not found in registered handlers");
            }
        }

        // Fallback to general handlers based on content type
        if (contentType.contains("application/json")) {
            WebhookHandler handler = handlers.get("defaultJsonWebhookHandler");
            if (handler != null) {
                log.debug("Selected defaultJsonWebhookHandler");
                return handler;
            }
        } else if (contentType.contains("application/xml") || contentType.contains("text/xml")) {
            WebhookHandler handler = handlers.get("defaultXmlWebhookHandler");
            if (handler != null) {
                log.debug("Selected defaultXmlWebhookHandler");
                return handler;
            }
        }

        log.error("No suitable handler found for content type: {}", contentType);
        throw new UnsupportedOperationException("Unsupported content type: " + contentType);
    }
}