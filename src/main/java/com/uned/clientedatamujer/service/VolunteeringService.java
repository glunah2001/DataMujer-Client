package com.uned.clientedatamujer.service;

import com.uned.clientedatamujer.dto.request.BaseVolunteeringRegisterDTO;
import com.uned.clientedatamujer.dto.request.VolunteeringWrapperDTO;
import com.uned.clientedatamujer.dto.response.VolunteeringDTO;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;

public class VolunteeringService extends BaseHttpClient{

    public Object createVolunteering(String accessJwt, BaseVolunteeringRegisterDTO dto) throws IOException {
        String url = URL + "/volunteering";

        String json = objectMapper.writeValueAsString(dto);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .header("AUTHORIZATION", "Bearer "+accessJwt)
                .build();

        return sendRequest(request, VolunteeringDTO.class);
    }

    public Object createVolunteering(String accessJwt, VolunteeringWrapperDTO dto) throws IOException {
        String url = URL + "/volunteering/multiple";

        String json = objectMapper.writeValueAsString(dto);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .header("AUTHORIZATION", "Bearer "+accessJwt)
                .build();

        return sendRequest(request, Void.class);
    }

}
