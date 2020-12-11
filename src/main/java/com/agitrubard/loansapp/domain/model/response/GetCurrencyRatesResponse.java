package com.agitrubard.loansapp.domain.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetCurrencyRatesResponse {

    private String currencyName;
    private double sellRate;
    private double buyRate;
    private double averageRate;
}