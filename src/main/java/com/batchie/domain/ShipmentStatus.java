package com.batchie.domain;

public enum ShipmentStatus {
    PENDING,
    LABEL_CREATED,
    PICKED_UP,
    IN_TRANSIT,
    OUT_FOR_DELIVERY,
    DELIVERED,
    EXCEPTION,
    PROCESSED,
    CANCELLED,
    FAILED;
}
