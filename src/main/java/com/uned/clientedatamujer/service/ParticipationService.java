package com.uned.clientedatamujer.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.uned.clientedatamujer.dto.SimplePage;
import com.uned.clientedatamujer.dto.response.ParticipationDTO;

import java.net.URI;
import java.net.http.HttpRequest;

public class ParticipationService extends BaseHttpClient{

    public Object createParticipation(String accessJwt, long activityId){
        String url = URL + "/participation?activityId="+activityId;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.noBody())
                .header("Content-Type", "application/json")
                .header("AUTHORIZATION", "Bearer "+accessJwt)
                .build();

        return sendRequest(request, ParticipationDTO.class);
    }

    public Object getMyParticipation(String accessJwt, int page){
        String url = URL + "/participation/me?page="+page;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("Content-Type", "application/json")
                .header("AUTHORIZATION", "Bearer "+accessJwt)
                .build();

        return sendRequest(request, new TypeReference<SimplePage<ParticipationDTO>>() {});
    }

    public Object getParticipationById(String accessJwt, String id) {
        String url = URL + "/participation?id="+id;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("Content-Type", "application/json")
                .header("AUTHORIZATION", "Bearer " + accessJwt)
                .build();

        return sendRequest(request, ParticipationDTO.class);
    }

    public Object getParticipationInActivity(String accessJwt, String activityId, int page) {
        String url = URL + "/participation/InActivity?activityId="+activityId+"&page="+page;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("Content-Type", "application/json")
                .header("AUTHORIZATION", "Bearer "+accessJwt)
                .build();

        return sendRequest(request, new TypeReference<SimplePage<ParticipationDTO>>() {});
    }

    public Object deleteParticipation(String accessJwt, String id) {
        String url = URL + "/participation?id="+id;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .DELETE()
                .header("Content-Type", "application/json")
                .header("AUTHORIZATION", "Bearer "+accessJwt)
                .build();

        return sendRequest(request, Void.class);
    }

    public Object startParticipation(String accessJwt, String id) {
        String url = URL + "/participation/start?id="+id;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .PUT(HttpRequest.BodyPublishers.noBody())
                .header("Content-Type", "application/json")
                .header("AUTHORIZATION", "Bearer "+accessJwt)
                .build();

        return sendRequest(request, Void.class);
    }

    public Object cancelParticipation(String accessJwt, String id) {
        String url = URL + "/participation/cancel?id="+id;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .PUT(HttpRequest.BodyPublishers.noBody())
                .header("Content-Type", "application/json")
                .header("AUTHORIZATION", "Bearer "+accessJwt)
                .build();

        return sendRequest(request, Void.class);
    }
}
