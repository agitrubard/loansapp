package com.agitrubard.loansapp.integration.service.impl;

import com.agitrubard.loansapp.apiconfig.YapiKrediApiConfiguration;
import com.agitrubard.loansapp.domain.controller.request.LoansPaymentPlanRequest;
import com.agitrubard.loansapp.domain.controller.response.GetLoansPaymentPlanResponse;
import com.agitrubard.loansapp.domain.model.enums.BankName;
import com.agitrubard.loansapp.integration.service.YapiKrediIntegrationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
class YapiKrediIntegrationServiceImpl extends YapiKrediApiConfiguration implements YapiKrediIntegrationService {

    @Override
    public GetLoansPaymentPlanResponse getLoansPaymentPlan(LoansPaymentPlanRequest loansPaymentPlanRequest) throws IOException {
        HttpEntity<?> entity = getLoanEntity(loansPaymentPlanRequest);

        RestTemplate restTemplate = new RestTemplate();
        final ResponseEntity<String> result = restTemplate.exchange(getLOAN_URL(), HttpMethod.POST, entity, String.class);

        return getLoansPaymentPlanResponse(result);
    }

    private HttpEntity<?> getLoanEntity(LoansPaymentPlanRequest loansPaymentPlanRequest) throws IOException {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", getToken());

        MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
        String nop = String.valueOf(loansPaymentPlanRequest.getLoanTerm());
        String principal = String.valueOf(loansPaymentPlanRequest.getLoanAmount());

        body.add("branchCode", "925");
        body.add("channelCode", "OPN");
        body.add("categoryCode", "W9");
        body.add("clientType", "1");
        body.add("nop", nop);
        body.add("principal", principal);

        return new HttpEntity<Object>(body, headers);
    }

    private String getToken() throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> result;
        result = restTemplate.exchange(getTOKEN_URL(), HttpMethod.POST, getTokenEntity(), String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(result.getBody());
        JsonNode accessToken = root.path("access_token");
        JsonNode tokenType = root.path("token_type");

        return tokenType.asText() + " " + accessToken.asText();
    }

    private HttpEntity<?> getTokenEntity() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        headers.add("CONTENT_TYPE", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.add("Accept", "application/json");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
        body.add("scope", "oob");
        body.add("grant_type", "client_credentials");
        body.add("client_id", getCLIENT_ID());
        body.add("client_secret", getCLIENT_SECRET());

        return new HttpEntity<Object>(body, headers);
    }

    private GetLoansPaymentPlanResponse getLoansPaymentPlanResponse(ResponseEntity<String> result) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(result.getBody());
        JsonNode intRate = root.path("response").path("return").path("intRate");
        JsonNode totalInterest = root.path("response").path("return").path("totalInterest");
        JsonNode monthlyCostRate = root.path("response").path("return").path("monthlyCostRate");
        JsonNode installmentAmount = root.path("response").path("return").path("installmentList").findValue("installmentAmount");
        JsonNode totalPaymentAmount = root.path("response").path("return").path("totalPaymentAmount");

        GetLoansPaymentPlanResponse getLoansPaymentPlanResponse = new GetLoansPaymentPlanResponse();
        getLoansPaymentPlanResponse.setBankName(BankName.YAPIKREDI);
        getLoansPaymentPlanResponse.setIntRate(intRate.asDouble() * 100);
        getLoansPaymentPlanResponse.setTotalInterest(totalInterest.asDouble());
        getLoansPaymentPlanResponse.setMonthlyCostRate(monthlyCostRate.asDouble());
        getLoansPaymentPlanResponse.setInstallmentAmount(installmentAmount.asDouble());
        getLoansPaymentPlanResponse.setTotalPaymentAmount(totalPaymentAmount.asDouble());

        return getLoansPaymentPlanResponse;
    }
}