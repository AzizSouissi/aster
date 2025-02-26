package com.batchie.repository;

import com.batchie.domain.Shipment;
import com.batchie.domain.ShipmentStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShipmentRepository extends MongoRepository<Shipment, String> {
    @Query("{ 'status': ?0, 'provider': ?1 }")
    List<Shipment> findByStatusAndProvider(ShipmentStatus status, String provider, int limit);
}