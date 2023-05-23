package com.wayapay.xerointegration.controller;

import com.google.gson.Gson;
import com.wayapay.xerointegration.dto.waya.request.WayaTransactionRequest;
import com.wayapay.xerointegration.dto.waya.response.ValidationPayload;
import com.wayapay.xerointegration.dto.waya.response.WayaTransactionResponse;
import com.wayapay.xerointegration.dto.xero.request.XeroBankTransactionRequestPayload;
import com.wayapay.xerointegration.dto.xero.response.XeroBankTransactionResponsePayload;
import com.wayapay.xerointegration.dto.xero.response.XeroSingleBankTransactionResponsePayload;
import com.wayapay.xerointegration.service.XeroIntegrationService;
import com.wayapay.xerointegration.validation.ModelValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/v1")
public class XeroIntegrationRestController
{

    @Autowired
    private XeroIntegrationService xeroIntegrationService;

    @Autowired
    private ModelValidator modelValidator;


    private static final Gson JSON = new Gson();


    @GetMapping("/transaction")
    public ResponseEntity<XeroSingleBankTransactionResponsePayload> handleGetBankTransactionRequest(@RequestParam("bankTransactionId") String bankTransactionId){
        XeroSingleBankTransactionResponsePayload responsePayload = xeroIntegrationService.getSingleBankTransaction(bankTransactionId);
        return ResponseEntity.ok(responsePayload);
    }

    @PostMapping("/transactions")
    public ResponseEntity<XeroBankTransactionResponsePayload> handleGetBankTransactionRequest(@RequestBody XeroBankTransactionRequestPayload requestPayload){
        ValidationPayload validationPayload = modelValidator.doModelValidation(requestPayload);
        if(validationPayload.isHasError()){
            return ResponseEntity.badRequest().body(JSON.fromJson(validationPayload.getErrorJson(), XeroBankTransactionResponsePayload.class));
        }
        XeroBankTransactionResponsePayload responsePayload = xeroIntegrationService.getTransactions(requestPayload);
        return ResponseEntity.ok(responsePayload);
    }

    @PostMapping("/upload")
    public ResponseEntity<WayaTransactionResponse> uploadTransaction(@RequestBody WayaTransactionRequest wayaTransactionRequest) throws URISyntaxException, IOException {
        WayaTransactionResponse response =  xeroIntegrationService.getTransactionFromWaya(wayaTransactionRequest);
        return ResponseEntity.ok(response);
    }
}
