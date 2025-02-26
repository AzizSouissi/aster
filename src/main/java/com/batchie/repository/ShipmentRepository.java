package com.batchie.repository;

import com.batchie.dto.TrackingEventDto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentRepository extends MongoRepository<TrackingEventDto, String> {
}
