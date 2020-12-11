package com.agitrubard.loansapp.integration.service.impl;

import com.agitrubard.loansapp.domain.model.converter.GetCurrencyRatesResponseConverter;
import com.agitrubard.loansapp.domain.model.converter.GetLoansPaymentPlanResponseConverter;
import com.agitrubard.loansapp.domain.model.request.LoansPaymentPlanRequest;
import com.agitrubard.loansapp.domain.model.response.GetCurrencyRatesResponse;
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
import java.util.ArrayList;
import java.util.List;

@Service
class YapiKrediIntegrationServiceImpl implements YapiKrediIntegrationService {

    private static final String RESPONSE = "response";
    private static final String RETURN = "return";

    @Override
    public GetLoansPaymentPlanResponse getLoansPaymentPlan(LoansPaymentPlanRequest loansPaymentPlanRequest) throws IOException {
        HttpEntity<?> entity = getLoanEntity(loansPaymentPlanRequest);

        RestTemplate restTemplate = new RestTemplate();
        final ResponseEntity<String> result = restTemplate.exchange(YapiKrediConfiguration.LOAN_URL, HttpMethod.POST, entity, String.class);

        return getLoansPaymentPlanResponse(result);
    }

    @Override
    public List<GetCurrencyRatesResponse> getCurrencyRates() throws IOException {
        HttpEntity<?> entity = getCurrencyRatesEntity();

        RestTemplate restTemplate = new RestTemplate();
        final ResponseEntity<String> result = restTemplate.exchange(YapiKrediConfiguration.CURRENCY_RATES_URL, HttpMethod.GET, entity, String.class);

        return getCurrencyRatesResponse(result);
    }

    private MultiValueMap<String, String> getHeaders() throws IOException {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", Token.getToken(YapiKrediConfiguration.TOKEN_URL, YapiKrediConfiguration.CLIENT_ID, YapiKrediConfiguration.CLIENT_SECRET));
        return headers;
    }

    private HttpEntity<?> getLoanEntity(LoansPaymentPlanRequest loansPaymentPlanRequest) throws IOException {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        String nop = String.valueOf(loansPaymentPlanRequest.getLoanTerm());
        String principal = String.valueOf(loansPaymentPlanRequest.getLoanAmount());

        body.add("branchCode", "925");
        body.add("channelCode", "OPN");
        body.add("categoryCode", "W9");
        body.add("clientType", "1");
        body.add("nop", nop);
        body.add("principal", principal);

        return new HttpEntity<>(body, getHeaders());
    }

    private HttpEntity<?> getCurrencyRatesEntity() throws IOException {
        return new HttpEntity<>(getHeaders());
    }

    private GetLoansPaymentPlanResponse getLoansPaymentPlanResponse(ResponseEntity<String> result) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(result.getBody());
        double intRate = (root.path(RESPONSE).path(RETURN).path("intRate")).asDouble() * 100;
        double totalInterest = (root.path(RESPONSE).path(RETURN).path("totalInterest")).asDouble();
        double monthlyCostRate = (root.path(RESPONSE).path(RETURN).path("monthlyCostRate")).asDouble();
        double installmentAmount = (root.path(RESPONSE).path(RETURN).path("installmentList").findValue("installmentAmount")).asDouble();
        double totalPaymentAmount = (root.path(RESPONSE).path(RETURN).path("totalPaymentAmount")).asDouble();

        return GetLoansPaymentPlanResponseConverter.convert(BankName.YAPIKREDI, intRate, totalInterest, monthlyCostRate, installmentAmount, totalPaymentAmount);
    }

    private List<GetCurrencyRatesResponse> getCurrencyRatesResponse(ResponseEntity<String> result) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(result.getBody());
        JsonNode exchangeRateList = root.path(RESPONSE).path("exchangeRateList");

        List<JsonNode> minorCurrencies = exchangeRateList.findValues("minorCurrency");
        List<JsonNode> majorCurrencies = exchangeRateList.findValues("majorCurrency");
        List<JsonNode> sellRates = exchangeRateList.findValues("sellRate");
        List<JsonNode> buyRates = exchangeRateList.findValues("buyRate");
        List<JsonNode> averageRates = exchangeRateList.findValues("averageRate");

        return getCurrencyRatesReponseList(minorCurrencies, majorCurrencies, sellRates, buyRates, averageRates);
    }

    private List<GetCurrencyRatesResponse> getCurrencyRatesReponseList(List<JsonNode> minorCurrencies, List<JsonNode> majorCurrencies, List<JsonNode> sellRates, List<JsonNode> buyRates, List<JsonNode> averageRates) {
        List<GetCurrencyRatesResponse> getCurrencyRatesResponses = new ArrayList<>();

        for (int i = 0; i < minorCurrencies.size(); i++) {
            String minorCurrency = minorCurrencies.get(i).asText();

            if (minorCurrency.equals("TL")) {
                String currencyName = majorCurrencies.get(i).asText();
                double sellRate = sellRates.get(i).asDouble();
                double buyRate = buyRates.get(i).asDouble();
                double averageRate = averageRates.get(i).asDouble();
                getCurrencyRatesResponses.add(GetCurrencyRatesResponseConverter.convert(currencyName, sellRate, buyRate, averageRate));
            }
        }
        return getCurrencyRatesResponses;
    }
}