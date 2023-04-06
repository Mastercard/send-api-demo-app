package com.mastercard.send.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.openapitools.client.ApiException;
import org.openapitools.client.api.PartnerLimitsApi;
import org.openapitools.client.model.PartnerLimitPutDetailParent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.mastercard.developer.encryption.EncryptionException;
import com.mastercard.send.service.MastercardSendService;

@RestController
public class PartnerLimitsController {

    private MastercardSendService mastercardSendService;

    @Autowired
    public void setMastercardSendService(MastercardSendService mastercardSendService) {
        this.mastercardSendService = mastercardSendService;
    }

    @GetMapping(value = "/{partnerId}/limits")
    public Object getPartnerLimit(@PathVariable String partnerId, @RequestHeader("Accept") String accept)
            throws EncryptionException, GeneralSecurityException, IOException, ApiException {
        PartnerLimitsApi api = getPartnerLimitsApi();


        //Executes Get Partner Limits API, returns the response object of type List<PartnerLimitDetailsWithSpend>
        return api.getPartnerLimit(partnerId, accept);
    }

    @PutMapping(value = "/{partnerId}/limits/daily-limits/{partnerLimitReference}")
    public Object updatePartnerLimit(@PathVariable String partnerId, @PathVariable String partnerLimitReference,
            @RequestBody PartnerLimitPutDetailParent partnerLimitPutDetailParent,
            @RequestHeader("Accept") String accept, @RequestHeader("Content-Type") String contentType)
            throws EncryptionException, GeneralSecurityException, IOException, ApiException {
        PartnerLimitsApi api = getPartnerLimitsApi();


        //Executes Put Partner Limits API, returns the response object of type TransactionInitiatorLimitParent
        return api.putPartnerLimit(partnerId, partnerLimitReference, partnerLimitPutDetailParent, accept, contentType);
    }
    
    protected PartnerLimitsApi getPartnerLimitsApi() throws GeneralSecurityException, EncryptionException, IOException {
        return new PartnerLimitsApi(mastercardSendService.getApiClient());
    }
}
