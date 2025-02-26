package com.batchie.util;

import com.batchie.dto.TrackingEventDto;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.time.LocalDateTime;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Component
public class WebhookUtil {

    private final ObjectMapper objectMapper;

    public WebhookUtil(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public boolean isSoapRequest(String contentType) {
        return contentType != null && contentType.contains("xml");
    }

    public boolean isRestRequest(String contentType) {
        return contentType != null && contentType.contains("json");
    }

    public TrackingEventDto parseJsonPayload(String rawRequest) {
        try {
            return objectMapper.readValue(rawRequest, TrackingEventDto.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public TrackingEventDto parseSoapPayload(String rawRequest) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(rawRequest)));

            String event = getXmlValue(document, "event");
            String timestamp = getXmlValue(document, "timestamp");
            String location = getXmlValue(document, "location");
            String details = getXmlValue(document, "details");
            String shipmentId = getXmlValue(document, "shipmentId");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime parsedTimestamp = timestamp != null ? LocalDateTime.parse(timestamp, formatter) : null;

            return TrackingEventDto.builder()
                    .event(event)
                    .timestamp(parsedTimestamp)
                    .location(location)
                    .details(details)
                    .shipmentId(shipmentId)
                    .build();
        } catch (Exception e) {
            return null;
        }
    }

    private String getXmlValue(Document document, String tagName) {
        if (document.getElementsByTagName(tagName).getLength() > 0) {
            return document.getElementsByTagName(tagName).item(0).getTextContent();
        }
        return null;
    }
}
