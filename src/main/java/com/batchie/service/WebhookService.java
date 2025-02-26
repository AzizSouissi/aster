package com.batchie.service;

import com.batchie.dto.ShipmentEventDto;
import com.batchie.util.WebhookUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebhookService {

    private final ShipmentService shipmentService;
    private final WebhookUtil webhookUtil;

    @Autowired
    public WebhookService(ShipmentService shipmentService, WebhookUtil webhookUtil) {
        this.shipmentService = shipmentService;
        this.webhookUtil = webhookUtil;
    }

    /**
     * Processes a webhook request, determines whether it's REST or SOAP,
     * and delegates handling accordingly.
     */
    public void processWebhook(String rawRequest, String contentType) {
        ShipmentEventDto shipmentEventDto = null;

        if (webhookUtil.isSoapRequest(contentType)) {
            shipmentEventDto = webhookUtil.parseSoapPayload(rawRequest);
        } else if (webhookUtil.isRestRequest(contentType)) {
            shipmentEventDto = webhookUtil.parseJsonPayload(rawRequest);
        } else {
            return;
        }

        if (shipmentEventDto != null) {
            shipmentService.processShipmentEvent(shipmentEventDto);
        }
    }
}
