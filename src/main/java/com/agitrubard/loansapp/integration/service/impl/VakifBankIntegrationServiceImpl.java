package com.agitrubard.loansapp.integration.service.impl;

import com.agitrubard.loansapp.domain.model.converter.GetLoansPaymentPlanResponseConverter;
import com.agitrubard.loansapp.domain.model.request.LoansPaymentPlanRequest;
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

@Service
class VakifBankIntegrationServiceImpl implements VakifBankIntegrationService {

    private static final String DATA = "Data";
    private static final String LOAN = "Loan";

    @Override
    public GetLoansPaymentPlanResponse getLoansPaymentPlan(LoansPaymentPlanRequest loansPaymentPlanRequest) throws IOException {
        HttpEntity<?> entity = getLoanEntity(loansPaymentPlanRequest);

        RestTemplate restTemplate = new RestTemplate();
        final ResponseEntity<String> result = restTemplate.exchange(VakifBankConfiguration.LOAN_URL, HttpMethod.POST, entity, String.class);

        return getLoansPaymentPlanResponse(result);
    }

    private HttpEntity<?> getLoanEntity(LoansPaymentPlanRequest loansPaymentPlanRequest) throws IOException {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", Token.getToken(VakifBankConfiguration.TOKEN_URL, VakifBankConfiguration.CLIENT_ID, VakifBankConfiguration.CLIENT_SECRET));
        headers.add("Content-Type", "application/json");

        String parameters = "{    \"LoanProductId\": \"41001\",    " +
                "\"LoanTerm\": " + loansPaymentPlanRequest.getLoanTerm() + ",    " +
                "\"LoanAmount\":  " + loansPaymentPlanRequest.getLoanAmount() + ",    " +
                "\"GracePeriod\": 0,    " +
                "\"InstallmentPeriod\": 1}";
        byte[] body = parameters.getBytes();

        return new HttpEntity<>(body, headers);
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
}