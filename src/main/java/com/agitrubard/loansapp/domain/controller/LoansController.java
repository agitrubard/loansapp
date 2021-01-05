package com.agitrubard.loansapp.domain.controller;

import com.agitrubard.loansapp.domain.controller.endpoint.LoansControllerEndpoint;
import com.agitrubard.loansapp.domain.model.exception.LoanAmountException;
import com.agitrubard.loansapp.domain.model.exception.LoanTermException;
import com.agitrubard.loansapp.domain.model.exception.LoanPaymentPlanResponseException;
import com.agitrubard.loansapp.domain.model.exception.TokenException;
import com.agitrubard.loansapp.domain.model.request.LoanPaymentPlanRequest;
import com.agitrubard.loansapp.domain.model.response.GetLoanPaymentPlanResponse;
import com.agitrubard.loansapp.integration.service.CombinedBankingIntegrationService;
import com.agitrubard.loansapp.integration.service.VakifBankIntegrationService;
import com.agitrubard.loansapp.integration.service.YapiKrediIntegrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(LoansControllerEndpoint.CUSTOMER)
public class LoansController {
    VakifBankIntegrationService vakifBankIntegrationService;
    YapiKrediIntegrationService yapiKrediIntegrationService;
    CombinedBankingIntegrationService combinedBankingIntegrationService;

    public LoansController(VakifBankIntegrationService vakifBankIntegrationService,
                           YapiKrediIntegrationService yapiKrediIntegrationService,
                           CombinedBankingIntegrationService combinedBankingIntegrationService) {
        this.vakifBankIntegrationService = vakifBankIntegrationService;
        this.yapiKrediIntegrationService = yapiKrediIntegrationService;
        this.combinedBankingIntegrationService = combinedBankingIntegrationService;
    }

    @PostMapping(value = LoansControllerEndpoint.GET_LOAN_PAYMENT_PLAN_VAKIF_BANK)
    public ResponseEntity<Object> getLoanPaymentPlanVakifBank(@RequestBody @Valid LoanPaymentPlanRequest loanPaymentPlanRequest) throws LoanPaymentPlanResponseException, TokenException, LoanTermException, LoanAmountException {
        return ResponseEntity.ok(vakifBankIntegrationService.getLoanPaymentPlan(loanPaymentPlanRequest));
    }

    @PostMapping(value = LoansControllerEndpoint.GET_LOAN_PAYMENT_PLAN_YAPI_KREDI)
    public ResponseEntity<Object> getLoanPaymentPlanYapiKredi(@RequestBody @Valid LoanPaymentPlanRequest loanPaymentPlanRequest)
            throws LoanPaymentPlanResponseException, TokenException, LoanAmountException, LoanTermException {
        return ResponseEntity.ok(yapiKrediIntegrationService.getLoanPaymentPlan(loanPaymentPlanRequest));
    }

    @PostMapping(value = LoansControllerEndpoint.GET_LOAN_PAYMENT_PLANS)
    public List<GetLoanPaymentPlanResponse> getLoanPaymentPlans(@RequestBody @Valid LoanPaymentPlanRequest loanPaymentPlanRequest)
            throws ExecutionException, InterruptedException {
        return combinedBankingIntegrationService.getLoanPaymentPlans(loanPaymentPlanRequest);
    }
}