package com.mastercard.send.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mastercard.send.exception.ConfigurationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.PartnerLimitsApi;
import org.openapitools.client.model.AlertSettings;
import org.openapitools.client.model.DailyLimitSetParent;
import org.openapitools.client.model.PartnerLimitDetailsWithSpend;
import org.openapitools.client.model.PartnerLimitPutDetailParent;
import org.openapitools.client.model.TransactionAmountLimit;
import org.openapitools.client.model.TransactionInititiatorLimitParent;
import org.openapitools.client.model.TransactionVolumeLimit;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mastercard.send.exception.WebErrorHandler;
import com.mastercard.send.service.MastercardSendService;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@PropertySource("classpath:application.properties")
class PartnerLimitsControllerTest {


    private PartnerLimitsController partnerLimitsController;

    @Mock
    private MastercardSendService MastercardSendService;

    @Mock
    private PartnerLimitsApi mockPartnerLimitsApi;

    private MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    ObjectWriter ow;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() throws Exception {
        closeable = MockitoAnnotations.openMocks(this);
        partnerLimitsController = spy(new PartnerLimitsController());
        partnerLimitsController.setMastercardSendService(MastercardSendService);
        doReturn(mockPartnerLimitsApi).when(partnerLimitsController).getPartnerLimitsApi();
        mockMvc = MockMvcBuilders.standaloneSetup(partnerLimitsController).setControllerAdvice(new WebErrorHandler())
                .build();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ow = mapper.writer().withDefaultPrettyPrinter();
    }

    @AfterEach
    void releaseMocks() throws Exception{
        closeable.close();
    }

    @Test
    void should_getPartnerLimit() throws Exception {
        when(mockPartnerLimitsApi.getPartnerLimit("ptnr_1lzs0zD6uWo7k-OdUXjLW_pFgX2", MediaType.APPLICATION_JSON_VALUE)).thenReturn(Collections.singletonList(new PartnerLimitDetailsWithSpend()));
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/ptnr_1lzs0zD6uWo7k-OdUXjLW_pFgX2/limits")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void should_updatePartnerLimit() throws Exception {
        PartnerLimitPutDetailParent partnerLimitPutDetailParent = getPartnerLimitRequest();
        when(mockPartnerLimitsApi.putPartnerLimit("ptnr_1lzs0zD6uWo7k-OdUXjLW_pFgX2", "8f25edf2fb524ddcbc5869aedede8c27fc9caec6", partnerLimitPutDetailParent, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE)).thenReturn(new TransactionInititiatorLimitParent());
        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/ptnr_1lzs0zD6uWo7k-OdUXjLW_pFgX2/limits/daily-limits/8f25edf2fb524ddcbc5869aedede8c27fc9caec6")
                        .content(ow.writeValueAsString(partnerLimitPutDetailParent))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void should_throwApiException() throws Exception {
        PartnerLimitPutDetailParent partnerLimitPutDetailParent = getPartnerLimitRequest();
        when(mockPartnerLimitsApi.putPartnerLimit("ptnr_1lzs0zD6uWo7k-OdUXjLW_pFgX2", "REF_0001", partnerLimitPutDetailParent, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE)).thenThrow(new ApiException(400, "Bad Request"));
        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/ptnr_1lzs0zD6uWo7k-OdUXjLW_pFgX2/limits/daily-limits/REF_0001")
                        .content(ow.writeValueAsString(partnerLimitPutDetailParent))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ApiException));
    }

    @Test
    void should_throwConfigurationException() throws Exception {
        PartnerLimitPutDetailParent partnerLimitPutDetailParent = getPartnerLimitRequest();
        when(mockPartnerLimitsApi.putPartnerLimit("ptnr_1lzs0zD6uWo7k-OdUXjLW_pFgX2", "REF_0001", partnerLimitPutDetailParent, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE)).thenThrow(new ConfigurationException("Authentication section of the application.properties file is not updated"));
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/ptnr_1lzs0zD6uWo7k-OdUXjLW_pFgX2/limits/daily-limits/REF_0001")
                                .content(ow.writeValueAsString(partnerLimitPutDetailParent))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .header("Accept", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ConfigurationException));
    }

    @Test
    void should_throwException() throws Exception {
        when(partnerLimitsController.getPartnerLimitsApi()).thenCallRealMethod();

        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/ptnr_1lzs0zD6uWo7k-OdUXjLW_pFgX2/limits/daily-limits/REF_0001")
                        .content(ow.writeValueAsString(getPartnerLimitRequest()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(result -> assertNotNull(result.getResolvedException()));
    }

    private PartnerLimitPutDetailParent getPartnerLimitRequest() {
        PartnerLimitPutDetailParent partnerLimitPutDetailParent = new PartnerLimitPutDetailParent();

        DailyLimitSetParent dailyLimitSetParent = new DailyLimitSetParent();
        TransactionAmountLimit transactionAmountLimit = new TransactionAmountLimit();
        transactionAmountLimit.setAmount(1000000);
        transactionAmountLimit.setCurrency("INR");
        dailyLimitSetParent.setTransactionAmountLimit(transactionAmountLimit);

        TransactionVolumeLimit transactionVolumeLimit = new TransactionVolumeLimit();
        transactionVolumeLimit.setMaxTransactionVolume(5000);
        dailyLimitSetParent.setTransactionVolumeLimit(transactionVolumeLimit);

        partnerLimitPutDetailParent.setDailyLimit(dailyLimitSetParent);

        AlertSettings alertSettings = new AlertSettings();
        alertSettings.setAlertsEnabled(true);
        List<String> alertEmails = new ArrayList<>();
        alertEmails.add("test123@abcmail.com");
        alertEmails.add("test456@abcmail.com");
        alertSettings.alertEmails(alertEmails);

        alertSettings.setAmountAlertThreshold(BigDecimal.valueOf(95));
        alertSettings.setVolumeAlertThreshold(BigDecimal.valueOf(80));

        partnerLimitPutDetailParent.setAlertSettings(alertSettings);

        return partnerLimitPutDetailParent;
    }
}
