package com.agitrubard.loansapp.integration.service.impl;

import com.agitrubard.loansapp.domain.model.request.LoansPaymentPlanRequest;
import com.agitrubard.loansapp.domain.model.response.GetLoansPaymentPlanResponse;
import com.agitrubard.loansapp.integration.service.CombinedBankingIntegrationService;
import com.agitrubard.loansapp.integration.service.VakifBankIntegrationService;
import com.agitrubard.loansapp.integration.service.YapiKrediIntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Service
public class CombinedBankingIntegrationServiceImpl implements CombinedBankingIntegrationService {

    @Autowired
    VakifBankIntegrationService vakifBankIntegrationService;
    @Autowired
    YapiKrediIntegrationService yapiKrediIntegrationService;

    @Override
    public List<GetLoansPaymentPlanResponse> getLoansPaymentPlans(LoansPaymentPlanRequest loansPaymentPlanRequest) throws IOException {
        List<GetLoansPaymentPlanResponse> getLoansPaymentPlanResponses = new LinkedList<>();

        getLoansPaymentPlanResponses.add(vakifBankIntegrationService.getLoansPaymentPlan(loansPaymentPlanRequest));
        getLoansPaymentPlanResponses.add(yapiKrediIntegrationService.getLoansPaymentPlan(loansPaymentPlanRequest));

        return getLoansPaymentPlanResponses;
    }
}