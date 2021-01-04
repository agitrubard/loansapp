package com.agitrubard.loansapp.integration.api.authorization;

import com.agitrubard.loansapp.domain.model.Constant;
import com.agitrubard.loansapp.domain.model.exception.TokenException;
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

import java.io.IOException;

@Slf4j
public class Token {

    public static String getToken(String TOKEN_URL, String CLIENT_ID, String CLIENT_SECRET) throws TokenException {
        log.info("Token Call Starting");

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> result;
        result = restTemplate.exchange(TOKEN_URL, HttpMethod.POST, getTokenEntity(CLIENT_ID, CLIENT_SECRET), String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root;
        try {
            root = mapper.readTree(result.getBody());
        } catch (IOException e) {
            throw new TokenException();
        }
        JsonNode accessToken = root.path(Constant.ACCESS_TOKEN);
        JsonNode tokenType = root.path(Constant.TOKEN_TYPE);

        return tokenType.asText() + " " + accessToken.asText();
    }

    private static HttpEntity<?> getTokenEntity(String CLIENT_ID, String CLIENT_SECRET) {
        log.info("Token Entity Call Starting");

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(Constant.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.add(Constant.ACCEPT, Constant.CONTENT_TYPE_APPLICATION_JSON);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add(Constant.CLIENT_ID, CLIENT_ID);
        body.add(Constant.CLIENT_SECRET, CLIENT_SECRET);
        body.add(Constant.GRANT_TYPE, Constant.GRANT_TYPE_VALUE);
        body.add(Constant.SCOPE, Constant.SCOPE_VALUE);

        return new HttpEntity<>(body, headers);
    }
}