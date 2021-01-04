package com.agitrubard.loansapp.domain.model.converter;

import com.agitrubard.loansapp.domain.model.enums.BankName;
import com.agitrubard.loansapp.domain.model.response.GetLoansPaymentPlanResponse;

public class GetLoansPaymentPlanResponseConverter {

    private GetLoansPaymentPlanResponseConverter() {
    }

    public static GetLoansPaymentPlanResponse convert(BankName bankName, Double intRate, Double totalInterest, Double monthlyCostRate, Double installmentAmount, Double totalPaymentAmount) {
        GetLoansPaymentPlanResponse getLoansPaymentPlanResponse = new GetLoansPaymentPlanResponse();

        getLoansPaymentPlanResponse.setBankName(bankName);
        getLoansPaymentPlanResponse.setIntRate(intRate);
        getLoansPaymentPlanResponse.setTotalInterest(totalInterest);
        getLoansPaymentPlanResponse.setMonthlyCostRate(monthlyCostRate);
        getLoansPaymentPlanResponse.setInstallmentAmount(installmentAmount);
        getLoansPaymentPlanResponse.setTotalPaymentAmount(totalPaymentAmount);

        return getLoansPaymentPlanResponse;
    }
}