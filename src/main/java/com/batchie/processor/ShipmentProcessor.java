package com.batchie.processor;

import com.batchie.domain.EventType;
import com.batchie.domain.Shipment;
import com.batchie.domain.ShipmentResult;
import com.batchie.domain.TrackingEvent;
import com.batchie.service.TrackerService;
import com.batchie.service.handler.ShipmentProviderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ShipmentProcessor implements ItemProcessor<Shipment, ShipmentResult> {

    private static final Logger logger = LoggerFactory.getLogger(ShipmentProcessor.class);

    @Autowired
    private ShipmentProviderFactory providerFactory;

    private TrackerService trackerService;
    private String currentProvider;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        JobParameters parameters = stepExecution.getJobParameters();
        this.currentProvider = parameters.getString("provider");
        logger.info("Processor initialized for provider: {}", currentProvider);

        try {
            // Initialize the tracker service based on provider early to fail fast
            this.trackerService = providerFactory.getTrackerService(currentProvider);
        } catch (Exception e) {
            logger.error("Failed to initialize tracker service for provider: {}", currentProvider, e);
            throw e;
        }
    }

    @Override
    public ShipmentResult process(Shipment shipment) throws Exception {
        logger.info("Processing shipment: {} for provider: {}", shipment.getTrackingNumber(), currentProvider);

        ShipmentResult result = new ShipmentResult();
        result.setShipment(shipment);

        try {
            // We'll create a tracking event for this shipment
            TrackingEvent trackingEvent = TrackingEvent.builder()
                    .shipmentId(shipment.getId())
                    .eventType(EventType.valueOf("PROCESSING"))
                    .details("Shipment processing initiated")
                    .timestamp(LocalDateTime.now())
                    .build();

            // Get tracking URL from the service
            String trackingUrl = trackerService.buildTrackingUrl(shipment.getTrackingNumber());

            result.setSuccess(true);
            result.setTrackingUrl(trackingUrl);
            result.setTrackingEvent(trackingEvent);

            logger.info("Successfully processed shipment: {}", shipment.getTrackingNumber());

        } catch (Exception e) {
            logger.error("Error processing shipment: " + shipment.getTrackingNumber(), e);
            result.setSuccess(false);
            result.setErrorMessage(e.getMessage());
        }

        return result;
    }
}