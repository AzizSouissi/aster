package com.batchie.domain;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Dimensions {
    private double length;
    private double width;
    private double height;
}
