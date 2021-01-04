package com.agitrubard.loansapp.domain.model.response.external.vakifbank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrencyDto {

    private String currencyCode;
    private Double rateDate;
    private Double saleRate;
    private Double purchaseRate;
}