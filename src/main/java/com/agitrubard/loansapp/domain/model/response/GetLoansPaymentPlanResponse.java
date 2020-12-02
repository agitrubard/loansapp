package com.agitrubard.loansapp.domain.model.response;

import com.agitrubard.loansapp.domain.model.enums.BankName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetLoansPaymentPlanResponse {

    private BankName bankName;
    private double intRate;
    private double totalInterest;
    private double monthlyCostRate;
    private double installmentAmount;
    private double totalPaymentAmount;
}