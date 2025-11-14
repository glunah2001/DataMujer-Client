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
        try{
            var request = buildRequest(
                    "/auth/forgot-password?email="+email,
                    "POST",
                    null,
                    false
            );
            return sendRequest(request, String.class);
        }catch(IllegalArgumentException e){
            return failedErrorJsonLecture("/");
        }
    }

    public Object resetPassword(ResetPasswordDTO dto) throws IOException {
        String json = objectMapper.writeValueAsString(dto);

        try{
            var request = buildRequest(
                    "/auth/reset-password",
                    "POST",
                    json,
                    false
            );
            return sendRequest(request, String.class);
        }catch(IllegalArgumentException e){
            return failedErrorJsonLecture("/");
        }
    }

    public Object login(UserLoginDTO dto) throws IOException{
        String json = objectMapper.writeValueAsString(dto);

        try{
            var request = buildRequest(
                    "/auth/login",
                    "POST",
                    json,
                    false
            );
            return sendRequest(request, TokenResponse.class);
        }catch(IllegalArgumentException e){
            return failedErrorJsonLecture("/");
        }
    }

    public Object refresh() {
        String url = URL + "/auth/refresh";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.noBody())
                .header("Content-Type", "application/json")
                .header("AUTHORIZATION", "Bearer "+ AuthSession.getRefreshToken())
                .header("X-Client-Name", DataUtilities.CLIENT_NAME)
                .header("X-Client-Version", DataUtilities.CLIENT_VERSION)
                .build();
        return sendRequest(request, TokenResponse.class);
    }

    public void logout(){
        try{
            var request = buildRequest(
                    "/auth/logout",
                    "POST",
                    null,
                    true
            );

            AuthSession.clear();
            DataUtilities.clearAll();

            sendRequest(request, Void.class);
        }catch(IllegalArgumentException _){
            AuthSession.clear();
            DataUtilities.clearAll();
        }
    }
}
