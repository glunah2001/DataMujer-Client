package com.uned.clientedatamujer.service;

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

}
