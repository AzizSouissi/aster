package com.batchie.service;

import com.batchie.domain.TrackingEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class FedExTrackerService implements TrackerService {

    private final RestTemplate restTemplate;

    @Autowired
    public FedExTrackerService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${fedex.service-endpoint}")
    private String serviceEndpoint;

    @Value("${fedex.tracking-url}")
    private String trackingUrl;

    @Override
    public String buildTrackingUrl(String trackingNumber) {
        return UriComponentsBuilder.fromUriString(trackingUrl)
                .queryParam("tracknumbers", trackingNumber)
                .toUriString();
    }

    @Override
    public TrackingEvent fetchTrackingDetails(String trackingNumber) {
        String requestBody = buildRequestBody(trackingNumber);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(serviceEndpoint, HttpMethod.POST, entity, String.class);
        return buildResponse(response.getBody());
    }

    private String buildRequestBody(String trackingNumber) {
        return "{\n" +
                "  \"TrackPackagesRequest\": {\n" +
                "    \"trackingInfoList\": [\n" +
                "      {\"trackNumberInfo\": {\"trackingNumber\": \"" + trackingNumber + "\"}}\n" +
                "    ]\n" +
                "  }\n" +
                "}";
    }

    private TrackingEvent buildResponse(String response) {
        // Parse the JSON response here and return Track with events
        TrackingEvent track = new TrackingEvent();

        // Assuming you have a way to parse the response to fill Track object
        // Parse the response, and populate the Track object
        // For example, using Jackson or Gson to parse JSON into Track objects

        // Implement your JSON parsing logic here

        return track;
    }
}
