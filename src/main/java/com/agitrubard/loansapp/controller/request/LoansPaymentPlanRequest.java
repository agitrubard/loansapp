package com.agitrubard.loansapp.controller.request;

import java.io.Serializable;

public class LoansPaymentPlanRequest implements Serializable {

    private static final long serialVersionUID = -1736851129816161807L;
    private String categoryCode; //Kategori Kodu
    private String clientType; //Müşteri Tipi (Bireysel: 1, Ticari: 2)
    private int nop; //Kredi Vadesi (Ay)
    private double principal; //Kredi Tutarı (TL)

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public int getNop() {
        return nop;
    }

    public void setNop(int nop) {
        this.nop = nop;
    }

    public double getPrincipal() {
        return principal;
    }

    public void setPrincipal(double principal) {
        this.principal = principal;
    }

    @Override
    public String toString() {
        return "LoansPaymentPlanRequest{" +
                ", categoryCode='" + categoryCode + '\'' +
                ", clientType='" + clientType + '\'' +
                ", nop=" + nop +
                ", principal=" + principal +
                '}';
    }
}