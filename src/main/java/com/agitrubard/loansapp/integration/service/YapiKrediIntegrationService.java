package com.agitrubard.loansapp.integration.service;

import com.agitrubard.loansapp.domain.model.exception.*;
import com.agitrubard.loansapp.domain.model.request.LoanPaymentPlanRequest;
import com.agitrubard.loansapp.domain.model.response.GetCurrencyRatesResponse;
import com.agitrubard.loansapp.domain.model.response.GetLoanPaymentPlanResponse;

import java.util.List;

public interface YapiKrediIntegrationService extends BankingService {

    @Override
    GetLoanPaymentPlanResponse getLoanPaymentPlan(LoanPaymentPlanRequest loanPaymentPlanRequest) throws LoanAmountException, LoanTermException, TokenException, LoanPaymentPlanResponseException;

    @Override
    List<GetCurrencyRatesResponse> getCurrencyRates() throws TokenException, CurrencyRatesException;
}