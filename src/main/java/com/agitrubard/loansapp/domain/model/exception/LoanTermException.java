package com.agitrubard.loansapp.domain.model.exception;

public class LoanTermException extends Exception {

    private static final long serialVersionUID = -6001689724356238197L;

    public LoanTermException() {
        super("Loan term can't be small 0!");
    }

    public LoanTermException(Throwable cause) {
        super(cause);
    }
}