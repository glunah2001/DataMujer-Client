package com.uned.clientedatamujer.service;

import com.uned.clientedatamujer.dto.request.ActivityRegisterDTO;
import com.uned.clientedatamujer.dto.response.ActivityDTO;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;

public class ActivityService extends BaseHttpClient{

    public Object postActivity(ActivityRegisterDTO dto, String accessJwt) throws IOException {
        String url = URL + "/activity";

        String json = objectMapper.writeValueAsString(dto);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "Application/json")
                .header("AUTHORIZATION", "Bearer "+accessJwt)
                .build();

        return sendRequest(request, ActivityDTO.class);
    }

}
