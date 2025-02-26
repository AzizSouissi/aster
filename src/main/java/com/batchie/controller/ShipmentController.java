package com.batchie.controller;

import com.batchie.dto.TrackingEventDto;
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
    public ResponseEntity<TrackingEventDto> getShipmentById(@PathVariable String id) {
        TrackingEventDto trackingEventDto = shipmentService.getShipmentById(id);
        if (trackingEventDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(trackingEventDto);
    }

    @GetMapping
    public ResponseEntity<List<TrackingEventDto>> getAllShipments() {
        List<TrackingEventDto> shipments = shipmentService.getAllShipments();
        return ResponseEntity.ok(shipments);
    }

    @PostMapping
    public ResponseEntity<String> createShipment(@Valid @RequestBody TrackingEventDto trackingEventDto) {
        shipmentService.createShipment(trackingEventDto);
        return ResponseEntity.ok("Shipment created successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateShipment(@PathVariable String id, @Valid @RequestBody TrackingEventDto trackingEventDto) {
        boolean updated = shipmentService.updateShipment(id, trackingEventDto);
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
}
