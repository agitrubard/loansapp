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
class VakifBankIntegrationServiceImpl extends VakifBankConfiguration implements VakifBankIntegrationService {

    @Override
    public GetLoansPaymentPlanResponse getLoansPaymentPlan(LoansPaymentPlanRequest loansPaymentPlanRequest) throws IOException {
        HttpEntity<?> entity = getLoanEntity(loansPaymentPlanRequest);

        RestTemplate restTemplate = new RestTemplate();
        final ResponseEntity<String> result = restTemplate.exchange(getLOAN_URL(), HttpMethod.POST, entity, String.class);

        return getLoansPaymentPlanResponse(result);
    }

    private HttpEntity<?> getLoanEntity(LoansPaymentPlanRequest loansPaymentPlanRequest) throws IOException {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        headers.add("Authorization", Token.getToken(getTOKEN_URL(), getCLIENT_ID(), getCLIENT_SECRET()));
        headers.add("Content-Type", "application/json");

        /*
        MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
        String loanTerm = String.valueOf(loansPaymentPlanRequest.getLoanTerm());
        String loanAmount = String.valueOf(loansPaymentPlanRequest.getLoanAmount());

        body.add("LoanProductId", "35009");
        body.add("LoanTerm", loanTerm);
        body.add("LoanAmount", loanAmount);
        body.add("GracePeriod", "0");
        body.add("InstallmentPeriod", "1");
        */

        String parameters = "{    \"LoanProductId\": \"35009\",    " +
                "\"LoanTerm\": " + loansPaymentPlanRequest.getLoanTerm() + ",    " +
                "\"LoanAmount\":  " + loansPaymentPlanRequest.getLoanAmount() + ",    " +
                "\"GracePeriod\": 0,    " +
                "\"InstallmentPeriod\": 1}";
        byte[] body = parameters.getBytes();

        return new HttpEntity<Object>(body, headers);
    }

    private GetLoansPaymentPlanResponse getLoansPaymentPlanResponse(ResponseEntity<String> result) throws JsonProcessingException {
        BankName bankName = BankName.VAKIFBANK;
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(result.getBody());
        double intRate = (root.path("Data").path("Loan").findValue("InterestRate")).asDouble();
        double loanTerm = (root.path("Data").path("Loan").findValue("LoanTerm")).asDouble();
        double totalAmount = (root.path("Data").path("Loan").findValue("TotalAmount")).asDouble();
        double installmentAmount = (root.path("Data").path("Loan").findValue("InstallmentAmount")).asDouble();
        double totalPaymentAmount = (installmentAmount * loanTerm);
        double totalInterest = totalPaymentAmount - totalAmount;
        double monthlyCostRate = ((installmentAmount - (totalAmount / loanTerm)) / totalInterest) * 100;

        return GetLoansPaymentPlanResponseConverter.convert(bankName, intRate, totalInterest, monthlyCostRate, installmentAmount, totalPaymentAmount);
    }
}