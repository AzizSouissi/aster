package com.batchie.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "shipments")
public class TrackingEventDto {
    @Id
    private String shipmentId;
    private String event;
    private String location;
    private String details;
}
