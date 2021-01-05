package com.agitrubard.loansapp.domain.model.response.constant;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class VakifBankConstant {

    public static final String COMMA = ",";
    public static final String CURRENCY_RATES_ENTITY_PARAMETERS = "{    \"ValidityDate\": \"" + getLocalDateTime() + "\"}";
    public static final String DATA = "Data";
    public static final String GRACE_PERIOD = "GracePeriod";
    public static final String GRACE_PERIOD_VALUE_0 = "0";
    public static final String INSTALLMENT_AMOUNT = "InstallmentAmount";
    public static final String INSTALLMENT_PERIOD = "InstallmentPeriod";
    public static final String INSTALLMENT_PERIOD_VALUE_1 = "1";
    public static final String INTEREST_RATE = "InterestRate";
    public static final String LOAN = "Loan";
    public static final String LOAN_AMOUNT = "LoanAmount";
    public static final String LOAN_PRODUCT_ID = "LoanProductId";
    public static final String LOAN_PRODUCT_ID_VALUE_41001 = "41001";
    public static final String LOAN_TERM = "LoanTerm";
    public static final String TOTAL_AMOUNT = "TotalAmount";

    public static String getLocalDateTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", new Locale("tr", "TR"));
        return simpleDateFormat.format(new Date());
    }
}