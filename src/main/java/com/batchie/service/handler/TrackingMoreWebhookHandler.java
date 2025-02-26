package com.batchie.service.handler;

import com.batchie.dto.TrackingEventDto;
import com.batchie.mapper.TrackingEventMapper;
import com.batchie.service.ShipmentService;
import com.batchie.util.WebhookUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrackingMoreWebhookHandler implements WebhookHandler {

    private final WebhookUtil webhookUtil;
    private final ShipmentService shipmentService;
    private final TrackingEventMapper trackingEventMapper;

    @Autowired
    public TrackingMoreWebhookHandler(WebhookUtil webhookUtil, ShipmentService shipmentService, TrackingEventMapper trackingEventMapper) {
        this.webhookUtil = webhookUtil;
        this.shipmentService = shipmentService;
        this.trackingEventMapper = trackingEventMapper;
    }

    @Override
    public void handleWebhook(String rawRequest, String contentType) {
        TrackingEventDto trackingEventDto = webhookUtil.parseJsonPayload(rawRequest);
        if (trackingEventDto != null) {
            shipmentService.processShipmentEvent(trackingEventMapper.toDomain(trackingEventDto));
        }
    }
}
