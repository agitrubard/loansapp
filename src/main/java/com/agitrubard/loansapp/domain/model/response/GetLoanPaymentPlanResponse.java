package com.agitrubard.loansapp.domain.model.response;

import com.agitrubard.loansapp.domain.model.enums.BankName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetLoanPaymentPlanResponse {

    private BankName bankName;
    private Double intRate;
    private Double totalInterest;
    private Double monthlyCostRate;
    private Double installmentAmount;
    private Double totalPaymentAmount;
}