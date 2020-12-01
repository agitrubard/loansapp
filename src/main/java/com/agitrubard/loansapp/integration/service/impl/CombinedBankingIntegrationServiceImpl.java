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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class CombinedBankingIntegrationServiceImpl implements CombinedBankingIntegrationService {

    @Autowired
    VakifBankIntegrationService vakifBankIntegrationService;
    @Autowired
    YapiKrediIntegrationService yapiKrediIntegrationService;

    @Override
    public List<GetLoansPaymentPlanResponse> getLoansPaymentPlans(LoansPaymentPlanRequest loansPaymentPlanRequest) throws ExecutionException, InterruptedException {
        CompletableFuture<GetLoansPaymentPlanResponse> getLoansPaymentPlanResponseVakifBank = CompletableFuture.supplyAsync(() -> {
            try {
                return vakifBankIntegrationService.getLoansPaymentPlan(loansPaymentPlanRequest);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });

        CompletableFuture<GetLoansPaymentPlanResponse> getLoansPaymentPlanResponseYapiKredi = CompletableFuture.supplyAsync(() -> {
            try {
                return yapiKrediIntegrationService.getLoansPaymentPlan(loansPaymentPlanRequest);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });

        CompletableFuture<List<GetLoansPaymentPlanResponse>> getLoansPaymentPlanResponses = getLoansPaymentPlanResponseVakifBank
                .thenCombineAsync(getLoansPaymentPlanResponseYapiKredi, (vakifBankLoans, yapiKrediLoans) -> {
                    List<GetLoansPaymentPlanResponse> responses = new LinkedList<>();

                    responses.add(vakifBankLoans);
                    responses.add(yapiKrediLoans);

                    return responses;
                });

        return getLoansPaymentPlanResponses.get();
    }
}