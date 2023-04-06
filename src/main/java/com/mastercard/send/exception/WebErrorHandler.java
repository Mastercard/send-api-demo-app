package com.mastercard.send.exception;

import java.util.ArrayList;
import java.util.List;

import org.openapitools.client.ApiException;
import org.openapitools.client.model.Error;
import org.openapitools.client.model.ErrorResponse;
import org.openapitools.client.model.ErrorResponseErrors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@ControllerAdvice
public class WebErrorHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebErrorHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        if (ex instanceof ApiException) {
            ApiException apiException = (ApiException) ex;
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            ErrorResponse error = gson.fromJson(apiException.getResponseBody(), ErrorResponse.class);
            String errorJson = gson.toJson(error);
            LOGGER.error(errorJson);

            return new ResponseEntity<>(error, HttpStatus.valueOf(apiException.getCode()));
        } else {
            return new ResponseEntity<>(createErrorResponse(getErrorErrorsError(ex)), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler(ConfigurationException.class)
    public ResponseEntity<ErrorResponse> handleExceptionConfiguration(Exception ex) {
        return new ResponseEntity<>(createErrorResponse(getConfigurationError(ex)), HttpStatus.FORBIDDEN);
    }

    private ErrorResponse createErrorResponse(Error standardError){
        ErrorResponseErrors standardErrorHeader = getErrorErrors(standardError);
        ErrorResponse error = new ErrorResponse();
        error.setErrors(standardErrorHeader);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String errorJson = gson.toJson(error);
        LOGGER.error(errorJson);
        return error;
    }

    private ErrorResponseErrors getErrorErrors(Error standardError) {
        List<Error> errorList = new ArrayList<>();
        errorList.add(standardError);
        ErrorResponseErrors standardErrorHeader = new ErrorResponseErrors();
        standardErrorHeader.error(errorList);
        return standardErrorHeader;
    }

    private Error getErrorErrorsError(Exception ex) {
        Error standardError = new Error();
        standardError.setSource("SEND_API_DEMO_APP");
        standardError.setReasonCode("UNKNOWN");
        standardError.setDescription(ex.getMessage());
        standardError.setRecoverable(false);
        return standardError;
    }

    private Error getConfigurationError(Exception ex) {
        Error standardError = new Error();
        standardError.setSource("SEND_API_DEMO_APP");
        standardError.setReasonCode("CONFIGURATION_MISSING");
        standardError.setDescription(ex.getMessage());
        standardError.setRecoverable(false);
        return standardError;
    }
}
