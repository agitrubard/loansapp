package com.agitrubard.loansapp.integration.service.impl;

import com.agitrubard.loansapp.domain.model.request.LoansPaymentPlanRequest;
import com.agitrubard.loansapp.domain.model.response.GetLoansPaymentPlanResponse;
import com.agitrubard.loansapp.integration.service.CombinedBankingIntegrationService;
import com.agitrubard.loansapp.integration.service.VakifBankIntegrationService;
import com.agitrubard.loansapp.integration.service.YapiKrediIntegrationService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class CombinedBankingIntegrationServiceImpl implements CombinedBankingIntegrationService {

    VakifBankIntegrationService vakifBankIntegrationService;
    YapiKrediIntegrationService yapiKrediIntegrationService;

    public CombinedBankingIntegrationServiceImpl(VakifBankIntegrationService vakifBankIntegrationService, YapiKrediIntegrationService yapiKrediIntegrationService) {
        this.vakifBankIntegrationService = vakifBankIntegrationService;
        this.yapiKrediIntegrationService = yapiKrediIntegrationService;
    }

    @Override
    public List<GetLoansPaymentPlanResponse> getLoansPaymentPlans(LoansPaymentPlanRequest loansPaymentPlanRequest) throws ExecutionException, InterruptedException {
        CompletableFuture<GetLoansPaymentPlanResponse> getLoansPaymentPlanResponseVakifBank = getLoansPaymentPlanResponseVakifBank(loansPaymentPlanRequest);

        CompletableFuture<GetLoansPaymentPlanResponse> getLoansPaymentPlanResponseYapiKredi = getLoansPaymentPlanResponseYapiKredi(loansPaymentPlanRequest);

        CompletableFuture<List<GetLoansPaymentPlanResponse>> getLoansPaymentPlanResponses = getLoansPaymentPlanResponseVakifBank
                .thenCombineAsync(getLoansPaymentPlanResponseYapiKredi, (vakifBankLoans, yapiKrediLoans) -> {
                    List<GetLoansPaymentPlanResponse> responses = new ArrayList<>();
                    responses.add(vakifBankLoans);
                    responses.add(yapiKrediLoans);
                    return responses;
                });

        return getLoansPaymentPlanResponses.get();
    }

    private CompletableFuture<GetLoansPaymentPlanResponse> getLoansPaymentPlanResponseYapiKredi(LoansPaymentPlanRequest loansPaymentPlanRequest) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return yapiKrediIntegrationService.getLoansPaymentPlan(loansPaymentPlanRequest);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    private CompletableFuture<GetLoansPaymentPlanResponse> getLoansPaymentPlanResponseVakifBank(LoansPaymentPlanRequest loansPaymentPlanRequest) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return vakifBankIntegrationService.getLoansPaymentPlan(loansPaymentPlanRequest);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
    }
}