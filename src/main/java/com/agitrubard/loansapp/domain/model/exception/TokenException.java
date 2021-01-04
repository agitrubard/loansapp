package com.agitrubard.loansapp.domain.model.exception;

public class TokenException extends Exception {

    private static final long serialVersionUID = -751589503502156234L;

    public TokenException() {
        super("Couldn't get tokens!");
    }

    public TokenException(Throwable cause) {
        super(cause);
    }
}