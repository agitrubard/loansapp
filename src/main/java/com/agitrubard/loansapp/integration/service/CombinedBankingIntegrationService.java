package com.agitrubard.loansapp.integration.service;

import com.agitrubard.loansapp.domain.model.request.LoansPaymentPlanRequest;
import com.agitrubard.loansapp.domain.model.response.GetLoansPaymentPlanResponse;

import java.io.IOException;
import java.util.List;

public interface CombinedBankingIntegrationService {

    List<GetLoansPaymentPlanResponse> getLoansPaymentPlans(LoansPaymentPlanRequest loansPaymentPlanRequest) throws IOException;
}