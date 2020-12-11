package com.agitrubard.loansapp.domain.model.converter;

import com.agitrubard.loansapp.domain.model.response.GetCurrencyRatesResponse;

public class GetCurrencyRatesResponseConverter {

    private GetCurrencyRatesResponseConverter() {
    }

    public static GetCurrencyRatesResponse convert(String currencyName, double sellRate, double buyRate, double averageRate) {
        GetCurrencyRatesResponse getCurrencyRatesResponse = new GetCurrencyRatesResponse();

        getCurrencyRatesResponse.setCurrencyName(currencyName);
        getCurrencyRatesResponse.setSellRate(sellRate);
        getCurrencyRatesResponse.setBuyRate(buyRate);
        getCurrencyRatesResponse.setAverageRate(averageRate);

        return getCurrencyRatesResponse;
    }
}