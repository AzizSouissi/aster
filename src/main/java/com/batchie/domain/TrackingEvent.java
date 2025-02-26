package com.batchie.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "tracking_events")
public class TrackingEvent {
    @Id
    private String id;
    private String shipmentId; // Reference to parent shipment

    private EventType eventType; // Standardized event type
    private String description; // Human-readable description
    private String location; // Where the event occurred
    private String locationCode; // Optional facility/location code
    private String details; // Additional notes or details

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    // Which carrier provided this status update
    private String source;
}
