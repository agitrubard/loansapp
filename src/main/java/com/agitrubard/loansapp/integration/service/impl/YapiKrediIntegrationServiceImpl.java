package com.agitrubard.loansapp.integration.service.impl;

import com.agitrubard.loansapp.domain.model.converter.GetLoanPaymentPlanResponseConverter;
import com.agitrubard.loansapp.domain.model.enums.Currency;
import com.agitrubard.loansapp.domain.model.exception.LoanAmountException;
import com.agitrubard.loansapp.domain.model.exception.LoanTermException;
import com.agitrubard.loansapp.domain.model.exception.LoanPaymentPlanResponseException;
import com.agitrubard.loansapp.domain.model.exception.TokenException;
import com.agitrubard.loansapp.domain.model.request.LoanPaymentPlanRequest;
import com.agitrubard.loansapp.domain.model.response.GetCurrencyRatesResponse;
import com.agitrubard.loansapp.domain.model.response.GetLoanPaymentPlanResponse;
import com.agitrubard.loansapp.domain.model.enums.BankName;
import com.agitrubard.loansapp.domain.model.response.constant.CustomConstant;
import com.agitrubard.loansapp.domain.model.response.constant.YapiKrediConstant;
import com.agitrubard.loansapp.domain.model.response.external.yapikredi.YapiKrediBaseResponse;
import com.agitrubard.loansapp.integration.api.authorization.Token;
import com.agitrubard.loansapp.integration.api.config.YapiKrediConfiguration;
import com.agitrubard.loansapp.integration.service.YapiKrediIntegrationService;
import com.fasterxml.jackson.core.JsonProcessingException;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class YapiKrediIntegrationServiceImpl implements YapiKrediIntegrationService {
    private final YapiKrediConfiguration yapiKrediConfiguration;
    private final RestTemplate restTemplate;

    public YapiKrediIntegrationServiceImpl(YapiKrediConfiguration yapiKrediConfiguration, RestTemplate restTemplate) {
        this.yapiKrediConfiguration = yapiKrediConfiguration;
        this.restTemplate = restTemplate;
    }

    @Override
    public GetLoanPaymentPlanResponse getLoanPaymentPlan(LoanPaymentPlanRequest loanPaymentPlanRequest) throws LoanAmountException, LoanTermException, TokenException, LoanPaymentPlanResponseException {
        log.info("YapiKredi Loan Payment Plan Call Starting");

        if (loanPaymentPlanRequest.getLoanAmount() < 2000) {
            throw new LoanAmountException();
        } else if (loanPaymentPlanRequest.getLoanTerm() <= 0) {
            throw new LoanTermException();
        }

        HttpEntity<?> entity = createLoanEntity(loanPaymentPlanRequest);
        final ResponseEntity<String> result = restTemplate.exchange(yapiKrediConfiguration.getLoanUrl(), HttpMethod.POST, entity, String.class);

        return getLoansPaymentPlanResponse(result);
    }

    @Override
    public List<GetCurrencyRatesResponse> getCurrencyRates() throws TokenException {
        log.info("YapiKredi Currency Rates Call Starting");

        HttpEntity<?> entity = createCurrencyRatesEntity();
        final ResponseEntity<YapiKrediBaseResponse> result = restTemplate.exchange(yapiKrediConfiguration.getCurrencyRatesUrl(), HttpMethod.GET, entity, YapiKrediBaseResponse.class);
        YapiKrediBaseResponse yapiKrediBaseResponse = result.getBody();

        return Optional.ofNullable(getCurrencyRatesResponse(yapiKrediBaseResponse)).orElse(new ArrayList<>());
    }

    private MultiValueMap<String, String> getHeaders() throws TokenException {
        log.info("YapiKredi Headers Call Starting");

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(CustomConstant.CONTENT_TYPE, CustomConstant.CONTENT_TYPE_APPLICATION_JSON);
        headers.add(CustomConstant.AUTHORIZATION, Token.getAccessToken(yapiKrediConfiguration.getTokenUrl(), yapiKrediConfiguration.getClientId(), yapiKrediConfiguration.getClientSecret()));

        return headers;
    }

    private HttpEntity<?> createLoanEntity(LoanPaymentPlanRequest loanPaymentPlanRequest) throws TokenException {
        log.info("YapiKredi Loan Entity Call Starting");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        String nop = String.valueOf(loanPaymentPlanRequest.getLoanTerm());
        String principal = String.valueOf(loanPaymentPlanRequest.getLoanAmount());

        body.add(YapiKrediConstant.BRANCH_CODE, YapiKrediConstant.BRANCH_CODE_VALUE_925);
        body.add(YapiKrediConstant.CHANNEL_CODE, YapiKrediConstant.CHANNEL_CODE_VALUE_OPN);
        body.add(YapiKrediConstant.CATEGORY_CODE, YapiKrediConstant.CATEGORY_CODE_VALUE_W9);
        body.add(YapiKrediConstant.CLIENT_TYPE, YapiKrediConstant.CLIENT_TYPE_VALUE_1);
        body.add(YapiKrediConstant.NOP, nop);
        body.add(YapiKrediConstant.PRINCIPAL, principal);

        return new HttpEntity<>(body, getHeaders());
    }

    private GetLoanPaymentPlanResponse getLoansPaymentPlanResponse(ResponseEntity<String> result) throws LoanPaymentPlanResponseException {
        log.info("YapiKredi Get Loans Payment Plan Response Call Starting");

        if (result.getBody() == null)
            return new GetLoanPaymentPlanResponse();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root;

        try {
            root = mapper.readTree(result.getBody());
        } catch (JsonProcessingException e) {
            throw new LoanPaymentPlanResponseException();
        }

        JsonNode pathResponseReturn = root.path(YapiKrediConstant.RESPONSE).path(YapiKrediConstant.RETURN);
        Double intRate = (pathResponseReturn.path(YapiKrediConstant.INT_RATE)).asDouble() * 100;
        Double totalInterest = (pathResponseReturn.path(YapiKrediConstant.TOTAL_INTEREST)).asDouble();
        Double monthlyCostRate = (pathResponseReturn.path(YapiKrediConstant.MONTHLY_COST_RATE)).asDouble();
        Double installmentAmount = (pathResponseReturn.path(YapiKrediConstant.INSTALLMENT_LIST).findValue(YapiKrediConstant.INSTALLMENT_AMOUNT)).asDouble();
        Double totalPaymentAmount = (pathResponseReturn.path(YapiKrediConstant.TOTAL_PAYMENT_AMOUNT)).asDouble();

        return GetLoanPaymentPlanResponseConverter.convert(BankName.YAPIKREDI, intRate, totalInterest, monthlyCostRate, installmentAmount, totalPaymentAmount);
    }

    private HttpEntity<?> createCurrencyRatesEntity() throws TokenException {
        log.info("YapiKredi Currency Rates Entity Call Starting");

        return new HttpEntity<>(getHeaders());
    }

    private List<GetCurrencyRatesResponse> getCurrencyRatesResponse(YapiKrediBaseResponse yapiKrediBaseResponse) {
        log.info("YapiKredi Get Currency Rates Response Call Starting");

        if (yapiKrediBaseResponse == null || yapiKrediBaseResponse.getResponse() == null) {
            return new ArrayList<>();
        }

        return yapiKrediBaseResponse.getResponse().getExchangeRateList().stream()
                .filter(currency -> Currency.TL.toString().equals(currency.getMinorCurrency()))
                .filter(currency -> !
                        Currency.TL.toString().equals(currency.getMajorCurrency()))
                .map(exchangeRate -> GetCurrencyRatesResponse.builder()
                        .currencyName(exchangeRate.getMajorCurrency())
                        .sellRate(exchangeRate.getSellRate())
                        .buyRate(exchangeRate.getBuyRate())
                        .averageRate(exchangeRate.getAverageRate()).build()).collect(Collectors.toList());
    }
}