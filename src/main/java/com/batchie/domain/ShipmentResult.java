package com.batchie.domain;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShipmentResult {
    private Shipment shipment;
    private boolean success;
    private String trackingUrl;
    private String errorMessage;
    private Track track;
}
