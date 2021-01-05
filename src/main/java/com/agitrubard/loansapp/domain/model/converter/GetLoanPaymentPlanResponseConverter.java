package com.agitrubard.loansapp.domain.model.converter;

import com.agitrubard.loansapp.domain.model.enums.BankName;
import com.agitrubard.loansapp.domain.model.response.GetLoanPaymentPlanResponse;

public class GetLoanPaymentPlanResponseConverter {

    private GetLoanPaymentPlanResponseConverter() {
    }

    public static GetLoanPaymentPlanResponse convert(BankName bankName, Double intRate, Double totalInterest, Double monthlyCostRate, Double installmentAmount, Double totalPaymentAmount) {
        GetLoanPaymentPlanResponse getLoanPaymentPlanResponse = new GetLoanPaymentPlanResponse();

        getLoanPaymentPlanResponse.setBankName(bankName);
        getLoanPaymentPlanResponse.setIntRate(intRate);
        getLoanPaymentPlanResponse.setTotalInterest(totalInterest);
        getLoanPaymentPlanResponse.setMonthlyCostRate(monthlyCostRate);
        getLoanPaymentPlanResponse.setInstallmentAmount(installmentAmount);
        getLoanPaymentPlanResponse.setTotalPaymentAmount(totalPaymentAmount);

        return getLoanPaymentPlanResponse;
    }
}