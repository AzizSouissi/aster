package com.batchie.domain;

public enum EventType {
    LABEL_CREATED,
    PICKED_UP,
    IN_TRANSIT,
    OUT_FOR_DELIVERY,
    DELIVERY_ATTEMPTED,
    DELIVERED,
    EXCEPTION,
    DELAYED,
    CUSTOMS_HOLD,
    RETURN_TO_SENDER;
}
