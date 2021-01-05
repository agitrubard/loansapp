package com.agitrubard.loansapp.domain.model.exception;

public class LoanPaymentPlanResponseException extends Exception {

    private static final long serialVersionUID = 3311398373722270988L;

    public LoanPaymentPlanResponseException() {
        super("Couldn't get Loans Payment Plan Response!");
    }

    public LoanPaymentPlanResponseException(Throwable cause) {
        super(cause);
    }
}