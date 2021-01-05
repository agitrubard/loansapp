package com.agitrubard.loansapp.domain.controller;

import com.agitrubard.loansapp.domain.controller.endpoint.CurrencyRatesEndpoint;
import com.agitrubard.loansapp.domain.model.exception.TokenException;
import com.agitrubard.loansapp.integration.service.CombinedBankingIntegrationService;
import com.agitrubard.loansapp.integration.service.VakifBankIntegrationService;
import com.agitrubard.loansapp.integration.service.YapiKrediIntegrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping(value = CurrencyRatesEndpoint.GET_CURRENCY_RATES_VAKIF_BANK)
    public ResponseEntity<Object> getCurrencyRatesVakifBank() throws TokenException {
        return ResponseEntity.ok(vakifBankIntegrationService.getCurrencyRates());
    }

    @GetMapping(value = CurrencyRatesEndpoint.GET_CURRENCY_RATES_YAPI_KREDI)
    public ResponseEntity<Object> getCurrencyRatesYapiKredi() throws TokenException {
        return ResponseEntity.ok(yapiKrediIntegrationService.getCurrencyRates());
    }
}