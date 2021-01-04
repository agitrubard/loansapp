package com.agitrubard.loansapp.domain.model.response.external.yapikredi;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExchangeRateDto {

    private String minorCurrency;
    private String majorCurrency;
    private Double sellRate;
    private Double buyRate;
    private Double averageRate;
}