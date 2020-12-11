package com.agitrubard.loansapp.integration.service.impl;

import com.agitrubard.loansapp.domain.model.converter.GetCurrencyRatesResponseConverter;
import com.agitrubard.loansapp.domain.model.converter.GetLoansPaymentPlanResponseConverter;
import com.agitrubard.loansapp.domain.model.request.LoansPaymentPlanRequest;
import com.agitrubard.loansapp.domain.model.response.GetCurrencyRatesResponse;
import com.agitrubard.loansapp.domain.model.response.GetLoansPaymentPlanResponse;
import com.agitrubard.loansapp.domain.model.enums.BankName;
import com.agitrubard.loansapp.integration.api.authorization.Token;
import com.agitrubard.loansapp.integration.api.config.VakifBankConfiguration;
import com.agitrubard.loansapp.integration.service.VakifBankIntegrationService;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
class VakifBankIntegrationServiceImpl implements VakifBankIntegrationService {

    private static final String DATA = "Data";
    private static final String LOAN = "Loan";
    private static final String CURRENCY = "Currency";

    @Override
    public GetLoansPaymentPlanResponse getLoansPaymentPlan(LoansPaymentPlanRequest loansPaymentPlanRequest) throws IOException {
        HttpEntity<?> entity = getLoanEntity(loansPaymentPlanRequest);

        RestTemplate restTemplate = new RestTemplate();
        final ResponseEntity<String> result = restTemplate.exchange(VakifBankConfiguration.LOAN_URL, HttpMethod.POST, entity, String.class);

        return getLoansPaymentPlanResponse(result);
    }

    @Override
    public List<GetCurrencyRatesResponse> getCurrencyRates() throws IOException {
        HttpEntity<?> entity = getCurrencyRatesEntity();

        RestTemplate restTemplate = new RestTemplate();
        final ResponseEntity<String> result = restTemplate.exchange(VakifBankConfiguration.CURRENCY_RATES_URL, HttpMethod.POST, entity, String.class);

        return getCurrencyRatesResponse(result);
    }

    private MultiValueMap<String, String> getHeaders() throws IOException {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", Token.getToken(VakifBankConfiguration.TOKEN_URL, VakifBankConfiguration.CLIENT_ID, VakifBankConfiguration.CLIENT_SECRET));
        return headers;
    }

    private HttpEntity<?> getLoanEntity(LoansPaymentPlanRequest loansPaymentPlanRequest) throws IOException {
        String parameters = "{    \"LoanProductId\": \"41001\",    " +
                "\"LoanTerm\": " + loansPaymentPlanRequest.getLoanTerm() + ",    " +
                "\"LoanAmount\":  " + loansPaymentPlanRequest.getLoanAmount() + ",    " +
                "\"GracePeriod\": 0,    " +
                "\"InstallmentPeriod\": 1}";
        byte[] body = parameters.getBytes();

        return new HttpEntity<>(body, getHeaders());
    }

    private HttpEntity<?> getCurrencyRatesEntity() throws IOException {
        String parameters = "{    \"ValidityDate\": \"" + getLocalDateTime() + "\"}";
        byte[] body = parameters.getBytes();

        return new HttpEntity<>(body, getHeaders());
    }

    private GetLoansPaymentPlanResponse getLoansPaymentPlanResponse(ResponseEntity<String> result) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(result.getBody());
        double intRate = (root.path(DATA).path(LOAN).findValue("InterestRate")).asDouble();
        double loanTerm = (root.path(DATA).path(LOAN).findValue("LoanTerm")).asDouble();
        double totalAmount = (root.path(DATA).path(LOAN).findValue("TotalAmount")).asDouble();
        double installmentAmount = (root.path(DATA).path(LOAN).findValue("InstallmentAmount")).asDouble();
        double totalPaymentAmount = (installmentAmount * loanTerm);
        double totalInterest = totalPaymentAmount - totalAmount;
        double monthlyCostRate = ((installmentAmount - (totalAmount / loanTerm)) / totalInterest) * 100;

        return GetLoansPaymentPlanResponseConverter.convert(BankName.VAKIFBANK, intRate, totalInterest, monthlyCostRate, installmentAmount, totalPaymentAmount);
    }

    private List<GetCurrencyRatesResponse> getCurrencyRatesResponse(ResponseEntity<String> result) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(result.getBody());
        JsonNode currency = root.path(DATA).path(CURRENCY);

        List<JsonNode> minorCurrencies = currency.findValues("CurrencyCode");
        List<JsonNode> saleRates = currency.findValues("SaleRate");
        List<JsonNode> purchaseRates = currency.findValues("PurchaseRate");

        return getCurrencyRatesReponseList(minorCurrencies, saleRates, purchaseRates);
    }

    private List<GetCurrencyRatesResponse> getCurrencyRatesReponseList(List<JsonNode> minorCurrencies, List<JsonNode> saleRates, List<JsonNode> purchaseRates) {
        List<GetCurrencyRatesResponse> getCurrencyRatesResponseList = new ArrayList<>();

        for (int i = 0; i < minorCurrencies.size(); i++) {
            String currencyName = minorCurrencies.get(i).asText();
            double sellRate = saleRates.get(i).asDouble();
            double buyRate = purchaseRates.get(i).asDouble();
            double averageRate = ((sellRate + buyRate) / 2.0);

            getCurrencyRatesResponseList.add(GetCurrencyRatesResponseConverter.convert(currencyName, sellRate, buyRate, averageRate));
        }
        return getCurrencyRatesResponseList;
    }

    private String getLocalDateTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", new Locale("tr", "TR"));
        return simpleDateFormat.format(new Date());
    }
}