package com.batchie.reader;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.batchie.domain.Shipment;
import com.batchie.domain.ShipmentStatus;
import com.batchie.repository.ShipmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class ShipmentReader implements ItemReader<Shipment> {

    private static final Logger logger = LoggerFactory.getLogger(ShipmentReader.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Value("${shipment.batch.size:100}")
    private int batchSize;

    private List<Shipment> shipments;
    private final AtomicInteger index = new AtomicInteger(0);
    private String currentProvider;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        JobParameters parameters = stepExecution.getJobParameters();
        this.currentProvider = parameters.getString("provider");

        logger.info("Initializing reader for provider: {}", currentProvider);
        initialize(currentProvider);
    }

    /**
     * Initialize the reader with the current batch of shipments
     * @param provider The provider to fetch shipments for
     */
    public void initialize(String provider) {
        Query query = new Query();
        query.addCriteria(Criteria.where("status").is(ShipmentStatus.PENDING));
        query.addCriteria(Criteria.where("provider").is(provider));
        query.limit(batchSize);

        this.shipments = mongoTemplate.find(query, Shipment.class);
        this.index.set(0);
        logger.info("Loaded {} shipments for provider: {}",
                shipments != null ? shipments.size() : 0, provider);
    }

    @Override
    public Shipment read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (shipments == null || shipments.isEmpty()) {
            return null;
        }

        int i = index.getAndIncrement();
        if (i < shipments.size()) {
            Shipment shipment = shipments.get(i);
            logger.debug("Reading shipment: {}", shipment.getTrackingNumber());
            return shipment;
        }

        return null;
    }
}