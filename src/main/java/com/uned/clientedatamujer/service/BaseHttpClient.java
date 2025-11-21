package com.uned.clientedatamujer.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.uned.clientedatamujer.dto.ApiError;
import com.uned.clientedatamujer.service.util.AuthSession;
import com.uned.clientedatamujer.service.util.DataUtilities;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

public abstract class BaseHttpClient {
    protected final String URL = "https://api.datamujer.com";

    protected final HttpClient client = HttpClient.newBuilder()
                                                .connectTimeout(Duration.ofSeconds(15))
                                                .build();

    protected final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    protected ApiError noServerConnection(String url){
        return new ApiError(
                LocalDateTime.now(),
                502,
                "SERVICE_UNAVAILABLE",
                "No se pudo conectar con el servidor.",
                url,
                null
        );
    }

    protected ApiError failedErrorJsonLecture(String url){
        return new ApiError(
                LocalDateTime.now(),
                500,
                "UNEXPECTED",
                "Error inesperado",
                url,
                null
        );
    }

    protected HttpRequest buildRequest(
            String url,
            String method,
            String body,
            boolean auth
    ){
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(URL+url))
                .header("Content-Type", "application/json")
                .header("X-Client-Name", DataUtilities.CLIENT_NAME)
                .header("X-Client-Version", DataUtilities.CLIENT_VERSION);

        if (auth) {
            builder.header("AUTHORIZATION", "Bearer " + AuthSession.getAccessToken());
        }

        switch (method.toUpperCase()) {
            case "GET":
                builder.GET();
                break;
            case "POST":
                builder.POST(body == null ?
                        HttpRequest.BodyPublishers.noBody() :
                        HttpRequest.BodyPublishers.ofString(body));
                break;
            case "PUT":
                builder.PUT(body == null ?
                        HttpRequest.BodyPublishers.noBody() :
                        HttpRequest.BodyPublishers.ofString(body));
                break;
            case "DELETE":
                if (body == null) builder.DELETE();
                else builder.method("DELETE", HttpRequest.BodyPublishers.ofString(body));
                break;
            default:
                throw new IllegalArgumentException("Método HTTP no soportado: " + method);
        }
        return builder.build();
    }

    protected <T> Object sendRequest(HttpRequest request, Class<T> responseType){
        try{
            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
            if(response.statusCode() >= 200 && response.statusCode() < 300){
                try{
                    if(responseType.equals(Void.class)) return null;
                    if(responseType.equals(String.class)) return response.body();
                    return objectMapper.readValue(response.body(), responseType);
                }catch (Exception e){
                    return failedErrorJsonLecture(request.uri().toString());
                }
            }else{
                try{
                    return objectMapper.readValue(response.body(), ApiError.class);
                }catch (Exception e){
                    return failedErrorJsonLecture(request.uri().toString());
                }
            }
        }catch (IOException | InterruptedException e) {
            return noServerConnection(request.uri().toString());
        }
    }

    protected <T> Object sendRequest(HttpRequest request, TypeReference<T> responseType) {
        try{
            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
            if(response.statusCode() >= 200 && response.statusCode() < 300) {
                try {
                    return objectMapper.readValue(response.body(), responseType);
                } catch (Exception e) {
                    return failedErrorJsonLecture(request.uri().toString());
                }
            }else{
                try{
                    return objectMapper.readValue(response.body(), ApiError.class);
                }catch (Exception e) {
                    return failedErrorJsonLecture(request.uri().toString());
                }
            }

        }catch (IOException | InterruptedException e) {
            return noServerConnection(request.uri().toString());
        }
    }
}
