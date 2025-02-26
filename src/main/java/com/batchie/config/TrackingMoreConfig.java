package com.batchie.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "trackingmore")
@Data
public class TrackingMoreConfig {
    private String apiKey;
    private String webhookSecret;
    private String endpoint = "https://api.trackingmore.com/v4";
    private int timeout = 30;
}