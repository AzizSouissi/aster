package com.batchie.service;

import com.batchie.domain.Shipment;
import com.batchie.domain.TrackingEvent;
import com.batchie.dto.ShipmentDto;
import com.batchie.mapper.ShipmentMapper;
import com.batchie.repository.ShipmentRepository;
import com.batchie.repository.TrackingEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final TrackingEventRepository trackingEventRepository;
    private final ShipmentMapper shipmentMapper;

    public ShipmentDto getShipmentById(String id) {
        return shipmentRepository.findById(id)
                .map(shipmentMapper::toDto)
                .orElse(null);
    }

    public List<ShipmentDto> getAllShipments() {
        return shipmentRepository.findAll().stream()
                .map(shipmentMapper::toDto)
                .collect(Collectors.toList());
    }

    public String createShipment(ShipmentDto shipmentDto) {
        Shipment shipment = shipmentMapper.toDomain(shipmentDto);
        shipment.setCreatedAt(LocalDateTime.now());
        shipment.setUpdatedAt(LocalDateTime.now());
        shipment = shipmentRepository.save(shipment);
        return shipment.getId();
    }

    public boolean updateShipment(String id, ShipmentDto shipmentDto) {
        return shipmentRepository.findById(id)
                .map(existingShipment -> {
                    Shipment updatedShipment = shipmentMapper.toDomain(shipmentDto);
                    updatedShipment.setId(id);
                    updatedShipment.setCreatedAt(existingShipment.getCreatedAt());
                    updatedShipment.setUpdatedAt(LocalDateTime.now());
                    shipmentRepository.save(updatedShipment);
                    return true;
                })
                .orElse(false);
    }

    public boolean deleteShipment(String id) {
        if (shipmentRepository.existsById(id)) {
            shipmentRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public void addTrackingEvent(String shipmentId, TrackingEvent trackingEvent) {
        shipmentRepository.findById(shipmentId).ifPresent(shipment -> {
            trackingEvent.setShipmentId(shipmentId);
            trackingEvent.setTimestamp(LocalDateTime.now());
            trackingEventRepository.save(trackingEvent);

            shipment.getTrackingEvents().add(trackingEvent);
            shipment.setUpdatedAt(LocalDateTime.now());
            shipmentRepository.save(shipment);
        });
    }

    public void processShipmentEvent(TrackingEvent trackingEvent) {
        trackingEventRepository.save(trackingEvent);
    }
}