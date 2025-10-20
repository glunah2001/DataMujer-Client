package com.uned.clientedatamujer.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.uned.clientedatamujer.dto.SimplePage;
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
                .header("Content-Type", "application/json")
                .header("AUTHORIZATION", "Bearer "+accessJwt)
                .build();

        return sendRequest(request, ActivityDTO.class);
    }

    public Object getActivityById(String accessJwt, String id) {
        String url = URL + "/activity?id="+id;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("Content-Type", "application/json")
                .header("AUTHORIZATION", "Bearer " + accessJwt)
                .build();

        return sendRequest(request, ActivityDTO.class);
    }

    public Object getNonFinishedActivities(String accessJwt, int page) {
        String url = URL + "/activity/all?page="+page;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("Content-Type", "application/json")
                .header("AUTHORIZATION", "Bearer "+accessJwt)
                .build();

        return sendRequest(request, new TypeReference<SimplePage<ActivityDTO>>() {});
    }
}
