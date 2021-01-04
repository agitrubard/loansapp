package com.agitrubard.loansapp.domain.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class GetCurrencyRatesResponse {

    private String currencyName;
    private Double sellRate;
    private Double buyRate;
    private Double averageRate;
}