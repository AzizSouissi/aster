package com.batchie.service;

import com.batchie.domain.TrackingEvent;

public interface TrackerService {

    String buildTrackingUrl(String trackingNumber);
    TrackingEvent fetchTrackingDetails(String trackingNumber);
}