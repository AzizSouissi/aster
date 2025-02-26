package com.batchie.service;

import com.batchie.domain.TrackingEvent;
import com.batchie.dto.TrackingEventDto;
import com.batchie.mapper.TrackingEventMapper;
import com.batchie.repository.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final TrackingEventMapper trackingEventMapper;

    public TrackingEventDto getShipmentById(String id) {
        return shipmentRepository.findById(id)
                .map(trackingEventMapper::toDto)
                .orElse(null);
    }

    public List<TrackingEventDto> getAllShipments() {
        return shipmentRepository.findAll().stream()
                .map(trackingEventMapper::toDto)
                .collect(Collectors.toList());
    }

    public void createShipment(TrackingEventDto trackingEventDto) {
        TrackingEvent trackingEvent = trackingEventMapper.toDomain(trackingEventDto);
        shipmentRepository.save(trackingEventMapper.toDto(trackingEvent));
    }

    public boolean updateShipment(String id, TrackingEventDto trackingEventDto) {
        if (shipmentRepository.existsById(id)) {
            TrackingEvent trackingEvent = trackingEventMapper.toDomain(trackingEventDto);
            trackingEvent.setShipmentId(id);
            shipmentRepository.save(trackingEventMapper.toDto(trackingEvent));
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteShipment(String id) {
        if (shipmentRepository.existsById(id)) {
            shipmentRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
