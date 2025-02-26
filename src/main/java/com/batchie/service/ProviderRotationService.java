package com.batchie.service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ProviderRotationService {

    @Getter
    @Value("#{'${shipment.providers}'.split(',')}")
    private List<String> providers;

    private final AtomicInteger currentProviderIndex = new AtomicInteger(0);

    public String getNextProvider() {
        int index = currentProviderIndex.getAndUpdate(i -> (i + 1) % providers.size());
        return providers.get(index);
    }

    public void resetRotation() {
        currentProviderIndex.set(0);
    }

    public String getCurrentProvider() {
        return providers.get(currentProviderIndex.get() % providers.size());
    }
}