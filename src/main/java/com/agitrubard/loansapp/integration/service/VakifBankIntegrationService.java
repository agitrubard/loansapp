package com.agitrubard.loansapp.integration.service;

import com.agitrubard.loansapp.domain.model.exception.LoanAmountException;
import com.agitrubard.loansapp.domain.model.exception.LoanTermException;
import com.agitrubard.loansapp.domain.model.exception.LoanPaymentPlanResponseException;
import com.agitrubard.loansapp.domain.model.exception.TokenException;
import com.agitrubard.loansapp.domain.model.request.LoanPaymentPlanRequest;
import com.agitrubard.loansapp.domain.model.response.GetCurrencyRatesResponse;
import com.agitrubard.loansapp.domain.model.response.GetLoanPaymentPlanResponse;

import java.util.List;

public interface VakifBankIntegrationService extends BankingService {

    @Override
    GetLoanPaymentPlanResponse getLoanPaymentPlan(LoanPaymentPlanRequest loanPaymentPlanRequest) throws LoanAmountException, LoanTermException, TokenException, LoanPaymentPlanResponseException;

    @Override
    List<GetCurrencyRatesResponse> getCurrencyRates() throws TokenException;
}