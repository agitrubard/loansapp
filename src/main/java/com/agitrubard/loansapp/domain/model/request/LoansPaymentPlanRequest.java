package com.agitrubard.loansapp.domain.model.request;

import java.io.Serializable;
import java.util.Objects;

public class LoansPaymentPlanRequest implements Serializable {

    private static final long serialVersionUID = -8224036318459891490L;
    private int loanTerm;
    private int loanAmount;

    public int getLoanTerm() {
        return loanTerm;
    }

    public void setLoanTerm(int loanTerm) {
        this.loanTerm = loanTerm;
    }

    public int getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(int loanAmount) {
        this.loanAmount = loanAmount;
    }

    @Override
    public String toString() {
        return "LoansPaymentPlanRequest{" +
                "loanTerm=" + loanTerm +
                ", loanAmount=" + loanAmount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoansPaymentPlanRequest)) return false;
        LoansPaymentPlanRequest that = (LoansPaymentPlanRequest) o;
        return getLoanTerm() == that.getLoanTerm() &&
                getLoanAmount() == that.getLoanAmount();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLoanTerm(), getLoanAmount());
    }
}