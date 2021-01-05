package com.agitrubard.loansapp.integration.service.impl;

import com.agitrubard.loansapp.domain.model.exception.LoanAmountException;
import com.agitrubard.loansapp.domain.model.exception.LoanTermException;
import com.agitrubard.loansapp.domain.model.exception.LoanPaymentPlanResponseException;
import com.agitrubard.loansapp.domain.model.exception.TokenException;
import com.agitrubard.loansapp.domain.model.request.LoanPaymentPlanRequest;
import com.agitrubard.loansapp.domain.model.response.GetLoanPaymentPlanResponse;
import com.agitrubard.loansapp.integration.service.CombinedBankingIntegrationService;
import com.agitrubard.loansapp.integration.service.VakifBankIntegrationService;
import com.agitrubard.loansapp.integration.service.YapiKrediIntegrationService;
import org.springframework.stereotype.Service;

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
    public List<GetLoanPaymentPlanResponse> getLoanPaymentPlans(LoanPaymentPlanRequest loanPaymentPlanRequest) throws ExecutionException, InterruptedException {
        CompletableFuture<GetLoanPaymentPlanResponse> getLoansPaymentPlanResponseVakifBank = getLoansPaymentPlanResponseVakifBank(loanPaymentPlanRequest);

        CompletableFuture<GetLoanPaymentPlanResponse> getLoansPaymentPlanResponseYapiKredi = getLoansPaymentPlanResponseYapiKredi(loanPaymentPlanRequest);

        CompletableFuture<List<GetLoanPaymentPlanResponse>> getLoansPaymentPlanResponses = getLoansPaymentPlanResponseVakifBank
                .thenCombineAsync(getLoansPaymentPlanResponseYapiKredi, (vakifBankLoans, yapiKrediLoans) -> {
                    List<GetLoanPaymentPlanResponse> responses = new ArrayList<>();
                    responses.add(vakifBankLoans);
                    responses.add(yapiKrediLoans);
                    return responses;
                });

        return getLoansPaymentPlanResponses.get();
    }

    private CompletableFuture<GetLoanPaymentPlanResponse> getLoansPaymentPlanResponseYapiKredi(LoanPaymentPlanRequest loanPaymentPlanRequest) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return yapiKrediIntegrationService.getLoanPaymentPlan(loanPaymentPlanRequest);
            } catch (LoanAmountException | LoanTermException | TokenException | LoanPaymentPlanResponseException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    private CompletableFuture<GetLoanPaymentPlanResponse> getLoansPaymentPlanResponseVakifBank(LoanPaymentPlanRequest loanPaymentPlanRequest) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return vakifBankIntegrationService.getLoanPaymentPlan(loanPaymentPlanRequest);
            } catch (LoanPaymentPlanResponseException | TokenException | LoanAmountException | LoanTermException e) {
                e.printStackTrace();
            }
            return null;
        });
    }
}