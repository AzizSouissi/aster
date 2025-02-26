package com.batchie.domain;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Dimensions {
    private double length;
    private double width;
    private double height;
    private String unit; // e.g., "in", "cm"
}
