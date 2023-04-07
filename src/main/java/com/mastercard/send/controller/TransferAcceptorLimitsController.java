package com.mastercard.send.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.openapitools.client.ApiException;
import org.openapitools.client.api.TransferAcceptorLimitsApi;
import org.openapitools.client.model.TransferAcceptorLimitPostDetailParent;
import org.openapitools.client.model.TransferAcceptorLimitPutDetailParent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.mastercard.developer.encryption.EncryptionException;
import com.mastercard.send.service.MastercardSendService;

@RestController
public class TransferAcceptorLimitsController {

    private MastercardSendService mastercardSendService;

    @Autowired
    public void setMastercardSendService(MastercardSendService mastercardSendService) {
        this.mastercardSendService = mastercardSendService;
    }

    @PostMapping(value = "/{partnerId}/limits/daily-limits/transfer-acceptor-limits")
    public Object addTransferAcceptorLimit(@PathVariable String partnerId,
            @RequestBody TransferAcceptorLimitPostDetailParent transferAcceptorLimitPostDetailParent,
            @RequestHeader("Accept") String accept, @RequestHeader("Content-Type") String contentType)
            throws EncryptionException, GeneralSecurityException, IOException, ApiException {
        TransferAcceptorLimitsApi api = getTransferAcceptorLimitsApi();

        //Executes Post Transfer Acceptor Limits API, returns the response object of type TransferAcceptorLimitParent
        return api.postTransferAcceptorLimit(partnerId, transferAcceptorLimitPostDetailParent, accept, contentType);
    }

    @PutMapping(value = "/{partnerId}/limits/transfer-acceptor-limits/{transferAcceptorLimitReference}")
    public Object updateTransferAcceptorLimit(@PathVariable String partnerId,
            @PathVariable String transferAcceptorLimitReference,
            @RequestBody TransferAcceptorLimitPutDetailParent transferAcceptorLimitPutDetailParent,
            @RequestHeader("Accept") String accept, @RequestHeader("Content-Type") String contentType)
            throws EncryptionException, GeneralSecurityException, IOException, ApiException {
        TransferAcceptorLimitsApi api = getTransferAcceptorLimitsApi();

        //Executes Put Transfer Acceptor Limits API, returns the response object of type TransferAcceptorLimitParent
        return api.putTransferAcceptorLimit(partnerId, transferAcceptorLimitReference,
                transferAcceptorLimitPutDetailParent, accept, contentType);
    }

    protected TransferAcceptorLimitsApi getTransferAcceptorLimitsApi() throws GeneralSecurityException, EncryptionException, IOException {
        return new TransferAcceptorLimitsApi(mastercardSendService.getApiClient());
    }
}
