package com.batchie.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "shipments")
public class Shipment {

    @Id
    private String id;
    private String trackingNumber;
    private String provider;
    private String serviceLevel;
    private ShipmentAddress senderAddress;
    private ShipmentAddress recipientAddress;
    private List<PackageDetails> packages;
    private List<TrackingEvent> trackingEvents;
    private String status;
    private double totalWeight;
    private double totalCost;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime updatedAt;
}
