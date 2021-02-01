package com.agitrubard.loansapp.domain.model.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler({LoanPaymentPlanResponseException.class})
    public String loanPaymentPlanResponse() {
        return "Couldn't get Loans Payment Plan Response!";
    }

    @ExceptionHandler({LoanAmountException.class})
    public String loanAmount() {
        return "Loan amount can't be small 2000!";
    }

    @ExceptionHandler({LoanTermException.class})
    public String loanTerm() {
        return "Loan term can't be small 0!";
    }

    @ExceptionHandler({TokenException.class})
    public String token() {
        return "Couldn't get tokens!";
    }
}