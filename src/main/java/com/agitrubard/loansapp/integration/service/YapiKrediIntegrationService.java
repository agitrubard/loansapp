package com.agitrubard.loansapp.integration.service;

import com.agitrubard.loansapp.domain.model.exception.LoanAmountException;
import com.agitrubard.loansapp.domain.model.exception.LoanTermException;
import com.agitrubard.loansapp.domain.model.exception.LoansPaymentPlanResponseException;
import com.agitrubard.loansapp.domain.model.exception.TokenException;
import com.agitrubard.loansapp.domain.model.request.LoansPaymentPlanRequest;
import com.agitrubard.loansapp.domain.model.response.GetCurrencyRatesResponse;
import com.agitrubard.loansapp.domain.model.response.GetLoansPaymentPlanResponse;

import java.util.List;

public interface YapiKrediIntegrationService extends BankingService {

    @Override
    GetLoansPaymentPlanResponse getLoansPaymentPlan(LoansPaymentPlanRequest loansPaymentPlanRequest) throws LoanAmountException, LoanTermException, TokenException, LoansPaymentPlanResponseException;

    @Override
    List<GetCurrencyRatesResponse> getCurrencyRates() throws TokenException;
}