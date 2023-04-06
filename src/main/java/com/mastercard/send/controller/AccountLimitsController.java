package com.mastercard.send.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.openapitools.client.ApiException;
import org.openapitools.client.api.AccountLimitsApi;
import org.openapitools.client.model.AccountLimitParent;
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
public class AccountLimitsController {

    private MastercardSendService mastercardSendService;

    @Autowired
    public void setMastercardSendService(MastercardSendService mastercardSendService) {
        this.mastercardSendService = mastercardSendService;
    }

    @PostMapping(value = "/{partnerId}/limits/daily-limits/account-limits")
    public Object addAccountLimit(@PathVariable String partnerId,
            @RequestBody AccountLimitParent accountLimitParent,
            @RequestHeader("Accept") String accept, @RequestHeader("Content-Type") String contentType)
            throws EncryptionException, GeneralSecurityException, IOException, ApiException {
        AccountLimitsApi api = getAccountsLimitsApi();

        //Executes Post Account Limits API, returns the response object of type AccountLimitDetail
        return api.postAccountLimit(partnerId, accountLimitParent, accept, contentType);
    }

    @PutMapping(value = "/{partnerId}/limits/account-limits/{accountLimitReference}")
    public Object updateAccountLimit(@PathVariable String partnerId,
            @PathVariable String accountLimitReference, @RequestBody AccountLimitParent accountLimitParent,
            @RequestHeader("Accept") String accept, @RequestHeader("Content-Type") String contentType)
            throws EncryptionException, GeneralSecurityException, IOException, ApiException {
        AccountLimitsApi api = getAccountsLimitsApi();


        //Executes Put Account Limits API, returns the response object of type AccountLimitDetail
        return api.putAccountLimit(partnerId, accountLimitReference, accountLimitParent, accept, contentType);
    }

    protected AccountLimitsApi getAccountsLimitsApi() throws GeneralSecurityException, EncryptionException, IOException {
        return new AccountLimitsApi(mastercardSendService.getApiClient());
    }
}
