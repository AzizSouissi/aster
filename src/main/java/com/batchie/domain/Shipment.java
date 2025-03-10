package com.batchie.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "shipments")
public class Shipment {
    @Id
    private String id;
    private String trackingNumber;
    private String provider;  // The shipping carrier (UPS, FedEx, etc.)
    private String serviceLevel;  // Express, Standard, etc.
    private ShipmentStatus status;  // Overall status of the shipment

    private ShipmentAddress senderAddress;
    private ShipmentAddress recipientAddress;

    @Builder.Default
    private List<PackageDetails> packages = new ArrayList<>();

    @Builder.Default
    private List<TrackingEvent> trackingEvents = new ArrayList<>();

    private double totalWeight;
    private double totalCost;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime estimatedDeliveryDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
}
