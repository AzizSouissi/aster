package com.batchie.controller;

import com.batchie.service.WebhookService;
import com.batchie.service.handler.TrackingMoreWebhookHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TrackingMoreControllerTest {

    private MockMvc mockMvc;

    @Mock
    private WebhookService webhookService;

    @Mock
    private TrackingMoreWebhookHandler trackingMoreWebhookHandler;

    private WebhookController webhookController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        webhookController = new WebhookController(webhookService, trackingMoreWebhookHandler);
        mockMvc = MockMvcBuilders.standaloneSetup(webhookController).build();
    }

    @Test
    public void testHandleTrackingMoreWebhook_Success() throws Exception {
        String payload = "{\n" +
                "  \"data\": {\n" +
                "    \"tracking_number\": \"1Z9999999999999999\",\n" +
                "    \"carrier_code\": \"ups\",\n" +
                "    \"status\": \"delivered\"\n" +
                "  }\n" +
                "}";

        when(trackingMoreWebhookHandler.validateWebhook(anyString(), anyString())).thenReturn(true);
        doNothing().when(webhookService).processWebhook(anyString(), contains("trackingmore"));

        mockMvc.perform(post("/webhook/trackingmore")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-TrackingMore-Signature", "test-signature")
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(content().string("Webhook processed successfully"));
    }

    @Test
    public void testHandleTrackingMoreWebhook_InvalidSignature() throws Exception {
        String payload = "{\n" +
                "  \"data\": {\n" +
                "    \"tracking_number\": \"1Z9999999999999999\",\n" +
                "    \"carrier_code\": \"ups\",\n" +
                "    \"status\": \"delivered\"\n" +
                "  }\n" +
                "}";

        when(trackingMoreWebhookHandler.validateWebhook(anyString(), anyString())).thenReturn(false);

        mockMvc.perform(post("/webhook/trackingmore")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-TrackingMore-Signature", "invalid-signature")
                        .content(payload))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid signature"));
    }
}