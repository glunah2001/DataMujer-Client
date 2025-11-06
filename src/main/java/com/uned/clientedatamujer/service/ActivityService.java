package com.uned.clientedatamujer.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.uned.clientedatamujer.dto.SimplePage;
import com.uned.clientedatamujer.dto.request.ActivityRegisterDTO;
import com.uned.clientedatamujer.dto.response.ActivityDTO;
import com.uned.clientedatamujer.service.util.AuthSession;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;

public class ActivityService extends BaseHttpClient{

    public Object postActivity(ActivityRegisterDTO dto) throws IOException {
        String url = URL + "/activity";

        String json = objectMapper.writeValueAsString(dto);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .header("AUTHORIZATION", "Bearer "+ AuthSession.getAccessToken())
                .build();

        return sendRequest(request, ActivityDTO.class);
    }

    public Object getActivityById(String id) {
        String url = URL + "/activity?id="+id;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("Content-Type", "application/json")
                .header("AUTHORIZATION", "Bearer "+AuthSession.getAccessToken())
                .build();

        return sendRequest(request, ActivityDTO.class);
    }

    public Object getNonFinishedActivities(int page) {
        String url = URL + "/activity/all?page="+page;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("Content-Type", "application/json")
                .header("AUTHORIZATION", "Bearer "+AuthSession.getAccessToken())
                .build();

        return sendRequest(request, new TypeReference<SimplePage<ActivityDTO>>() {});
    }

    public Object deleteActivity(String id){
        String url = URL + "/activity?id="+id;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .DELETE()
                .header("Content-Type", "application/json")
                .header("AUTHORIZATION", "Bearer "+AuthSession.getAccessToken())
                .build();

        return sendRequest(request, Void.class);
    }

}
