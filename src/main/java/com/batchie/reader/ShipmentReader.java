package com.batchie.reader;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.batchie.domain.Shipment;
import com.batchie.domain.ShipmentStatus;
import com.batchie.repository.ShipmentRepository;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ShipmentReader implements ItemReader<Shipment> {

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Value("${shipment.batch.size:100}")
    private int batchSize;

    private List<Shipment> shipments;
    private final AtomicInteger index = new AtomicInteger(0);

    /**
     * Initialize the reader with the current batch of shipments
     * @param provider The provider to fetch shipments for
     */
    public void initialize(String provider) {
        this.shipments = shipmentRepository.findByStatusAndProvider(ShipmentStatus.PENDING, provider, batchSize);
        this.index.set(0);
    }

    @Override
    public Shipment read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (shipments == null || shipments.isEmpty()) {
            return null;
        }

        int i = index.getAndIncrement();
        if (i < shipments.size()) {
            return shipments.get(i);
        }

        return null;
    }
}