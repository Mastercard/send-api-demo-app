package com.mastercard.send.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mastercard.send.exception.ConfigurationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.AccountLimitsApi;
import org.openapitools.client.model.AccountLimitDetail;
import org.openapitools.client.model.AccountLimitParent;
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
class AccountLimitsControllerTest {


    private AccountLimitsController accountLimitsController;

    @Mock
    private MastercardSendService MastercardSendService;

    @Mock
    private AccountLimitsApi mockAccountLimitsApi;

    private MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    ObjectWriter ow;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() throws Exception {
        closeable = MockitoAnnotations.openMocks(this);
        accountLimitsController = spy(new AccountLimitsController());
        accountLimitsController.setMastercardSendService(MastercardSendService);
        doReturn(mockAccountLimitsApi).when(accountLimitsController).getAccountsLimitsApi();
        mockMvc = MockMvcBuilders.standaloneSetup(accountLimitsController).setControllerAdvice(new WebErrorHandler())
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
        AccountLimitParent accountLimitParent = getAccountLimitRequest();
        when(mockAccountLimitsApi.postAccountLimit("ptnr_1lzs0zD6uWo7k-OdUXjLW_pFgX2", accountLimitParent, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE )).thenReturn(new AccountLimitDetail());
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/ptnr_1lzs0zD6uWo7k-OdUXjLW_pFgX2/limits/daily-limits/account-limits")
                        .content(ow.writeValueAsString(accountLimitParent))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void should_updateAccountLimit() throws Exception {
        AccountLimitParent accountLimitParent = getAccountLimitRequest();
        when(mockAccountLimitsApi.putAccountLimit("ptnr_1lzs0zD6uWo7k-OdUXjLW_pFgX2", "77bfeafa1b2f4ce2a4323006cb2c6b8003bfc086", accountLimitParent, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE )).thenReturn(new AccountLimitDetail());
        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/ptnr_1lzs0zD6uWo7k-OdUXjLW_pFgX2/limits/account-limits/77bfeafa1b2f4ce2a4323006cb2c6b8003bfc086")
                        .content(ow.writeValueAsString(getAccountLimitRequest()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void should_throwApiException() throws Exception {
        AccountLimitParent accountLimitParent = getAccountLimitRequest();
        when(mockAccountLimitsApi.putAccountLimit("ptnr_1lzs0zD6uWo7k-OdUXjLW_pFgX2", "REF_0001", accountLimitParent, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE )).thenThrow(new ApiException(400, "Bad Request"));
        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/ptnr_1lzs0zD6uWo7k-OdUXjLW_pFgX2/limits/account-limits/REF_0001")
                        .content(ow.writeValueAsString(accountLimitParent))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ApiException));
    }

    @Test
    void should_throwConfigurationException() throws Exception {
        AccountLimitParent accountLimitParent = getAccountLimitRequest();
        when(mockAccountLimitsApi.putAccountLimit("ptnr_1lzs0zD6uWo7k-OdUXjLW_pFgX2", "REF_0001", accountLimitParent, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE )).thenThrow(new ConfigurationException("Authentication section of the application.properties file is not updated"));
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/ptnr_1lzs0zD6uWo7k-OdUXjLW_pFgX2/limits/account-limits/REF_0001")
                                .content(ow.writeValueAsString(accountLimitParent))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .header("Accept", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ConfigurationException));
    }

    @Test
    void should_throwException() throws Exception {
        when(accountLimitsController.getAccountsLimitsApi()).thenCallRealMethod();
        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/ptnr_1lzs0zD6uWo7k-OdUXjLW_pFgX2/limits/account-limits/REF_0001")
                        .content(ow.writeValueAsString(getAccountLimitRequest()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                        .header("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(result -> assertNotNull(result.getResolvedException()));
    }

    private AccountLimitParent getAccountLimitRequest() {
        AccountLimitParent accountLimitParent = new AccountLimitParent();
        accountLimitParent.setTransferAcceptorId("456487898368123");
        accountLimitParent.setNetwork("MASTERCARD");
        accountLimitParent.setTransactionType("PAY");
        accountLimitParent.setPaymentType("P2P");
        accountLimitParent.setCardProductType("CNSMR");
        accountLimitParent.setCardType("DEBIT");
        accountLimitParent.setMessageSystem("S");
        accountLimitParent.setMinTransactionAmount(1.00);
        accountLimitParent.setMaxTransactionAmount(250.00);
        accountLimitParent.setDailyMaxAmount(6000.00);
        accountLimitParent.setWeeklyMaxAmount(10000.00);
        accountLimitParent.setMonthlyMaxAmount(25000.00);
        accountLimitParent.setDailyMaxVolume(5000);
        accountLimitParent.setWeeklyMaxVolume(8000);
        accountLimitParent.setMonthlyMaxVolume(20000);
        accountLimitParent.setLimitEnabled(true);

        return accountLimitParent;
    }
}
