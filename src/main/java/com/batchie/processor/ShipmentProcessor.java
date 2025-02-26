package com.batchie.processor;

import com.batchie.domain.Shipment;
import com.batchie.domain.ShipmentResult;
import com.batchie.domain.Track;
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

@Component
public class ShipmentProcessor implements ItemProcessor<Shipment, ShipmentResult> {

    private static final Logger logger = LoggerFactory.getLogger(ShipmentProcessor.class);

    @Autowired
    private ShipmentProviderFactory providerFactory;

    private String currentProvider;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        JobParameters parameters = stepExecution.getJobParameters();
        this.currentProvider = parameters.getString("provider");
        logger.info("Processor initialized for provider: {}", currentProvider);
    }

    @Override
    public ShipmentResult process(Shipment shipment) throws Exception {
        logger.info("Processing shipment: {} for provider: {}", shipment.getTrackingNumber(), currentProvider);

        ShipmentResult result = new ShipmentResult();
        result.setShipment(shipment);

        try {
            TrackerService trackerService = providerFactory.getTrackerService(currentProvider);
            Track trackingData = trackerService.fetchTrackingDetails(shipment.getTrackingNumber());

            result.setTrack(trackingData);
            result.setSuccess(true);
            result.setTrackingUrl(trackerService.buildTrackingUrl(shipment.getTrackingNumber()));

            logger.info("Successfully processed shipment: {}", shipment.getTrackingNumber());

        } catch (Exception e) {
            logger.error("Error processing shipment: " + shipment.getTrackingNumber(), e);
            result.setSuccess(false);
            result.setErrorMessage(e.getMessage());
        }

        return result;
    }
}