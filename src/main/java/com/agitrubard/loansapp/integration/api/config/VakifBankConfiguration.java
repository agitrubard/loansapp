package com.agitrubard.loansapp.integration.api.config;

public class VakifBankConfiguration {

    private final String LOAN_URL = "https://apigw.vakifbank.com.tr:8443/loanCalculator";
    private final String TOKEN_URL = "https://apigw.vakifbank.com.tr:8443/auth/oauth/v2/token";
    private final String CLIENT_ID = "l7xx70b66982084a48f9ab73f2904f0cd33c";
    private final String CLIENT_SECRET = "92612aaaaf584f18812025765bb7fa9f";

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