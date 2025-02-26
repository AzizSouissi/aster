package com.batchie.dto;

import com.batchie.domain.PackageDetails;
import com.batchie.domain.ShipmentAddress;
import com.batchie.domain.ShipmentStatus;
import com.batchie.domain.TrackingEvent;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentDto {
    @Id
    private String id;
    private String trackingNumber;
    private String provider;
    private String serviceLevel;
    private ShipmentStatus status;

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