package com.batchie.service.handler;

import com.batchie.config.TrackingMoreConfig;
import com.batchie.dto.TrackingEventDto;
import com.batchie.mapper.TrackingEventMapper;
import com.batchie.service.ShipmentService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Slf4j
@Component("trackingMoreWebhookHandler")
public class TrackingMoreWebhookHandler implements WebhookHandler {
    private final ObjectMapper objectMapper;
    private final ShipmentService shipmentService;
    private final TrackingEventMapper trackingEventMapper;
    private final TrackingMoreConfig config;

    @Autowired
    public TrackingMoreWebhookHandler(
            ObjectMapper objectMapper,
            ShipmentService shipmentService,
            TrackingEventMapper trackingEventMapper,
            TrackingMoreConfig config) {
        this.objectMapper = objectMapper;
        this.shipmentService = shipmentService;
        this.trackingEventMapper = trackingEventMapper;
        this.config = config;
    }

    @Override
    public void handleWebhook(String rawRequest, String contentType) {
        try {
            log.info("Processing TrackingMore webhook payload");
            TrackingEventDto trackingEventDto = parseTrackingMorePayload(rawRequest);
            if (trackingEventDto != null) {
                log.info("Saving tracking event for shipment: {}", trackingEventDto.getShipmentId());
                shipmentService.processShipmentEvent(trackingEventMapper.toDomain(trackingEventDto));
                log.info("Successfully processed TrackingMore webhook");
            } else {
                log.warn("Unable to parse TrackingMore webhook payload");
            }
        } catch (Exception e) {
            log.error("Error processing TrackingMore webhook", e);
            throw new RuntimeException("Failed to process TrackingMore webhook", e);
        }
    }

    private TrackingEventDto parseTrackingMorePayload(String rawRequest) throws IOException {
        JsonNode rootNode = objectMapper.readTree(rawRequest);
        JsonNode dataNode = rootNode.path("data");

        if (dataNode.isMissingNode()) {
            log.warn("Missing 'data' field in TrackingMore payload");
            return null;
        }

        String event = dataNode.path("status").asText("");
        String location = "";

        // Extract location from tracking_location if available
        JsonNode locationNode = dataNode.path("tracking_location");
        if (!locationNode.isMissingNode()) {
            String city = locationNode.path("city").asText("");
            String country = locationNode.path("country").asText("");
            location = (!city.isEmpty() && !country.isEmpty()) ? city + ", " + country :
                    (!city.isEmpty() ? city : country);
        }

        String details = dataNode.path("status_description").asText("");
        String shipmentId = dataNode.path("tracking_number").asText("");

        String timestampStr = dataNode.path("updated_at").asText("");
        LocalDateTime timestamp = null;
        if (!timestampStr.isEmpty()) {
            try {
                // TrackingMore uses ISO 8601 format
                DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                timestamp = LocalDateTime.parse(timestampStr, formatter);
            } catch (Exception e) {
                log.warn("Unable to parse timestamp: {}", timestampStr, e);
            }
        }

        if (shipmentId.isEmpty() || event.isEmpty()) {
            log.warn("Required fields missing in TrackingMore payload");
            return null;
        }

        return TrackingEventDto.builder()
                .event(event)
                .location(location)
                .details(details)
                .shipmentId(shipmentId)
                .timestamp(timestamp)
                .build();
    }

    public boolean validateWebhook(String signature, String payload) {
        if (signature == null || config.getWebhookSecret() == null) {
            return false;
        }

        String computedSignature = calculateHmacSha256(payload, config.getWebhookSecret());
        return signature.equals(computedSignature);
    }

    private String calculateHmacSha256(String data, String key) {
        try {
            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA256");
            sha256Hmac.init(secretKey);
            return Base64.getEncoder().encodeToString(sha256Hmac.doFinal(data.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate HMAC", e);
        }
    }
}