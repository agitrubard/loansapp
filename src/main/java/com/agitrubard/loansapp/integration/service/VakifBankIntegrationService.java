package com.agitrubard.loansapp.integration.service;

import com.agitrubard.loansapp.domain.model.request.LoansPaymentPlanRequest;
import com.agitrubard.loansapp.domain.model.response.GetLoansPaymentPlanResponse;

import java.io.IOException;

public interface VakifBankIntegrationService extends BankingService {

    @Override
    GetLoansPaymentPlanResponse getLoansPaymentPlan(LoansPaymentPlanRequest loansPaymentPlanRequest) throws IOException;
}