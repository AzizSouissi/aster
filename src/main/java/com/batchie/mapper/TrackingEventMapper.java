package com.batchie.mapper;

import com.batchie.domain.TrackingEvent;
import com.batchie.dto.TrackingEventDto;
import org.springframework.stereotype.Component;

@Component
public class TrackingEventMapper {

    public TrackingEvent toDomain(TrackingEventDto dto) {
        return TrackingEvent.builder()
                .shipmentId(dto.getShipmentId())
                .eventType(dto.getEventType())
                .location(dto.getLocation())
                .details(dto.getDetails())
                .timestamp(dto.getTimestamp())
                .build();
    }

    public TrackingEventDto toDto(TrackingEvent domain) {
        return TrackingEventDto.builder()
                .shipmentId(domain.getShipmentId())
                .eventType(domain.getEventType())
                .location(domain.getLocation())
                .details(domain.getDetails())
                .timestamp(domain.getTimestamp())
                .build();
    }
}
