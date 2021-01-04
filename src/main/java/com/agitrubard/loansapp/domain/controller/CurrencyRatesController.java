package com.agitrubard.loansapp.domain.controller;

import com.agitrubard.loansapp.domain.controller.endpoint.CurrencyRatesEndpoint;
import com.agitrubard.loansapp.domain.model.exception.TokenException;
import com.agitrubard.loansapp.domain.model.response.GetCurrencyRatesResponse;
import com.agitrubard.loansapp.integration.service.CombinedBankingIntegrationService;
import com.agitrubard.loansapp.integration.service.VakifBankIntegrationService;
import com.agitrubard.loansapp.integration.service.YapiKrediIntegrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CurrencyRatesController {
    VakifBankIntegrationService vakifBankIntegrationService;
    YapiKrediIntegrationService yapiKrediIntegrationService;
    CombinedBankingIntegrationService combinedBankingIntegrationService;

    public CurrencyRatesController(VakifBankIntegrationService vakifBankIntegrationService,
                                   YapiKrediIntegrationService yapiKrediIntegrationService,
                                   CombinedBankingIntegrationService combinedBankingIntegrationService) {
        this.vakifBankIntegrationService = vakifBankIntegrationService;
        this.yapiKrediIntegrationService = yapiKrediIntegrationService;
        this.combinedBankingIntegrationService = combinedBankingIntegrationService;
    }

    @GetMapping(value = CurrencyRatesEndpoint.GET_CURRENCY_RATES_VAKIFBANK)
    public List<GetCurrencyRatesResponse> getCurrencyRatesVakifBank() throws TokenException {
        return vakifBankIntegrationService.getCurrencyRates();
    }
    @GetMapping(value = CurrencyRatesEndpoint.GET_CURRENCY_RATES_YAPIKREDI)
    public ResponseEntity<Object> getCurrencyRatesYapiKredi() throws TokenException {
        return ResponseEntity.ok(yapiKrediIntegrationService.getCurrencyRates());
    }
}