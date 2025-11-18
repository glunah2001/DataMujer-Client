package com.uned.clientedatamujer.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.uned.clientedatamujer.dto.SimplePage;
import com.uned.clientedatamujer.dto.request.ParticipationWrapperDTO;
import com.uned.clientedatamujer.dto.response.ActivityDTO;
import com.uned.clientedatamujer.dto.response.ParticipationDTO;
import com.uned.clientedatamujer.service.util.AuthSession;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;

public class ParticipationService extends BaseHttpClient{

    public Object createParticipation(long activityId){
        try{
            var request = buildRequest(
                    "/participation?activityId="+activityId,
                    "POST",
                    null,
                    true
            );
            return sendRequest(request, ParticipationDTO.class);
        }catch(IllegalArgumentException e){
            return failedErrorJsonLecture("/");
        }
    }

    public Object createParticipations(ParticipationWrapperDTO dto) throws IOException {
        String json = objectMapper.writeValueAsString(dto);
        try{
            var request = buildRequest(
                    "/participation/multiple",
                    "POST",
                    json,
                    true
            );
            return sendRequest(request, Void.class);
        }catch(IllegalArgumentException e){
            return failedErrorJsonLecture("/");
        }
    }

    public Object getMyParticipation(int page){
        try{
            var request = buildRequest(
                    "/participation/me?page="+page,
                    "GET",
                    null,
                    true
            );
            return sendRequest(request, new TypeReference<SimplePage<ParticipationDTO>>() {});
        }catch(IllegalArgumentException e){
            return failedErrorJsonLecture("/");
        }
    }

    public Object getParticipationById(String id) {
        try{
            var request = buildRequest(
                    "/participation?id="+id,
                    "GET",
                    null,
                    true
            );
            return sendRequest(request, ParticipationDTO.class);
        }catch(IllegalArgumentException e){
            return failedErrorJsonLecture("/");
        }
    }

    public Object getParticipationInActivity(String activityId, int page) {
        try{
            var request = buildRequest(
                    "/participation/InActivity?activityId="+activityId+"&page="+page,
                    "GET",
                    null,
                    true
            );
            return sendRequest(request, new TypeReference<SimplePage<ParticipationDTO>>() {});
        }catch(IllegalArgumentException e){
            return failedErrorJsonLecture("/");
        }
    }

    public Object deleteParticipation(String id) {
        try{
            var request = buildRequest(
                    "/participation?id="+id,
                    "DELETE",
                    null,
                    true
            );
            return sendRequest(request, Void.class);
        }catch(IllegalArgumentException e){
            return failedErrorJsonLecture("/");
        }
    }

    public Object startParticipation(String id) {
        try{
            var request = buildRequest(
                    "/participation/start?id="+id,
                    "PUT",
                    null,
                    true
            );
            return sendRequest(request, ParticipationDTO.class);
        }catch(IllegalArgumentException e){
            return failedErrorJsonLecture("/");
        }
    }

    public Object cancelParticipation(String id) {
        try{
            var request = buildRequest(
                    "/participation/cancel?id="+id,
                    "PUT",
                    null,
                    true
            );
            return sendRequest(request, ParticipationDTO.class);
        }catch(IllegalArgumentException e){
            return failedErrorJsonLecture("/");
        }
    }
}
