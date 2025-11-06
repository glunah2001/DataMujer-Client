package com.uned.clientedatamujer.service;

import com.uned.clientedatamujer.dto.authentication.ResetPasswordDTO;
import com.uned.clientedatamujer.dto.authentication.UserLoginDTO;
import com.uned.clientedatamujer.dto.token.TokenResponse;
import com.uned.clientedatamujer.service.util.AuthSession;
import com.uned.clientedatamujer.service.util.DataUtilities;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;

public class AuthService extends BaseHttpClient{

    public Object forgotPassword(String email) {
        String url = URL + "/auth/forgot-password?email="+email;
        HttpRequest request = HttpRequest.newBuilder()
                                         .uri(URI.create(url))
                                         .POST(HttpRequest.BodyPublishers.noBody())
                                         .header("Content-Type", "application/json")
                                         .build();

        return sendRequest(request, String.class);
    }

    public Object resetPassword(ResetPasswordDTO dto) throws IOException {
        String url = URL + "/auth/reset-password";
        String json = objectMapper.writeValueAsString(dto);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();

        return sendRequest(request, String.class);
    }

    public Object login(UserLoginDTO dto) throws IOException{
        String url = URL + "/auth/login";
        String json = objectMapper.writeValueAsString(dto);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();
        return sendRequest(request, TokenResponse.class);
    }

    public Object refresh() {
        String url = URL + "/auth/refresh";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.noBody())
                .header("Content-Type", "application/json")
                .header("AUTHORIZATION", "Bearer "+ AuthSession.getRefreshToken())
                .build();
        return sendRequest(request, TokenResponse.class);
    }

    public void logout(){
        String url = URL + "/auth/logout";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.noBody())
                .header("Content-Type", "application/json")
                .header("AUTHORIZATION", "Bearer "+AuthSession.getAccessToken())
                .build();

        AuthSession.clear();
        DataUtilities.clearAll();

        sendRequest(request, Void.class);
    }
}
