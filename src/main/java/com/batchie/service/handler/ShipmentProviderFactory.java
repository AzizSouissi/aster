package com.batchie.service.handler;

import com.batchie.service.FedExTrackerService;
import com.batchie.service.TrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShipmentProviderFactory {

    @Autowired
    private FedExTrackerService fedExService;

    public TrackerService getTrackerService(String provider) {
        switch (provider.toLowerCase()) {
            case "fedex":
                return fedExService;
            default:
                throw new IllegalArgumentException("Unsupported provider: " + provider);
        }
    }
}