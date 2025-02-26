package com.batchie.domain;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PackageDetails {
    private double weight;
    private Dimensions dimensions;
    private String contentDescription;
    private double insuredValue;
    private String packageIdentifier; // Optional barcode or package-specific ID
}
