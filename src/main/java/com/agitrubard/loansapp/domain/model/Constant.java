package com.agitrubard.loansapp.domain.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class Constant {

    public static final String ACCEPT = "Accept";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String AUTHORIZATION = "Authorization";
    public static final String CLIENT_ID = "client_id";
    public static final String CLIENT_SECRET = "client_secret";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";
    public static final String GRANT_TYPE = "grant_type";
    public static final String GRANT_TYPE_VALUE = "client_credentials";
    public static final String SCOPE = "scope";
    public static final String SCOPE_VALUE = "oob";
    public static final String TOKEN_TYPE = "token_type";
    public static final String VAKIFBANK_CURRENCY_RATES_ENTITY_PARAMETERS = "{    \"ValidityDate\": \"" + getLocalDateTime() + "\"}";
    public static final String VAKIFBANK_DATA = "Data";
    public static final String VAKIFBANK_GRACE_PERIOD = "GracePeriod";
    public static final String VAKIFBANK_GRACE_PERIOD_VALUE_0 = "0";
    public static final String VAKIFBANK_INSTALLMENT_AMOUNT = "InstallmentAmount";
    public static final String VAKIFBANK_INSTALLMENT_PERIOD = "InstallmentPeriod";
    public static final String VAKIFBANK_INSTALLMENT_PERIOD_VALUE_1 = "1";
    public static final String VAKIFBANK_INTEREST_RATE = "InterestRate";
    public static final String VAKIFBANK_LOAN = "Loan";
    public static final String VAKIFBANK_LOAN_AMOUNT = "LoanAmount";
    public static final String VAKIFBANK_LOAN_PRODUCT_ID = "LoanProductId";
    public static final String VAKIFBANK_LOAN_PRODUCT_ID_VALUE_41001 = "41001";
    public static final String VAKIFBANK_LOAN_TERM = "LoanTerm";
    public static final String VAKIFBANK_TOTAL_AMOUNT = "TotalAmount";
    public static final String YAPIKREDI_BRANCH_CODE = "branchCode";
    public static final String YAPIKREDI_BRANCH_CODE_VALUE_925 = "925";
    public static final String YAPIKREDI_CATEGORY_CODE = "categoryCode";
    public static final String YAPIKREDI_CATEGORY_CODE_VALUE_W9 = "W9";
    public static final String YAPIKREDI_CHANNEL_CODE = "channelCode";
    public static final String YAPIKREDI_CHANNEL_CODE_VALUE_OPN = "OPN";
    public static final String YAPIKREDI_CLIENT_TYPE = "clientType";
    public static final String YAPIKREDI_CLIENT_TYPE_VALUE_1 = "1";
    public static final String YAPIKREDI_INSTALLMENT_AMOUNT = "installmentAmount";
    public static final String YAPIKREDI_INSTALLMENT_LIST = "installmentList";
    public static final String YAPIKREDI_INT_RATE = "intRate";
    public static final String YAPIKREDI_MONTHLY_COST_RATE = "monthlyCostRate";
    public static final String YAPIKREDI_NOP = "nop";
    public static final String YAPIKREDI_PRINCIPAL = "principal";
    public static final String YAPIKREDI_RESPONSE = "response";
    public static final String YAPIKREDI_RETURN = "return";
    public static final String YAPIKREDI_TOTAL_INTEREST = "totalInterest";
    public static final String YAPIKREDI_TOTAL_PAYMENT_AMOUNT = "totalPaymentAmount";

    public static String getLocalDateTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", new Locale("tr", "TR"));
        return simpleDateFormat.format(new Date());
    }
}