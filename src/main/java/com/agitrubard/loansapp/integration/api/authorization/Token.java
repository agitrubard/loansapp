package com.agitrubard.loansapp.integration.api.authorization;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

public class Token {

    public static String getToken(String TOKEN_URL, String CLIENT_ID, String CLIENT_SECRET) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> result;
        result = restTemplate.exchange(TOKEN_URL, HttpMethod.POST, getTokenEntity(CLIENT_ID, CLIENT_SECRET), String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(result.getBody());
        JsonNode accessToken = root.path("access_token");
        JsonNode tokenType = root.path("token_type");

        return tokenType.asText() + " " + accessToken.asText();
    }

    private static HttpEntity<?> getTokenEntity(String CLIENT_ID, String CLIENT_SECRET) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("CONTENT_TYPE", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.add("Accept", "application/json");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", CLIENT_ID);
        body.add("client_secret", CLIENT_SECRET);
        body.add("grant_type", "client_credentials");
        body.add("scope", "oob");

        return new HttpEntity<>(body, headers);
    }
}