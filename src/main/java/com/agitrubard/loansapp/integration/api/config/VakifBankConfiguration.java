package com.agitrubard.loansapp.integration.api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@ConfigurationProperties("vakifbankconfig")
@Getter
@Setter
public class VakifBankConfiguration {

    @Value("client-id")
    private String clientId;
    @Value("client-secret")
    private String clientSecret;
    @Value("token-url")
    private String tokenUrl;
    @Value("loan-url")
    private String loanUrl;
    @Value("currency-rates-url")
    private String currencyRatesUrl;
}