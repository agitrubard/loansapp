package com.agitrubard.loansapp.service;

import com.agitrubard.loansapp.controller.request.LoansPaymentPlanRequest;

import java.io.IOException;

public interface LoansService {

    StringBuilder getLoansPaymentPlan(LoansPaymentPlanRequest loansPaymentPlanRequest) throws IOException;
}