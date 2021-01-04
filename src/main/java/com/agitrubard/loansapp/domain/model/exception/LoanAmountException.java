package com.agitrubard.loansapp.domain.model.exception;

public class LoanAmountException extends Exception {

    private static final long serialVersionUID = -2320558498807390367L;

    public LoanAmountException() {
        super("Loan amount can't be small 2000!");
    }

    public LoanAmountException(Throwable cause) {
        super(cause);
    }
}