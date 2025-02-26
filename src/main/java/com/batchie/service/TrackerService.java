package com.batchie.service;

import com.batchie.domain.Track;

public interface TrackerService {

    String buildTrackingUrl(String trackingNumber);
    Track fetchTrackingDetails(String trackingNumber);
}