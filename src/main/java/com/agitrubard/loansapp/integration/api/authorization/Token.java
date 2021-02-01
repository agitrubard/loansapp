package com.agitrubard.loansapp.integration.api.authorization;

import com.agitrubard.loansapp.domain.model.enums.BankName;
import com.agitrubard.loansapp.domain.model.exception.TokenException;
import com.agitrubard.loansapp.domain.model.response.constant.CustomConstant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class Token {

    public static String getAccessToken(BankName bankName, String TOKEN_URL, String CLIENT_ID, String CLIENT_SECRET) throws TokenException {
        log.info(bankName + " Token Call Starting");

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> result = null;

        try {
            result = restTemplate.exchange(TOKEN_URL, HttpMethod.POST, createTokenEntity(bankName, CLIENT_ID, CLIENT_SECRET), String.class);
        } catch (Exception e) {
            log.error(bankName + " Token Unknown Host Exception!");
        }

        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode root = mapper.readTree(result.getBody());
            JsonNode accessToken = root.path(CustomConstant.ACCESS_TOKEN);
            JsonNode tokenType = root.path(CustomConstant.TOKEN_TYPE);

            return tokenType.asText() + " " + accessToken.asText();
        } catch (JsonProcessingException e) {
            throw new TokenException();
        } catch (NullPointerException e) {
            log.error(bankName + " Token Null Pointer Exception!");
            return null;
        }
    }

    private static HttpEntity<?> createTokenEntity(BankName bankName, String CLIENT_ID, String CLIENT_SECRET) {
        log.info(bankName + " Token Entity Call Starting");

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(CustomConstant.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.add(CustomConstant.ACCEPT, CustomConstant.CONTENT_TYPE_APPLICATION_JSON);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add(CustomConstant.CLIENT_ID, CLIENT_ID);
        body.add(CustomConstant.CLIENT_SECRET, CLIENT_SECRET);
        body.add(CustomConstant.GRANT_TYPE, CustomConstant.GRANT_TYPE_VALUE);
        body.add(CustomConstant.SCOPE, CustomConstant.SCOPE_VALUE);

        return new HttpEntity<>(body, headers);
    }
}