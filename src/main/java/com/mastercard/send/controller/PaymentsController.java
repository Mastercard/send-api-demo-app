package com.mastercard.send.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.openapitools.client.ApiException;
import org.openapitools.client.api.PaymentsApi;
import org.openapitools.client.api.PaymentsSearchesApi;
import org.openapitools.client.model.PaymentCreateData;
import org.openapitools.client.model.PaymentSearchData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mastercard.developer.encryption.EncryptionException;
import com.mastercard.send.service.MastercardSendService;

@RestController
public class PaymentsController {

    @Value("${partnerId}")
    private String partnerId;

    private MastercardSendService mastercardSendService;

    @Autowired
    public void setMastercardSendService(MastercardSendService mastercardSendService) {
        this.mastercardSendService = mastercardSendService;
    }

    @PostMapping(value = "/payments")
    public Object payments(@RequestBody PaymentCreateData paymentCreateData)
            throws EncryptionException, GeneralSecurityException, IOException, ApiException {
        PaymentsApi api = getPaymentsApi();

        return api.createPaymentUsingPOST(partnerId, paymentCreateData, false, MediaType.APPLICATION_JSON_VALUE,
                MediaType.APPLICATION_JSON_VALUE);
    }

    @PostMapping(value = "/payments/searches")
    public Object paymentsSearches(@RequestBody PaymentSearchData paymentSearchData)
            throws EncryptionException, GeneralSecurityException, IOException, ApiException {
        PaymentsSearchesApi api = getPaymentSearchesApi();

        return api.retrievePaymentInfoUsingPOST(partnerId, paymentSearchData, MediaType.APPLICATION_JSON_VALUE,
                MediaType.APPLICATION_JSON_VALUE);
    }

    protected PaymentsApi getPaymentsApi() throws GeneralSecurityException, EncryptionException, IOException {
        return new PaymentsApi(mastercardSendService.getApiClient());
    }

    protected  PaymentsSearchesApi getPaymentSearchesApi() throws GeneralSecurityException, EncryptionException, IOException {
        return new PaymentsSearchesApi(mastercardSendService.getApiClient());
    }
}
