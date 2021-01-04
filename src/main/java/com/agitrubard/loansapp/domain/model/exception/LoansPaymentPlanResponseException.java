package com.agitrubard.loansapp.domain.model.exception;

public class LoansPaymentPlanResponseException extends Exception{

    private static final long serialVersionUID = 3311398373722270988L;

    public LoansPaymentPlanResponseException() {
        super("Couldn't get Loans Payment Plan Response!");
    }

    public LoansPaymentPlanResponseException(Throwable cause) {
        super(cause);
    }
}