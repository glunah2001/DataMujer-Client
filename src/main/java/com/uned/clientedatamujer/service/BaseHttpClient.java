package com.uned.clientedatamujer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.uned.clientedatamujer.dto.ApiError;

import java.net.http.HttpClient;
import java.time.Duration;
import java.time.LocalDateTime;

public abstract class BaseHttpClient {
    protected final String URL = "http://localhost:8080";

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
}
