package com.agitrubard.loansapp.integration.service.impl;

import com.agitrubard.loansapp.domain.model.converter.GetLoanPaymentPlanResponseConverter;
import com.agitrubard.loansapp.domain.model.exception.LoanPaymentPlanResponseException;
import com.agitrubard.loansapp.domain.model.exception.TokenException;
import com.agitrubard.loansapp.domain.model.request.LoanPaymentPlanRequest;
import com.agitrubard.loansapp.domain.model.response.GetCurrencyRatesResponse;
import com.agitrubard.loansapp.domain.model.response.GetLoanPaymentPlanResponse;
import com.agitrubard.loansapp.domain.model.enums.BankName;
import com.agitrubard.loansapp.domain.model.response.constant.CustomConstant;
import com.agitrubard.loansapp.domain.model.response.constant.VakifBankConstant;
import com.agitrubard.loansapp.domain.model.response.external.vakifbank.VakifBankBaseResponse;
import com.agitrubard.loansapp.integration.api.authorization.Token;
import com.agitrubard.loansapp.integration.api.config.VakifBankConfiguration;
import com.agitrubard.loansapp.integration.service.VakifBankIntegrationService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
public class VakifBankIntegrationServiceImpl implements VakifBankIntegrationService {
    private final VakifBankConfiguration vakifBankConfiguration;
    private final RestTemplate restTemplate;

    public VakifBankIntegrationServiceImpl(VakifBankConfiguration vakifBankConfiguration, RestTemplate restTemplate) {
        this.vakifBankConfiguration = vakifBankConfiguration;
        this.restTemplate = restTemplate;
    }

    @Override
    public GetLoanPaymentPlanResponse getLoanPaymentPlan(LoanPaymentPlanRequest loanPaymentPlanRequest) throws TokenException, LoanPaymentPlanResponseException {
        log.info("VakifBank Loan Payment Plan Call Starting");

        HttpEntity<?> entity = createLoanEntity(loanPaymentPlanRequest);
        final ResponseEntity<String> result = restTemplate.exchange(vakifBankConfiguration.getLoanUrl(), HttpMethod.POST, entity, String.class);

        return getLoansPaymentPlanResponse(result);
    }

    @Override
    public List<GetCurrencyRatesResponse> getCurrencyRates() throws TokenException {
        log.info("VakifBank Currency Rates Call Starting");

        HttpEntity<?> entity = createCurrencyRatesEntity();
        final ResponseEntity<VakifBankBaseResponse> result = restTemplate.exchange(vakifBankConfiguration.getCurrencyRatesUrl(), HttpMethod.POST, entity, VakifBankBaseResponse.class);
        VakifBankBaseResponse vakifBankBaseResponse = result.getBody();

        return Optional.ofNullable(getCurrencyRatesResponse(vakifBankBaseResponse)).orElse(new ArrayList<>());
    }

    private MultiValueMap<String, String> getHeaders() throws TokenException {
        log.info("VakifBank Headers Call Starting");

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(CustomConstant.CONTENT_TYPE, CustomConstant.CONTENT_TYPE_APPLICATION_JSON);
        headers.add(CustomConstant.AUTHORIZATION, Token.getAccessToken(vakifBankConfiguration.getTokenUrl(), vakifBankConfiguration.getClientId(), vakifBankConfiguration.getClientSecret()));

        return headers;
    }

    private HttpEntity<?> createLoanEntity(LoanPaymentPlanRequest loanPaymentPlanRequest) throws TokenException {
        log.info("VakifBank Loan Entity Call Starting");

        String parameters = "{    \"" + VakifBankConstant.LOAN_PRODUCT_ID + "\": \"" + VakifBankConstant.LOAN_PRODUCT_ID_VALUE_41001 + "\"" + VakifBankConstant.COMMA +
                "\"" + VakifBankConstant.LOAN_TERM + "\": " + loanPaymentPlanRequest.getLoanTerm() + VakifBankConstant.COMMA +
                "\"" + VakifBankConstant.LOAN_AMOUNT + "\":  " + loanPaymentPlanRequest.getLoanAmount() + VakifBankConstant.COMMA +
                "\"" + VakifBankConstant.GRACE_PERIOD + "\": " + VakifBankConstant.GRACE_PERIOD_VALUE_0 + VakifBankConstant.COMMA +
                "\"" + VakifBankConstant.INSTALLMENT_PERIOD + "\": " + VakifBankConstant.INSTALLMENT_PERIOD_VALUE_1 + "}";
        byte[] body = parameters.getBytes();

        return new HttpEntity<>(body, getHeaders());
    }

    private GetLoanPaymentPlanResponse getLoansPaymentPlanResponse(ResponseEntity<String> result) throws LoanPaymentPlanResponseException {
        log.info("VakifBank Get Loans Payment Plan Response Call Starting");

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root;

        try {
            root = mapper.readTree(result.getBody());
        } catch (IOException e) {
            throw new LoanPaymentPlanResponseException();
        }

        JsonNode pathDataLoan = root.path(VakifBankConstant.DATA).path(VakifBankConstant.LOAN);
        Double intRate = (pathDataLoan.findValue(VakifBankConstant.INTEREST_RATE)).asDouble();
        Double loanTerm = (pathDataLoan.findValue(VakifBankConstant.LOAN_TERM)).asDouble();
        Double totalAmount = (pathDataLoan.findValue(VakifBankConstant.TOTAL_AMOUNT)).asDouble();
        Double installmentAmount = (pathDataLoan.findValue(VakifBankConstant.INSTALLMENT_AMOUNT)).asDouble();
        Double totalPaymentAmount = (installmentAmount * loanTerm);
        Double totalInterest = totalPaymentAmount - totalAmount;
        Double monthlyCostRate = ((installmentAmount - (totalAmount / loanTerm)) / totalInterest) * 100;

        return GetLoanPaymentPlanResponseConverter.convert(BankName.VAKIFBANK, intRate, totalInterest, monthlyCostRate, installmentAmount, totalPaymentAmount);
    }

    private HttpEntity<?> createCurrencyRatesEntity() throws TokenException {
        log.info("VakifBank Currency Rates Entity Call Starting");

        String parameters = VakifBankConstant.CURRENCY_RATES_ENTITY_PARAMETERS;
        byte[] body = parameters.getBytes();

        return new HttpEntity<>(body, getHeaders());
    }

    private List<GetCurrencyRatesResponse> getCurrencyRatesResponse(VakifBankBaseResponse vakifBankBaseResponse) {
        log.info("VakifBank Get Currency Rates Response Call Starting");

        if (vakifBankBaseResponse == null || vakifBankBaseResponse.getData() == null) {
            return new ArrayList<>();
        }

        return vakifBankBaseResponse.getData().getCurrency().stream()
                .map(currency -> GetCurrencyRatesResponse.builder()
                        .currencyName(currency.getCurrencyCode())
                        .sellRate(currency.getSaleRate())
                        .buyRate(currency.getPurchaseRate())
                        .averageRate(((currency.getSaleRate() + currency.getPurchaseRate()) / 2.0)).build()).collect(Collectors.toList());
    }
}