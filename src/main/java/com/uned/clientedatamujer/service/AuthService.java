package com.uned.clientedatamujer.service;

import com.uned.clientedatamujer.dto.authentication.ResetPasswordDTO;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;

public class AuthService extends BaseHttpClient{

    public Object forgotPassword(String email) {
        String url = URL + "/auth/forgot-password?email="+email;
        HttpRequest request = HttpRequest.newBuilder()
                                         .uri(URI.create(url))
                                         .POST(HttpRequest.BodyPublishers.noBody())
                                         .header("Content-Type", "Application/json")
                                         .build();

        return sendRequest(request, String.class);
    }

    public Object resetPassword(ResetPasswordDTO dto) throws IOException {
        String url = URL + "/auth/reset-password";
        String json = objectMapper.writeValueAsString(dto);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "Application/json")
                .build();

        return sendRequest(request, String.class);
    }
}
