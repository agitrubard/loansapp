package com.agitrubard.loansapp.integration.service;

import com.agitrubard.loansapp.domain.controller.request.LoansPaymentPlanRequest;
import com.agitrubard.loansapp.domain.controller.response.GetLoansPaymentPlanResponse;

import java.io.IOException;

public interface BankingService {

    GetLoansPaymentPlanResponse getLoansPaymentPlan(LoansPaymentPlanRequest loansPaymentPlanRequest) throws IOException;
}