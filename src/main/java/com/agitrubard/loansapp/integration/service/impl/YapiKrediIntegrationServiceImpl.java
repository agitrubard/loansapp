package com.agitrubard.loansapp.integration.service.impl;

import com.agitrubard.loansapp.domain.model.Constant;
import com.agitrubard.loansapp.domain.model.converter.GetLoansPaymentPlanResponseConverter;
import com.agitrubard.loansapp.domain.model.enums.Currency;
import com.agitrubard.loansapp.domain.model.exception.LoanAmountException;
import com.agitrubard.loansapp.domain.model.exception.LoanTermException;
import com.agitrubard.loansapp.domain.model.exception.LoansPaymentPlanResponseException;
import com.agitrubard.loansapp.domain.model.exception.TokenException;
import com.agitrubard.loansapp.domain.model.request.LoansPaymentPlanRequest;
import com.agitrubard.loansapp.domain.model.response.GetCurrencyRatesResponse;
import com.agitrubard.loansapp.domain.model.response.GetLoansPaymentPlanResponse;
import com.agitrubard.loansapp.domain.model.enums.BankName;
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
    public GetLoansPaymentPlanResponse getLoansPaymentPlan(LoansPaymentPlanRequest loansPaymentPlanRequest) throws LoanAmountException, LoanTermException, TokenException, LoansPaymentPlanResponseException {
        log.info("YapiKredi Loan Payment Plan Call Starting");

        if (loansPaymentPlanRequest.getLoanAmount() < 2000) {
            throw new LoanAmountException();
        } else if (loansPaymentPlanRequest.getLoanTerm() <= 0) {
            throw new LoanTermException();
        }

        HttpEntity<?> entity = getLoanEntity(loansPaymentPlanRequest);
        final ResponseEntity<String> result = restTemplate.exchange(yapiKrediConfiguration.getLoanUrl(), HttpMethod.POST, entity, String.class);

        return getLoansPaymentPlanResponse(result);
    }

    @Override
    public List<GetCurrencyRatesResponse> getCurrencyRates() throws TokenException {
        log.info("YapiKredi Currency Rates Call Starting");

        HttpEntity<?> entity = getCurrencyRatesEntity();
        final ResponseEntity<YapiKrediBaseResponse> result = restTemplate.exchange(yapiKrediConfiguration.getCurrencyRatesUrl(), HttpMethod.GET, entity, YapiKrediBaseResponse.class);
        YapiKrediBaseResponse yapiKrediBaseResponse = result.getBody();

        return Optional.ofNullable(getCurrencyRatesResponse(yapiKrediBaseResponse)).orElse(new ArrayList<>());
    }

    private MultiValueMap<String, String> getHeaders() throws TokenException {
        log.info("YapiKredi Headers Call Starting");

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(Constant.CONTENT_TYPE, Constant.CONTENT_TYPE_APPLICATION_JSON);
        headers.add(Constant.AUTHORIZATION, Token.getToken(yapiKrediConfiguration.getTokenUrl(), yapiKrediConfiguration.getClientId(), yapiKrediConfiguration.getClientSecret()));
        return headers;
    }

    private HttpEntity<?> getLoanEntity(LoansPaymentPlanRequest loansPaymentPlanRequest) throws TokenException {
        log.info("YapiKredi Loan Entity Call Starting");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        String nop = String.valueOf(loansPaymentPlanRequest.getLoanTerm());
        String principal = String.valueOf(loansPaymentPlanRequest.getLoanAmount());

        body.add(Constant.YAPIKREDI_BRANCH_CODE, Constant.YAPIKREDI_BRANCH_CODE_VALUE_925);
        body.add(Constant.YAPIKREDI_CHANNEL_CODE, Constant.YAPIKREDI_CHANNEL_CODE_VALUE_OPN);
        body.add(Constant.YAPIKREDI_CATEGORY_CODE, Constant.YAPIKREDI_CATEGORY_CODE_VALUE_W9);
        body.add(Constant.YAPIKREDI_CLIENT_TYPE, Constant.YAPIKREDI_CLIENT_TYPE_VALUE_1);
        body.add(Constant.YAPIKREDI_NOP, nop);
        body.add(Constant.YAPIKREDI_PRINCIPAL, principal);

        return new HttpEntity<>(body, getHeaders());
    }

    private GetLoansPaymentPlanResponse getLoansPaymentPlanResponse(ResponseEntity<String> result) throws LoansPaymentPlanResponseException {
        log.info("YapiKredi Get Loans Payment Plan Response Call Starting");

        if (result.getBody() == null)
            return new GetLoansPaymentPlanResponse();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root;
        try {
            root = mapper.readTree(result.getBody());
        } catch (JsonProcessingException e) {
            throw new LoansPaymentPlanResponseException();
        }
        JsonNode responseReturn = root.path(Constant.YAPIKREDI_RESPONSE).path(Constant.YAPIKREDI_RETURN);
        Double intRate = (responseReturn.path(Constant.YAPIKREDI_INT_RATE)).asDouble() * 100;
        Double totalInterest = (responseReturn.path(Constant.YAPIKREDI_TOTAL_INTEREST)).asDouble();
        Double monthlyCostRate = (responseReturn.path(Constant.YAPIKREDI_MONTHLY_COST_RATE)).asDouble();
        Double installmentAmount = (responseReturn.path(Constant.YAPIKREDI_INSTALLMENT_LIST).findValue(Constant.YAPIKREDI_INSTALLMENT_AMOUNT)).asDouble();
        Double totalPaymentAmount = (responseReturn.path(Constant.YAPIKREDI_TOTAL_PAYMENT_AMOUNT)).asDouble();

        return GetLoansPaymentPlanResponseConverter.convert(BankName.YAPIKREDI, intRate, totalInterest, monthlyCostRate, installmentAmount, totalPaymentAmount);
    }

    private HttpEntity<?> getCurrencyRatesEntity() throws TokenException {
        log.info("YapiKredi Currency Rates Entity Call Starting");

        return new HttpEntity<>(getHeaders());
    }

    private List<GetCurrencyRatesResponse> getCurrencyRatesResponse(YapiKrediBaseResponse yapiKrediBaseResponse) {
        log.info("YapiKredi Get Currency Rates Response Call Starting");

        if (yapiKrediBaseResponse == null || yapiKrediBaseResponse.getResponse() == null)
            return new ArrayList<>();

        return yapiKrediBaseResponse.getResponse().getExchangeRateList().stream()
                .filter(currency -> currency.getMinorCurrency().equals(Currency.TL.toString()))
                .map(exchangeRate -> GetCurrencyRatesResponse.builder()
                        .currencyName(exchangeRate.getMajorCurrency())
                        .sellRate(exchangeRate.getSellRate())
                        .buyRate(exchangeRate.getBuyRate())
                        .averageRate(exchangeRate.getAverageRate()).build()).collect(Collectors.toList());
    }
}