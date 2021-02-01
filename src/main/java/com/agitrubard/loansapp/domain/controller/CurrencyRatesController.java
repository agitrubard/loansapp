package com.agitrubard.loansapp.domain.controller;

import com.agitrubard.loansapp.domain.controller.endpoint.CurrencyRatesEndpoint;
import com.agitrubard.loansapp.domain.model.exception.CurrencyRatesException;
import com.agitrubard.loansapp.domain.model.exception.TokenException;
import com.agitrubard.loansapp.integration.service.VakifBankIntegrationService;
import com.agitrubard.loansapp.integration.service.YapiKrediIntegrationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class CurrencyRatesController {
    private final VakifBankIntegrationService vakifBankIntegrationService;
    private final YapiKrediIntegrationService yapiKrediIntegrationService;

    @GetMapping(value = CurrencyRatesEndpoint.GET_CURRENCY_RATES_VAKIF_BANK)
    public ResponseEntity<Object> getCurrencyRatesVakifBank() throws TokenException, CurrencyRatesException {
        return ResponseEntity.ok(vakifBankIntegrationService.getCurrencyRates());
    }

    @GetMapping(value = CurrencyRatesEndpoint.GET_CURRENCY_RATES_YAPI_KREDI)
    public ResponseEntity<Object> getCurrencyRatesYapiKredi() throws TokenException, CurrencyRatesException {
        return ResponseEntity.ok(yapiKrediIntegrationService.getCurrencyRates());
    }
}