package com.agitrubard.loansapp.service.impl;

import com.agitrubard.loansapp.controller.request.LoansPaymentPlanRequest;
import com.agitrubard.loansapp.service.LoansService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class LoansServiceImpl implements LoansService {

    @Override
    public StringBuilder getLoansPaymentPlan(LoansPaymentPlanRequest loansPaymentPlanRequest) throws IOException {
        URL url = new URL("https://api.yapikredi.com.tr/api/credit/calculation/v1/loanPaymentPlan");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer 7e28db6d-9b66-492d-9302-bfe6bead0b0a");
        conn.setRequestProperty("Content-Type", "application/json");
        String inputs = "{  \"branchCode\": \"925\"," +
                "\"categoryCode\": \""+ loansPaymentPlanRequest.getCategoryCode() +
                "\",  \"channelCode\": \"OPN\"," +
                "\"clientType\": \""+ loansPaymentPlanRequest.getClientType() +
                "\",  \"nop\": "+ loansPaymentPlanRequest.getNop() +"," +
                "\"principal\": "+ loansPaymentPlanRequest.getPrincipal() +"}";
        byte[] body = inputs.getBytes();
        conn.setFixedLengthStreamingMode(body.length);
        conn.setDoOutput(true);

        OutputStream out = conn.getOutputStream();
        out.write(body);
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }
        rd.close();
        conn.disconnect();
        return sb;
    }
}