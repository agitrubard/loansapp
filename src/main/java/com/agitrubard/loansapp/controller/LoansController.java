package com.agitrubard.loansapp.controller;

import com.agitrubard.loansapp.controller.request.LoansPaymentPlanRequest;
import com.agitrubard.loansapp.service.LoansService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/customer")
public class LoansController {

    @Autowired
    LoansService loansService;

    @GetMapping(value = "/loanPaymentPlan")
    public StringBuilder getLoanPaymentPlan(@RequestBody LoansPaymentPlanRequest loansPaymentPlanRequest) throws IOException {
        return loansService.getLoansPaymentPlan(loansPaymentPlanRequest);
    }
}