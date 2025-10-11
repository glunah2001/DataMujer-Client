package com.uned.clientedatamujer.service;

import com.uned.clientedatamujer.dto.ApiError;
import com.uned.clientedatamujer.dto.authentication.ResetPasswordDTO;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

public class AuthService extends BaseHttpClient{

    public Object forgotPassword(String email) {
        String url = super.URL + "/auth/forgot-password?email="+email;

        HttpRequest request = HttpRequest.newBuilder()
                                         .uri(URI.create(url))
                                         .POST(HttpRequest.BodyPublishers.noBody())
                                         .header("Content-Type", "Application/json")
                                         .build();
        try {
            HttpResponse<String> response = super.client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return response.body();
            } else {
                try {
                    return super.objectMapper.readValue(response.body(), ApiError.class);
                } catch(Exception e) {
                    return new ApiError(
                            LocalDateTime.now(),
                            500,
                            "UNEXPECTED",
                            "Error inesperado",
                            url,
                            null
                    );
                }
            }
        }catch(IOException | InterruptedException e){
            return super.noServerConnection(url);
        }
    }

    public Object resetPassword(String token, String password) throws IOException {
        String url = super.URL + "/auth/reset-password";

        ResetPasswordDTO dto = new ResetPasswordDTO(token, password);

        String json = super.objectMapper.writeValueAsString(dto);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "Application/json")
                .build();

        try{
            HttpResponse<String> response = super.client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if(response.statusCode() >= 200 && response.statusCode() < 300) {
                return response.body();
            }else{
                try{
                    return super.objectMapper.readValue(response.body(), ApiError.class);
                }catch(Exception e) {
                    return new ApiError(
                            LocalDateTime.now(),
                            500,
                            "UNEXPECTED",
                            "Error inesperado",
                            url,
                            null
                    );
                }
            }

        }catch(IOException | InterruptedException e){
            return super.noServerConnection(url);
        }
    }
}
