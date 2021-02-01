package com.agitrubard.loansapp.integration.service.impl;

import com.agitrubard.loansapp.domain.model.enums.BankName;
import com.agitrubard.loansapp.domain.model.exception.LoanAmountException;
import com.agitrubard.loansapp.domain.model.exception.LoanTermException;
import com.agitrubard.loansapp.domain.model.exception.LoanPaymentPlanResponseException;
import com.agitrubard.loansapp.domain.model.exception.TokenException;
import com.agitrubard.loansapp.domain.model.request.LoanPaymentPlanRequest;
import com.agitrubard.loansapp.domain.model.response.GetLoanPaymentPlanResponse;
import com.agitrubard.loansapp.integration.service.CombinedBankingIntegrationService;
import com.agitrubard.loansapp.integration.service.VakifBankIntegrationService;
import com.agitrubard.loansapp.integration.service.YapiKrediIntegrationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@AllArgsConstructor
@Slf4j
public class CombinedBankingIntegrationServiceImpl implements CombinedBankingIntegrationService {
    private final VakifBankIntegrationService vakifBankIntegrationService;
    private final YapiKrediIntegrationService yapiKrediIntegrationService;

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
                log.error(BankName.YAPIKREDI + " Get Loans Payment Plan Response in Combined Banking Integration Service");
            }
            return null;
        });
    }

    private CompletableFuture<GetLoanPaymentPlanResponse> getLoansPaymentPlanResponseVakifBank(LoanPaymentPlanRequest loanPaymentPlanRequest) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return vakifBankIntegrationService.getLoanPaymentPlan(loanPaymentPlanRequest);
            } catch (LoanPaymentPlanResponseException | TokenException | LoanAmountException | LoanTermException e) {
                log.error(BankName.VAKIFBANK + " Get Loans Payment Plan Response in Combined Banking Integration Service");
            }
            return null;
        });
    }
}