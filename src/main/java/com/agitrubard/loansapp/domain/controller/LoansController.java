package com.agitrubard.loansapp.domain.controller;

import com.agitrubard.loansapp.domain.controller.endpoint.LoanControllerEndpoint;
import com.agitrubard.loansapp.domain.model.request.LoansPaymentPlanRequest;
import com.agitrubard.loansapp.domain.model.response.GetLoansPaymentPlanResponse;
import com.agitrubard.loansapp.integration.service.CombinedBankingIntegrationService;
import com.agitrubard.loansapp.integration.service.VakifBankIntegrationService;
import com.agitrubard.loansapp.integration.service.YapiKrediIntegrationService;
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
    public GetLoansPaymentPlanResponse getLoansPaymentPlanVakifBank(@RequestBody @Valid LoansPaymentPlanRequest loansPaymentPlanRequest) throws IOException {
        return vakifBankIntegrationService.getLoansPaymentPlan(loansPaymentPlanRequest);
    }

    @PostMapping(value = LoanControllerEndpoint.GET_LOANS_PAYMENT_PLAN_YAPIKREDI)
    public GetLoansPaymentPlanResponse getLoansPaymentPlanYapiKredi(@RequestBody @Valid LoansPaymentPlanRequest loansPaymentPlanRequest) throws IOException {
        return yapiKrediIntegrationService.getLoansPaymentPlan(loansPaymentPlanRequest);
    }

    @PostMapping(value = LoanControllerEndpoint.GET_LOANS_PAYMENT_PLANS)
    public List<GetLoansPaymentPlanResponse> getLoansPaymentPlans(@RequestBody @Valid LoansPaymentPlanRequest loansPaymentPlanRequest) throws IOException, ExecutionException, InterruptedException {
        return combinedBankingIntegrationService.getLoansPaymentPlans(loansPaymentPlanRequest);
    }
}