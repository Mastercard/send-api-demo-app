package com.mastercard.send.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.client.api.TransferEligibilityApi;
import org.openapitools.client.model.AcquiringCredentials;
import org.openapitools.client.model.DualMessage;
import org.openapitools.client.model.TransferEligibilityCheckData;
import org.openapitools.client.model.TransferEligibilityDetails;
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
class TransferEligibilityControllerTest {

    @Mock
    private MastercardSendService mastercardSendService;

    @Mock
    private TransferEligibilityApi mockTransferEligibilityApi;

    private TransferEligibilityController transferEligibilityController;


    private MockMvc mockMvc;

    @Value("${partnerId}")
    private String partnerId;

    ObjectMapper mapper = new ObjectMapper();

    ObjectWriter ow;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() throws Exception {
        closeable = MockitoAnnotations.openMocks(this);
        transferEligibilityController = spy(new TransferEligibilityController());
        ReflectionTestUtils.setField(transferEligibilityController, "partnerId", partnerId);
        transferEligibilityController.setMastercardSendService(mastercardSendService);
        doReturn(mockTransferEligibilityApi).when(transferEligibilityController).getTransferEligibilityApi();
        mockMvc = MockMvcBuilders.standaloneSetup(transferEligibilityController).setControllerAdvice(new WebErrorHandler())
                .build();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ow = mapper.writer().withDefaultPrettyPrinter();
    }

    @AfterEach
    void releaseMocks() throws Exception{
        closeable.close();
    }

    @Test
    void should_checkTransferEligibility() throws Exception {
        TransferEligibilityCheckData transferEligibilityCheckData = getTransferEligibilityCheckData();
        when(mockTransferEligibilityApi.checkTransferEligibilityUsingPOST(partnerId, transferEligibilityCheckData, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE)).thenReturn(new TransferEligibilityDetails());
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/transfer-eligibilities")
                        .content(ow.writeValueAsString(transferEligibilityCheckData))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                        .header("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void should_expectException() throws Exception {
        TransferEligibilityCheckData transferEligibilityCheckData = getTransferEligibilityCheckData();
        when(transferEligibilityController.getTransferEligibilityApi()).thenCallRealMethod();
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/transfer-eligibilities")
                                .content(ow.writeValueAsString(transferEligibilityCheckData))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(result -> assertNotNull(result.getResolvedException()));

    }

    private TransferEligibilityCheckData getTransferEligibilityCheckData() {
        TransferEligibilityCheckData transferEligibilityCheckData = new TransferEligibilityCheckData();
        transferEligibilityCheckData.setAcquirerCountry("USA");
        transferEligibilityCheckData.setAmount("6000");
        transferEligibilityCheckData.setCurrency("USD");
        transferEligibilityCheckData.setPaymentType("P2P");
        transferEligibilityCheckData.setRecipientAccountUri("pan:5102589999999913");
        transferEligibilityCheckData.setSenderAccountUri("pan:5102589999999921");
        transferEligibilityCheckData.setTransferAcceptorCountry("USA");
        transferEligibilityCheckData.setTransferAcceptorId("456487898368");
        transferEligibilityCheckData.setTransactionLocalDateTime("2021-06-17T18:05:02");

        AcquiringCredentials acquiringCredentials = new AcquiringCredentials();
        acquiringCredentials.setAcquiringCountry("USA");
        acquiringCredentials.setAcquiringIca("123456");

        DualMessage dualMessage = new DualMessage();
        dualMessage.setAcquiringBin("512340");
        dualMessage.setAcquirerReferenceId("512340");

        acquiringCredentials.setDualMessage(dualMessage);

        transferEligibilityCheckData.setAcquiringCredentials(acquiringCredentials);

        return transferEligibilityCheckData;
    }
}
