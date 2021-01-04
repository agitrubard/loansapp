package com.agitrubard.loansapp.domain.model.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class LoansPaymentPlanRequest implements Serializable {

    private static final long serialVersionUID = -8224036318459891490L;
    @NotNull
    private Integer loanTerm;
    @NotNull
    private Integer loanAmount;
}