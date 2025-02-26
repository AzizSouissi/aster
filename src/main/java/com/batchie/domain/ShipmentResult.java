package com.batchie.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShipmentResult {
    private Shipment shipment;
    private TrackingEvent trackingEvent;
    private boolean success;
    private String errorMessage;
    private String trackingUrl;
}