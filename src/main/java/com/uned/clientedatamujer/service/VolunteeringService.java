package com.uned.clientedatamujer.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.uned.clientedatamujer.dto.SimplePage;
import com.uned.clientedatamujer.dto.request.BaseVolunteeringRegisterDTO;
import com.uned.clientedatamujer.dto.request.VolunteeringUpdateDTO;
import com.uned.clientedatamujer.dto.request.VolunteeringWrapperDTO;
import com.uned.clientedatamujer.dto.response.VolunteeringDTO;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;

public class VolunteeringService extends BaseHttpClient{

    public Object getVolunteeringById(String id) {
        String url = URL + "/volunteering?id="+id;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("Content-Type", "application/json")
                .header("AUTHORIZATION", "Bearer "+AuthSession.getAccessToken())
                .build();

        return sendRequest(request, VolunteeringDTO.class);
    }

    public Object getMyPendingVolunteering(int page) {
        String url = URL + "/volunteering/me?page="+page;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("Content-Type", "application/json")
                .header("AUTHORIZATION", "Bearer "+AuthSession.getAccessToken())
                .build();

        return sendRequest(request, new TypeReference<SimplePage<VolunteeringDTO>>() {});
    }

    public Object getVolunteeringInActivity(String activityId, int page) {
        String url = URL + "/volunteering/InActivity?activityId="+activityId+"&page="+page;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("Content-Type", "application/json")
                .header("AUTHORIZATION", "Bearer "+AuthSession.getAccessToken())
                .build();

        return sendRequest(request, new TypeReference<SimplePage<VolunteeringDTO>>() {});
    }

    public Object createVolunteering(BaseVolunteeringRegisterDTO dto) throws IOException {
        String url = URL + "/volunteering";

        String json = objectMapper.writeValueAsString(dto);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .header("AUTHORIZATION", "Bearer "+AuthSession.getAccessToken())
                .build();

        return sendRequest(request, VolunteeringDTO.class);
    }

    public Object createVolunteering(VolunteeringWrapperDTO dto) throws IOException {
        String url = URL + "/volunteering/multiple";

        String json = objectMapper.writeValueAsString(dto);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .header("AUTHORIZATION", "Bearer "+AuthSession.getAccessToken())
                .build();

        return sendRequest(request, Void.class);
    }

    public Object deleteVolunteering(String id){
        String url = URL + "/volunteering?id="+id;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .DELETE()
                .header("Content-Type", "application/json")
                .header("AUTHORIZATION", "Bearer "+AuthSession.getAccessToken())
                .build();

        return sendRequest(request, Void.class);
    }

    public Object updateVolunteering(VolunteeringUpdateDTO dto, String id) throws IOException{
        String url = URL + "/volunteering?id="+id;

        String json = objectMapper.writeValueAsString(dto);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .header("AUTHORIZATION", "Bearer "+AuthSession.getAccessToken())
                .build();

        return sendRequest(request, VolunteeringDTO.class);
    }
}
