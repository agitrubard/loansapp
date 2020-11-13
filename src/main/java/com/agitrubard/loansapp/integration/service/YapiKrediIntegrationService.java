package com.agitrubard.loansapp.integration.service;

import com.agitrubard.loansapp.domain.controller.request.LoansPaymentPlanRequest;

import java.io.IOException;

public interface YapiKrediIntegrationService extends BankingService {

    @Override
    String getLoansPaymentPlan(LoansPaymentPlanRequest loansPaymentPlanRequest) throws IOException;
}
