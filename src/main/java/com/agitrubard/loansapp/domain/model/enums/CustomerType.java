package com.agitrubard.loansapp.domain.model.enums;

public enum CustomerType {

    UNKNOWN(0),
    INDIVIDUAL(1),
    CORPORATE(2);

    private final int value;

    CustomerType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}