package com.agitrubard.loansapp.domain.controller;

import com.agitrubard.loansapp.domain.controller.endpoint.LoanControllerEndpoint;
import com.agitrubard.loansapp.domain.model.exception.LoanAmountException;
import com.agitrubard.loansapp.domain.model.exception.LoanTermException;
import com.agitrubard.loansapp.domain.model.exception.LoansPaymentPlanResponseException;
import com.agitrubard.loansapp.domain.model.exception.TokenException;
import com.agitrubard.loansapp.domain.model.request.LoansPaymentPlanRequest;
import com.agitrubard.loansapp.domain.model.response.GetLoansPaymentPlanResponse;
import com.agitrubard.loansapp.integration.service.CombinedBankingIntegrationService;
import com.agitrubard.loansapp.integration.service.VakifBankIntegrationService;
import com.agitrubard.loansapp.integration.service.YapiKrediIntegrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(LoanControllerEndpoint.CUSTOMER)
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

    @PostMapping(value = LoanControllerEndpoint.GET_LOANS_PAYMENT_PLAN_VAKIFBANK)
    public ResponseEntity<Object> getLoansPaymentPlanVakifBank(@RequestBody @Valid LoansPaymentPlanRequest loansPaymentPlanRequest) throws LoansPaymentPlanResponseException, TokenException {
        return ResponseEntity.ok(vakifBankIntegrationService.getLoansPaymentPlan(loansPaymentPlanRequest));
    }

    @PostMapping(value = LoanControllerEndpoint.GET_LOANS_PAYMENT_PLAN_YAPIKREDI)
    public ResponseEntity<Object> getLoansPaymentPlanYapiKredi(@RequestBody @Valid LoansPaymentPlanRequest loansPaymentPlanRequest)
            throws LoansPaymentPlanResponseException, TokenException, LoanAmountException, LoanTermException {
        return ResponseEntity.ok(yapiKrediIntegrationService.getLoansPaymentPlan(loansPaymentPlanRequest));
    }

    @PostMapping(value = LoanControllerEndpoint.GET_LOANS_PAYMENT_PLANS)
    public List<GetLoansPaymentPlanResponse> getLoansPaymentPlans(@RequestBody @Valid LoansPaymentPlanRequest loansPaymentPlanRequest)
            throws ExecutionException, InterruptedException {
        return combinedBankingIntegrationService.getLoansPaymentPlans(loansPaymentPlanRequest);
    }
}