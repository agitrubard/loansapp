package com.agitrubard.loansapp.integration.service.impl;

import com.agitrubard.loansapp.domain.model.converter.GetLoansPaymentPlanResponseConverter;
import com.agitrubard.loansapp.domain.model.request.LoansPaymentPlanRequest;
import com.agitrubard.loansapp.domain.model.response.GetLoansPaymentPlanResponse;
import com.agitrubard.loansapp.domain.model.enums.BankName;
import com.agitrubard.loansapp.integration.api.authorization.Token;
import com.agitrubard.loansapp.integration.api.config.YapiKrediConfiguration;
import com.agitrubard.loansapp.integration.service.YapiKrediIntegrationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
class YapiKrediIntegrationServiceImpl extends YapiKrediConfiguration implements YapiKrediIntegrationService {

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
        headers.add("Authorization", Token.getToken(getTOKEN_URL(), getCLIENT_ID(), getCLIENT_SECRET()));

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

    private GetLoansPaymentPlanResponse getLoansPaymentPlanResponse(ResponseEntity<String> result) throws JsonProcessingException {
        BankName bankName = BankName.YAPIKREDI;
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(result.getBody());
        double intRate = (root.path("response").path("return").path("intRate")).asDouble() * 100;
        double totalInterest = (root.path("response").path("return").path("totalInterest")).asDouble();
        double monthlyCostRate = (root.path("response").path("return").path("monthlyCostRate")).asDouble();
        double installmentAmount = (root.path("response").path("return").path("installmentList").findValue("installmentAmount")).asDouble();
        double totalPaymentAmount = (root.path("response").path("return").path("totalPaymentAmount")).asDouble();

        return GetLoansPaymentPlanResponseConverter.convert(bankName, intRate, totalInterest, monthlyCostRate, installmentAmount, totalPaymentAmount);
    }
}