package com.batchie.controller;

import com.batchie.domain.TrackingEvent;
import com.batchie.dto.ShipmentDto;
import com.batchie.service.ShipmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shipments")
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentService shipmentService;

    @GetMapping("/{id}")
    public ResponseEntity<ShipmentDto> getShipmentById(@PathVariable String id) {
        ShipmentDto shipmentDto = shipmentService.getShipmentById(id);
        if (shipmentDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(shipmentDto);
    }

    @GetMapping
    public ResponseEntity<List<ShipmentDto>> getAllShipments() {
        List<ShipmentDto> shipments = shipmentService.getAllShipments();
        return ResponseEntity.ok(shipments);
    }

    @PostMapping
    public ResponseEntity<String> createShipment(@Valid @RequestBody ShipmentDto shipmentDto) {
        String shipmentId = shipmentService.createShipment(shipmentDto);
        return ResponseEntity.ok("Shipment created successfully with ID: " + shipmentId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateShipment(@PathVariable String id, @Valid @RequestBody ShipmentDto shipmentDto) {
        boolean updated = shipmentService.updateShipment(id, shipmentDto);
        if (updated) {
            return ResponseEntity.ok("Shipment updated successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteShipment(@PathVariable String id) {
        boolean deleted = shipmentService.deleteShipment(id);
        if (deleted) {
            return ResponseEntity.ok("Shipment deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/tracking-events")
    public ResponseEntity<String> addTrackingEvent(@PathVariable String id, @Valid @RequestBody TrackingEvent trackingEvent) {
        shipmentService.addTrackingEvent(id, trackingEvent);
        return ResponseEntity.ok("Tracking event added successfully");
    }
}