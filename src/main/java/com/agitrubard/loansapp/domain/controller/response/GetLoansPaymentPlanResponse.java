package com.agitrubard.loansapp.domain.controller.response;

import com.agitrubard.loansapp.domain.model.enums.BankName;

public class GetLoansPaymentPlanResponse {

    private BankName bankName;
    private double intRate;
    private double totalInterest;
    private double monthlyCostRate;
    private double installmentAmount;
    private double totalPaymentAmount;

    public BankName getBankName() {
        return bankName;
    }

    public void setBankName(BankName bankName) {
        this.bankName = bankName;
    }

    public double getIntRate() {
        return intRate;
    }

    public void setIntRate(double intRate) {
        this.intRate = intRate;
    }

    public double getTotalInterest() {
        return totalInterest;
    }

    public void setTotalInterest(double totalInterest) {
        this.totalInterest = totalInterest;
    }

    public double getMonthlyCostRate() {
        return monthlyCostRate;
    }

    public void setMonthlyCostRate(double monthlyCostRate) {
        this.monthlyCostRate = monthlyCostRate;
    }

    public double getInstallmentAmount() {
        return installmentAmount;
    }

    public void setInstallmentAmount(double installmentAmount) {
        this.installmentAmount = installmentAmount;
    }

    public double getTotalPaymentAmount() {
        return totalPaymentAmount;
    }

    public void setTotalPaymentAmount(double totalPaymentAmount) {
        this.totalPaymentAmount = totalPaymentAmount;
    }

    @Override
    public String toString() {
        return "GetLoansPaymentPlanResponse{" +
                "bankName=" + bankName +
                ", intRate=" + intRate +
                ", totalInterest=" + totalInterest +
                ", monthlyCostRate=" + monthlyCostRate +
                ", installmentAmount=" + installmentAmount +
                ", totalPaymentAmount=" + totalPaymentAmount +
                '}';
    }
}