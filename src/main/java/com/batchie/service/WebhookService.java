package com.batchie.service;

import com.batchie.dto.TrackingEventDto;
import com.batchie.mapper.TrackingEventMapper;
import com.batchie.util.WebhookUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebhookService {

    private final ShipmentService shipmentService;
    private final WebhookUtil webhookUtil;
    private final TrackingEventMapper trackingEventMapper;

    @Autowired
    public WebhookService(ShipmentService shipmentService, WebhookUtil webhookUtil, TrackingEventMapper trackingEventMapper) {
        this.shipmentService = shipmentService;
        this.webhookUtil = webhookUtil;
        this.trackingEventMapper = trackingEventMapper;
    }

    public void processWebhook(String rawRequest, String contentType) {
        TrackingEventDto trackingEventDto = null;

        if (webhookUtil.isSoapRequest(contentType)) {
            trackingEventDto = webhookUtil.parseSoapPayload(rawRequest);
        } else if (webhookUtil.isRestRequest(contentType)) {
            trackingEventDto = webhookUtil.parseJsonPayload(rawRequest);
        } else {
            return;
        }

        if (trackingEventDto != null) {
            shipmentService.processShipmentEvent(trackingEventMapper.toDomain(trackingEventDto));
        }
    }
}
