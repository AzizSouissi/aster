package com.batchie.mapper;

import com.batchie.domain.Shipment;
import com.batchie.dto.ShipmentDto;
import org.springframework.stereotype.Component;

@Component
public class ShipmentMapper {

    public ShipmentDto toDto(Shipment shipment) {
        return ShipmentDto.builder()
                .id(shipment.getId())
                .trackingNumber(shipment.getTrackingNumber())
                .provider(shipment.getProvider())
                .serviceLevel(shipment.getServiceLevel())
                .senderAddress(shipment.getSenderAddress())
                .recipientAddress(shipment.getRecipientAddress())
                .packages(shipment.getPackages())
                .trackingEvents(shipment.getTrackingEvents())
                .status(shipment.getStatus())
                .totalWeight(shipment.getTotalWeight())
                .totalCost(shipment.getTotalCost())
                .createdAt(shipment.getCreatedAt())
                .updatedAt(shipment.getUpdatedAt())
                .build();
    }

    public Shipment toDomain(ShipmentDto shipmentDto) {
        return Shipment.builder()
                .id(shipmentDto.getId())
                .trackingNumber(shipmentDto.getTrackingNumber())
                .provider(shipmentDto.getProvider())
                .serviceLevel(shipmentDto.getServiceLevel())
                .senderAddress(shipmentDto.getSenderAddress())
                .recipientAddress(shipmentDto.getRecipientAddress())
                .packages(shipmentDto.getPackages())
                .trackingEvents(shipmentDto.getTrackingEvents())
                .status(shipmentDto.getStatus())
                .totalWeight(shipmentDto.getTotalWeight())
                .totalCost(shipmentDto.getTotalCost())
                .createdAt(shipmentDto.getCreatedAt())
                .updatedAt(shipmentDto.getUpdatedAt())
                .build();
    }
}