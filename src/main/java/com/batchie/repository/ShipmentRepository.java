package com.batchie.repository;

import com.batchie.domain.TrackingEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentRepository extends MongoRepository<TrackingEvent, String> {
}
