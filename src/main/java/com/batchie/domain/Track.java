package com.batchie.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "tracks")
public class Track {
    @Id
    private String id;
    private String shipmentId;
    private String trackingNumber;
    private String carrier;
    private String status;
    private String location;
    private String details;
    private LocalDateTime timestamp;
}
