package com.mastercard.send.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.openapitools.client.ApiException;
import org.openapitools.client.api.TransferEligibilityApi;
import org.openapitools.client.model.TransferEligibilityCheckData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mastercard.developer.encryption.EncryptionException;
import com.mastercard.send.service.MastercardSendService;

@RestController
public class TransferEligibilityController {

    @Value("${partnerId}")
    private String partnerId;

    private MastercardSendService mastercardSendService;

    @Autowired
    public void setMastercardSendService(MastercardSendService mastercardSendService) {
        this.mastercardSendService = mastercardSendService;
    }

    @PostMapping(value = "/transfer-eligibilities")
    public Object checkTransferEligibility(@RequestBody TransferEligibilityCheckData transferEligibilityRequest)
            throws ApiException, EncryptionException, GeneralSecurityException, IOException {
        TransferEligibilityApi api = getTransferEligibilityApi();

        return api.checkTransferEligibilityUsingPOST(partnerId, transferEligibilityRequest,
                MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE);
    }

    protected TransferEligibilityApi getTransferEligibilityApi() throws GeneralSecurityException, EncryptionException, IOException {
         return new TransferEligibilityApi(mastercardSendService.getApiClient());
    }
}
