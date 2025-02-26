package com.batchie.writer;

import com.batchie.domain.Shipment;
import com.batchie.domain.ShipmentResult;
import com.batchie.domain.ShipmentStatus;
import com.batchie.repository.ShipmentRepository;
import com.batchie.repository.TrackingEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ShipmentWriter implements ItemWriter<ShipmentResult> {

    private static final Logger logger = LoggerFactory.getLogger(ShipmentWriter.class);

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private TrackingEventRepository trackingRepository;

    @Override
    public void write(Chunk<? extends ShipmentResult> results) throws Exception {
        logger.info("Writing {} shipment results", results.size());

        for (ShipmentResult result : results) {
            Shipment shipment = result.getShipment();

            if (result.isSuccess()) {
                // Update shipment with tracking data
                shipment.setStatus(ShipmentStatus.PROCESSED);

                // Save updated shipment
                shipmentRepository.save(shipment);
                logger.info("Updated shipment: {}", shipment.getTrackingNumber());
            }
        }
    }
}