package com.agitrubard.loansapp.integration.api.config;

public class YapiKrediConfiguration {

    private final String LOAN_URL = "https://api.yapikredi.com.tr/api/credit/calculation/v1/loanPaymentPlan";
    private final String TOKEN_URL = "https://api.yapikredi.com.tr/auth/oauth/v2/token";
    private final String CLIENT_ID = "l7xx179417aa99e34db683a93e9eac411885";
    private final String CLIENT_SECRET = "f8c96815056b48d7992fc4221393ddcf";

    public String getLOAN_URL() {
        return LOAN_URL;
    }

    public String getTOKEN_URL() {
        return TOKEN_URL;
    }

    public String getCLIENT_ID() {
        return CLIENT_ID;
    }

    public String getCLIENT_SECRET() {
        return CLIENT_SECRET;
    }
}