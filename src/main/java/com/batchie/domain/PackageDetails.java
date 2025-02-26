package com.batchie.domain;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PackageDetails {
    private double weight;
    private Dimensions dimensions;
    private String contentDescription;
    private double insuredValue;
}
