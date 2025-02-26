package com.batchie.service.handler;

import com.batchie.dto.TrackingEventDto;
import com.batchie.mapper.TrackingEventMapper;
import com.batchie.service.ShipmentService;
import com.batchie.util.WebhookUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component("defaultXmlWebhookHandler")
public class DefaultXmlWebhookHandler implements WebhookHandler {

    private final WebhookUtil webhookUtil;
    private final ShipmentService shipmentService;
    private final TrackingEventMapper trackingEventMapper;

    @Autowired
    public DefaultXmlWebhookHandler(WebhookUtil webhookUtil, ShipmentService shipmentService, TrackingEventMapper trackingEventMapper) {
        this.webhookUtil = webhookUtil;
        this.shipmentService = shipmentService;
        this.trackingEventMapper = trackingEventMapper;
    }

    @Override
    public void handleWebhook(String rawRequest, String contentType) {
        log.debug("Handling XML webhook");
        TrackingEventDto trackingEventDto = webhookUtil.parseSoapPayload(rawRequest);
        if (trackingEventDto != null) {
            log.info("Processing shipment event from XML webhook");
            shipmentService.processShipmentEvent(trackingEventMapper.toDomain(trackingEventDto));
        } else {
            log.warn("Failed to parse XML webhook payload");
        }
    }
}