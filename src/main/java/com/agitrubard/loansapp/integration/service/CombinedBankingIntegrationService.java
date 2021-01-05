package com.agitrubard.loansapp.integration.service;

import com.agitrubard.loansapp.domain.model.request.LoanPaymentPlanRequest;
import com.agitrubard.loansapp.domain.model.response.GetLoanPaymentPlanResponse;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface CombinedBankingIntegrationService {

    List<GetLoanPaymentPlanResponse> getLoanPaymentPlans(LoanPaymentPlanRequest loanPaymentPlanRequest) throws ExecutionException, InterruptedException;
}