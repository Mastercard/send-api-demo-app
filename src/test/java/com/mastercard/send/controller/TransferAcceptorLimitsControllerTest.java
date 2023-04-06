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
import org.openapitools.client.api.TransferAcceptorLimitsApi;
import org.openapitools.client.model.TransferAcceptorAlertSettings;
import org.openapitools.client.model.TransferAcceptorDailyLimitSetParent;
import org.openapitools.client.model.TransferAcceptorLimitParent;
import org.openapitools.client.model.TransferAcceptorLimitPostDetailParent;
import org.openapitools.client.model.TransferAcceptorLimitPutDetailParent;
import org.openapitools.client.model.TransferAcceptorTransactionAmountLimit;
import org.openapitools.client.model.TransferAcceptorTransactionVolumeLimit;
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
class TransferAcceptorLimitsControllerTest {


    private TransferAcceptorLimitsController transferAcceptorLimitsController;

    @Mock
    private MastercardSendService MastercardSendService;

    @Mock
    private TransferAcceptorLimitsApi mockTransferAcceptorLimitsApi;

    private MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    ObjectWriter ow;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() throws Exception {
        closeable = MockitoAnnotations.openMocks(this);
        transferAcceptorLimitsController = spy(new TransferAcceptorLimitsController());
        transferAcceptorLimitsController.setMastercardSendService(MastercardSendService);
        doReturn(mockTransferAcceptorLimitsApi).when(transferAcceptorLimitsController).getTransferAcceptorLimitsApi();
        mockMvc = MockMvcBuilders.standaloneSetup(transferAcceptorLimitsController)
                .setControllerAdvice(new WebErrorHandler())
                .build();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ow = mapper.writer().withDefaultPrettyPrinter();

    }

    @AfterEach
    void releaseMocks() throws Exception{
        closeable.close();
    }

    @Test
    void should_addAccountLimit() throws Exception {
        TransferAcceptorLimitPostDetailParent transferAcceptorLimit = getTransferAcceptorLimitPostRequest();
        when(mockTransferAcceptorLimitsApi.postTransferAcceptorLimit("ptnr_1lzs0zD6uWo7k-OdUXjLW_pFgX2", transferAcceptorLimit, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE )).thenReturn(new TransferAcceptorLimitParent());
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/ptnr_1lzs0zD6uWo7k-OdUXjLW_pFgX2/limits/daily-limits/transfer-acceptor-limits")
                        .content(ow.writeValueAsString(transferAcceptorLimit))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void should_updateAccountLimit() throws Exception {
        TransferAcceptorLimitPutDetailParent transferAcceptorLimit = getTransferAcceptorLimitPutRequest();
        when(mockTransferAcceptorLimitsApi.putTransferAcceptorLimit("ptnr_1lzs0zD6uWo7k-OdUXjLW_pFgX2", "2371d04df0d44281b52a88e8b2067926b0a7a51c", transferAcceptorLimit, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE)).thenReturn(new TransferAcceptorLimitParent());
        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/ptnr_1lzs0zD6uWo7k-OdUXjLW_pFgX2/limits/transfer-acceptor-limits/2371d04df0d44281b52a88e8b2067926b0a7a51c")
                        .content(ow.writeValueAsString(transferAcceptorLimit))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void should_throwApiException() throws Exception {
        TransferAcceptorLimitPutDetailParent transferAcceptorLimit = getTransferAcceptorLimitPutRequest();
        when(mockTransferAcceptorLimitsApi.putTransferAcceptorLimit("ptnr_1lzs0zD6uWo7k-OdUXjLW_pFgX2", "REF_0001", transferAcceptorLimit, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE)).thenThrow(new ApiException(400, "Bad Request"));
        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/ptnr_1lzs0zD6uWo7k-OdUXjLW_pFgX2/limits/transfer-acceptor-limits/REF_0001")
                        .content(ow.writeValueAsString(getTransferAcceptorLimitPostRequest()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ApiException));
    }

    @Test
    void should_throwConfigurationException() throws Exception {
        TransferAcceptorLimitPutDetailParent transferAcceptorLimit = getTransferAcceptorLimitPutRequest();
        when(mockTransferAcceptorLimitsApi.putTransferAcceptorLimit("ptnr_1lzs0zD6uWo7k-OdUXjLW_pFgX2", "REF_0001", transferAcceptorLimit, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE)).thenThrow(new ConfigurationException("Authentication section of the application.properties file is not updated"));
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/ptnr_1lzs0zD6uWo7k-OdUXjLW_pFgX2/limits/transfer-acceptor-limits/REF_0001")
                                .content(ow.writeValueAsString(getTransferAcceptorLimitPostRequest()))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .header("Accept", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ConfigurationException));
    }

    @Test
    void should_throwException() throws Exception {
        when(transferAcceptorLimitsController.getTransferAcceptorLimitsApi()).thenCallRealMethod();
        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/ptnr_1lzs0zD6uWo7k-OdUXjLW_pFgX2/limits/transfer-acceptor-limits/REF_0001")
                        .content(ow.writeValueAsString(getTransferAcceptorLimitPostRequest()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(result -> assertNotNull(result.getResolvedException()));
    }

    private TransferAcceptorLimitPostDetailParent getTransferAcceptorLimitPostRequest() {
        TransferAcceptorLimitPostDetailParent transferAcceptorLimitPostDetailParent = new TransferAcceptorLimitPostDetailParent();
        transferAcceptorLimitPostDetailParent.setTransferAcceptorId("456487898368123");

        TransferAcceptorDailyLimitSetParent dailyLimitSetParent = new TransferAcceptorDailyLimitSetParent();
        TransferAcceptorTransactionAmountLimit transactionAmountLimit = new TransferAcceptorTransactionAmountLimit();
        transactionAmountLimit.setAmount(1000000);
        transactionAmountLimit.setCurrency("INR");
        dailyLimitSetParent.setTransactionAmountLimit(transactionAmountLimit);

        TransferAcceptorTransactionVolumeLimit transactionVolumeLimit = new TransferAcceptorTransactionVolumeLimit();
        transactionVolumeLimit.setMaxTransactionVolume(5000);
        dailyLimitSetParent.setTransactionVolumeLimit(transactionVolumeLimit);

        transferAcceptorLimitPostDetailParent.setDailyLimit(dailyLimitSetParent);

        TransferAcceptorAlertSettings alertSettings = new TransferAcceptorAlertSettings();
        List<String> alertEmails = new ArrayList<>();
        alertEmails.add("test123@abcmail.com");
        alertEmails.add("test456@abcmail.com");
        alertSettings.alertEmails(alertEmails);

        alertSettings.setAmountAlertThreshold(BigDecimal.valueOf(95));
        alertSettings.setVolumeAlertThreshold(BigDecimal.valueOf(80));

        transferAcceptorLimitPostDetailParent.setAlertSettings(alertSettings);

        return transferAcceptorLimitPostDetailParent;
    }

    private TransferAcceptorLimitPutDetailParent getTransferAcceptorLimitPutRequest() {
        TransferAcceptorLimitPutDetailParent transferAcceptorLimitPutDetailParent = new TransferAcceptorLimitPutDetailParent();

        TransferAcceptorDailyLimitSetParent dailyLimitSetParent = new TransferAcceptorDailyLimitSetParent();
        TransferAcceptorTransactionAmountLimit transactionAmountLimit = new TransferAcceptorTransactionAmountLimit();
        transactionAmountLimit.setAmount(1000000);
        transactionAmountLimit.setCurrency("INR");
        dailyLimitSetParent.setTransactionAmountLimit(transactionAmountLimit);

        TransferAcceptorTransactionVolumeLimit transactionVolumeLimit = new TransferAcceptorTransactionVolumeLimit();
        transactionVolumeLimit.setMaxTransactionVolume(5000);
        dailyLimitSetParent.setTransactionVolumeLimit(transactionVolumeLimit);

        transferAcceptorLimitPutDetailParent.setDailyLimit(dailyLimitSetParent);

        TransferAcceptorAlertSettings alertSettings = new TransferAcceptorAlertSettings();
        List<String> alertEmails = new ArrayList<>();
        alertEmails.add("test123@abcmail.com");
        alertEmails.add("test456@abcmail.com");
        alertSettings.alertEmails(alertEmails);

        alertSettings.setAmountAlertThreshold(BigDecimal.valueOf(95));
        alertSettings.setVolumeAlertThreshold(BigDecimal.valueOf(80));

        transferAcceptorLimitPutDetailParent.setAlertSettings(alertSettings);

        return transferAcceptorLimitPutDetailParent;
    }
}
