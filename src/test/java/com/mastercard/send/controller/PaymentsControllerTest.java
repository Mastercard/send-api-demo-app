package com.mastercard.send.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import com.mastercard.send.exception.ConfigurationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.PaymentsApi;
import org.openapitools.client.api.PaymentsSearchesApi;
import org.openapitools.client.model.AcquiringCredentials;
import org.openapitools.client.model.DualMessage;
import org.openapitools.client.model.PaymentCreateData;
import org.openapitools.client.model.PaymentDetails;
import org.openapitools.client.model.PaymentSearchData;
import org.openapitools.client.model.PaymentSearchDetails;
import org.openapitools.client.model.Recipient;
import org.openapitools.client.model.Sender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
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
class PaymentsControllerTest {


    private PaymentsController paymentsController;

    @Mock
    private MastercardSendService mastercardSendService;

    @Mock
    private PaymentsApi mockPaymentsApi;

    @Mock
    private PaymentsSearchesApi mockPaymentsSearchesApi;

    private MockMvc mockMvc;

    @Value("${partnerId}")
    private String partnerId;

    ObjectMapper mapper = new ObjectMapper();

    ObjectWriter ow;

    private static final String paymentId = UUID.randomUUID().toString();

    private AutoCloseable closeable;

    @BeforeEach
    void setUp(){
        closeable = MockitoAnnotations.openMocks(this);
        paymentsController = spy(new PaymentsController());
        paymentsController.setMastercardSendService(mastercardSendService);
        ReflectionTestUtils.setField(paymentsController, "partnerId", partnerId);
        mockMvc = MockMvcBuilders.standaloneSetup(paymentsController).setControllerAdvice(new WebErrorHandler())
                .build();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ow = mapper.writer().withDefaultPrettyPrinter();
    }

    @AfterEach
    void releaseMocks() throws Exception{
        closeable.close();
    }

    @Test
    void should_createPayments() throws Exception {
        doReturn(mockPaymentsApi).when(paymentsController).getPaymentsApi();
        PaymentCreateData paymentCreateData = getPaymentCreateData();
        when(mockPaymentsApi.createPaymentUsingPOST(partnerId, paymentCreateData,false, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE)).thenReturn(new PaymentDetails());
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/payments")
                        .content(ow.writeValueAsString(paymentCreateData))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                        .header("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void should_retrievePaymentInfo() throws Exception {
        doReturn(mockPaymentsSearchesApi).when(paymentsController).getPaymentSearchesApi();
        PaymentSearchData paymentSearchData = getPaymentSearchData();
        when(mockPaymentsSearchesApi.retrievePaymentInfoUsingPOST(partnerId, paymentSearchData, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE)).thenReturn(new PaymentSearchDetails());
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/payments/searches")
                        .content(ow.writeValueAsString(paymentSearchData))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                        .header("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void should_throwApiException() throws Exception {
        doReturn(mockPaymentsApi).when(paymentsController).getPaymentsApi();
        PaymentCreateData paymentCreateData = getPaymentCreateData();
        when(mockPaymentsApi.createPaymentUsingPOST(partnerId, paymentCreateData,false, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE)).thenThrow(new ApiException(409, "Bad Request"));
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/payments")
                                .content(ow.writeValueAsString(paymentCreateData))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isConflict())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ApiException));
    }

    @Test
    void should_throwConfigurationException() throws Exception {
        doReturn(mockPaymentsApi).when(paymentsController).getPaymentsApi();
        PaymentCreateData paymentCreateData = getPaymentCreateData();
        when(mockPaymentsApi.createPaymentUsingPOST(partnerId, paymentCreateData,false, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE)).thenThrow(new ConfigurationException("Authentication section of the application.properties file is not updated"));
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/payments")
                                .content(ow.writeValueAsString(paymentCreateData))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ConfigurationException));
    }

    @Test
    void should_paymentThrowsException() throws Exception {
        when(paymentsController.getPaymentsApi()).thenCallRealMethod();
        PaymentCreateData paymentCreateData = getPaymentCreateData();
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/payments")
                                .content(ow.writeValueAsString(paymentCreateData))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(result -> assertNotNull(result.getResolvedException()));
    }

    @Test
    void should_SearchesThrowsException() throws Exception {
        when(paymentsController.getPaymentSearchesApi()).thenCallRealMethod();

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/payments/searches")
                        .content(ow.writeValueAsString(getPaymentSearchData()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                        .header("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(result -> assertNotNull(result.getResolvedException()));
    }

    private PaymentCreateData getPaymentCreateData() {
        PaymentCreateData paymentCreateData = new PaymentCreateData();
        paymentCreateData.setPaymentReference(paymentId);
        paymentCreateData.setAmount("6000");
        paymentCreateData.setInterchangeRateDesignator("QR");
        paymentCreateData.setCurrency("USD");
        paymentCreateData.setAdditionalMessage("Adding additional message");
        paymentCreateData.setDeviceType("WEB");
        paymentCreateData.setFundingSource("05");
        paymentCreateData.setMerchantCategoryCode("6536");
        paymentCreateData.setParticipationId("1234567890");
        paymentCreateData.setPaymentType("P2P");

        AcquiringCredentials acquiringCredentials = new AcquiringCredentials();
        acquiringCredentials.setAcquiringCountry("USA");
        acquiringCredentials.setAcquiringIca("123456");

        DualMessage dualMessage = new DualMessage();
        dualMessage.setAcquiringBin("512340");

        acquiringCredentials.setDualMessage(dualMessage);

        paymentCreateData.setAcquiringCredentials(acquiringCredentials);

        Recipient recipient = new Recipient();
        recipient.setAccountUri("pan:5117510000000007;exp=2050-02;cvc=123");
        paymentCreateData.setRecipient(recipient);

        Sender sender = new Sender();
        sender.setAccountUri("pan:5117510000000007;exp=2050-02;cvc=123");
        paymentCreateData.setSender(sender);

        paymentCreateData.setTransactionLocalDateTime("2021-06-17T18:05:02");
        paymentCreateData.setPointOfServiceInteraction("MAGSTRIPE");
        paymentCreateData.setTransactionPurpose("07");
        paymentCreateData.setUniqueTransactionReference("0555555128315305401");

        return paymentCreateData;
    }

    private PaymentSearchData getPaymentSearchData() {
        PaymentSearchData paymentSearchData = new PaymentSearchData();
        paymentSearchData.setPaymentReference("pay_000001328");

        return paymentSearchData;
    }
}
