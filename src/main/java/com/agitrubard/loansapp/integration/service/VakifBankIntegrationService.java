package com.agitrubard.loansapp.integration.service;

import com.agitrubard.loansapp.domain.model.request.LoansPaymentPlanRequest;
import com.agitrubard.loansapp.domain.model.response.GetCurrencyRatesResponse;
import com.agitrubard.loansapp.domain.model.response.GetLoansPaymentPlanResponse;

import java.io.IOException;
import java.util.List;

public interface VakifBankIntegrationService extends BankingService {

    @Override
    GetLoansPaymentPlanResponse getLoansPaymentPlan(LoansPaymentPlanRequest loansPaymentPlanRequest) throws IOException;

    @Override
    List<GetCurrencyRatesResponse> getCurrencyRates() throws IOException;
}