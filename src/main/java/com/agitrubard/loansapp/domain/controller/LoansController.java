package com.agitrubard.loansapp.domain.controller;

import com.agitrubard.loansapp.domain.controller.endpoint.LoanControllerEndpoint;
import com.agitrubard.loansapp.domain.model.request.LoansPaymentPlanRequest;
import com.agitrubard.loansapp.domain.model.response.GetLoansPaymentPlanResponse;
import com.agitrubard.loansapp.integration.service.VakifBankIntegrationService;
import com.agitrubard.loansapp.integration.service.YapiKrediIntegrationService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(LoanControllerEndpoint.CUSTOMER)
public class LoansController {
    VakifBankIntegrationService vakifBankIntegrationService;
    YapiKrediIntegrationService yapiKrediIntegrationService;

    public LoansController(VakifBankIntegrationService vakifBankIntegrationService, YapiKrediIntegrationService yapiKrediIntegrationService) {
        this.vakifBankIntegrationService = vakifBankIntegrationService;
        this.yapiKrediIntegrationService = yapiKrediIntegrationService;
    }

    @GetMapping(value = LoanControllerEndpoint.GET_LOAN_PAYMENT_PLAN_VAKIFBANK)
    public GetLoansPaymentPlanResponse getLoanPaymentPlanVakifBank(@RequestBody LoansPaymentPlanRequest loansPaymentPlanRequest) throws IOException {
        return vakifBankIntegrationService.getLoansPaymentPlan(loansPaymentPlanRequest);
    }

    @GetMapping(value = LoanControllerEndpoint.GET_LOAN_PAYMENT_PLAN_YAPIKREDI)
    public GetLoansPaymentPlanResponse getLoanPaymentPlanYapiKredi(@RequestBody LoansPaymentPlanRequest loansPaymentPlanRequest) throws IOException {
        return yapiKrediIntegrationService.getLoansPaymentPlan(loansPaymentPlanRequest);
    }
}