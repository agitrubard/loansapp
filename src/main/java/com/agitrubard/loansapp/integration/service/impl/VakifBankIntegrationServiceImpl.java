package com.agitrubard.loansapp.integration.service.impl;

import com.agitrubard.loansapp.domain.model.Constant;
import com.agitrubard.loansapp.domain.model.converter.GetLoansPaymentPlanResponseConverter;
import com.agitrubard.loansapp.domain.model.exception.LoansPaymentPlanResponseException;
import com.agitrubard.loansapp.domain.model.exception.TokenException;
import com.agitrubard.loansapp.domain.model.request.LoansPaymentPlanRequest;
import com.agitrubard.loansapp.domain.model.response.GetCurrencyRatesResponse;
import com.agitrubard.loansapp.domain.model.response.GetLoansPaymentPlanResponse;
import com.agitrubard.loansapp.domain.model.enums.BankName;
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
    public GetLoansPaymentPlanResponse getLoansPaymentPlan(LoansPaymentPlanRequest loansPaymentPlanRequest) throws TokenException, LoansPaymentPlanResponseException {
        log.info("VakifBank Loan Payment Plan Call Starting");

        HttpEntity<?> entity = getLoanEntity(loansPaymentPlanRequest);
        final ResponseEntity<String> result = restTemplate.exchange(vakifBankConfiguration.getLoanUrl(), HttpMethod.POST, entity, String.class);

        return getLoansPaymentPlanResponse(result);
    }

    @Override
    public List<GetCurrencyRatesResponse> getCurrencyRates() throws TokenException {
        log.info("VakifBank Currency Rates Call Starting");

        HttpEntity<?> entity = getCurrencyRatesEntity();
        final ResponseEntity<VakifBankBaseResponse> result = restTemplate.exchange(vakifBankConfiguration.getCurrencyRatesUrl(), HttpMethod.POST, entity, VakifBankBaseResponse.class);
        VakifBankBaseResponse vakifBankBaseResponse = result.getBody();

        return Optional.ofNullable(getCurrencyRatesResponse(vakifBankBaseResponse)).orElse(new ArrayList<>());
    }

    private MultiValueMap<String, String> getHeaders() throws TokenException {
        log.info("VakifBank Headers Call Starting");

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(Constant.CONTENT_TYPE, Constant.CONTENT_TYPE_APPLICATION_JSON);
        headers.add(Constant.AUTHORIZATION, Token.getToken(vakifBankConfiguration.getTokenUrl(), vakifBankConfiguration.getClientId(), vakifBankConfiguration.getClientSecret()));
        return headers;
    }

    private HttpEntity<?> getLoanEntity(LoansPaymentPlanRequest loansPaymentPlanRequest) throws TokenException {
        log.info("VakifBank Loan Entity Call Starting");

        String parameters = "{    \"" + Constant.VAKIFBANK_LOAN_PRODUCT_ID + "\": \"" + Constant.VAKIFBANK_LOAN_PRODUCT_ID_VALUE_41001 + "\",    " +
                "\"" + Constant.VAKIFBANK_LOAN_TERM + "\": " + loansPaymentPlanRequest.getLoanTerm() + ",    " +
                "\"" + Constant.VAKIFBANK_LOAN_AMOUNT + "\":  " + loansPaymentPlanRequest.getLoanAmount() + ",    " +
                "\"" + Constant.VAKIFBANK_GRACE_PERIOD + "\": " + Constant.VAKIFBANK_GRACE_PERIOD_VALUE_0 + ",    " +
                "\"" + Constant.VAKIFBANK_INSTALLMENT_PERIOD + "\": " + Constant.VAKIFBANK_INSTALLMENT_PERIOD_VALUE_1 + "}";
        byte[] body = parameters.getBytes();
        return new HttpEntity<>(body, getHeaders());
    }

    private GetLoansPaymentPlanResponse getLoansPaymentPlanResponse(ResponseEntity<String> result) throws LoansPaymentPlanResponseException {
        log.info("VakifBank Get Loans Payment Plan Response Call Starting");

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root;
        try {
            root = mapper.readTree(result.getBody());
        } catch (IOException e) {
            throw new LoansPaymentPlanResponseException();
        }
        Double intRate = (root.path(Constant.VAKIFBANK_DATA).path(Constant.VAKIFBANK_LOAN).findValue(Constant.VAKIFBANK_INTEREST_RATE)).asDouble();
        Double loanTerm = (root.path(Constant.VAKIFBANK_DATA).path(Constant.VAKIFBANK_LOAN).findValue(Constant.VAKIFBANK_LOAN_TERM)).asDouble();
        Double totalAmount = (root.path(Constant.VAKIFBANK_DATA).path(Constant.VAKIFBANK_LOAN).findValue(Constant.VAKIFBANK_TOTAL_AMOUNT)).asDouble();
        Double installmentAmount = (root.path(Constant.VAKIFBANK_DATA).path(Constant.VAKIFBANK_LOAN).findValue(Constant.VAKIFBANK_INSTALLMENT_AMOUNT)).asDouble();
        Double totalPaymentAmount = (installmentAmount * loanTerm);
        Double totalInterest = totalPaymentAmount - totalAmount;
        Double monthlyCostRate = ((installmentAmount - (totalAmount / loanTerm)) / totalInterest) * 100;

        return GetLoansPaymentPlanResponseConverter.convert(BankName.VAKIFBANK, intRate, totalInterest, monthlyCostRate, installmentAmount, totalPaymentAmount);
    }

    private HttpEntity<?> getCurrencyRatesEntity() throws TokenException {
        log.info("VakifBank Currency Rates Entity Call Starting");

        String parameters = Constant.VAKIFBANK_CURRENCY_RATES_ENTITY_PARAMETERS;
        byte[] body = parameters.getBytes();
        return new HttpEntity<>(body, getHeaders());
    }

    private List<GetCurrencyRatesResponse> getCurrencyRatesResponse(VakifBankBaseResponse vakifBankBaseResponse) {
        log.info("VakifBank Get Currency Rates Response Call Starting");

        if (vakifBankBaseResponse == null || vakifBankBaseResponse.getData() == null)
            return new ArrayList<>();

        return vakifBankBaseResponse.getData().getCurrency().stream()
                .map(currency -> GetCurrencyRatesResponse.builder()
                        .currencyName(currency.getCurrencyCode())
                        .sellRate(currency.getSaleRate())
                        .buyRate(currency.getPurchaseRate())
                        .averageRate(((currency.getSaleRate() + currency.getPurchaseRate()) / 2.0)).build()).collect(Collectors.toList());
    }
}