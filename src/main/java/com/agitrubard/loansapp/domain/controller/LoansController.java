package com.agitrubard.loansapp.domain.controller;

import com.agitrubard.loansapp.domain.controller.endpoint.LoanControllerEndpoint;
import com.agitrubard.loansapp.domain.controller.request.LoansPaymentPlanRequest;
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

    @GetMapping(value = LoanControllerEndpoint.GET_LOAN_PAYMENT_PLAN)
    public String getLoanPaymentPlan(@RequestBody LoansPaymentPlanRequest loansPaymentPlanRequest) throws IOException {
        return "------------ VAKIFBANK ------------\n" + vakifBankIntegrationService.getLoansPaymentPlan(loansPaymentPlanRequest) + "\n\n" +
               "------------ YAPIKREDİ ------------\n" + yapiKrediIntegrationService.getLoansPaymentPlan(loansPaymentPlanRequest);
    }
}