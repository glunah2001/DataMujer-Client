package com.uned.clientedatamujer.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.uned.clientedatamujer.dto.SimplePage;
import com.uned.clientedatamujer.dto.request.BaseVolunteeringRegisterDTO;
import com.uned.clientedatamujer.dto.request.VolunteeringUpdateDTO;
import com.uned.clientedatamujer.dto.request.VolunteeringWrapperDTO;
import com.uned.clientedatamujer.dto.response.ProfileDTO;
import com.uned.clientedatamujer.dto.response.VolunteeringDTO;
import com.uned.clientedatamujer.service.util.AuthSession;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;

public class VolunteeringService extends BaseHttpClient{

    public Object getVolunteeringById(String id) {
        try{
            var request = buildRequest(
                    "/volunteering?id="+id,
                    "GET",
                    null,
                    true
            );
            return sendRequest(request, VolunteeringDTO.class);
        }catch(IllegalArgumentException e){
            return failedErrorJsonLecture("/");
        }
    }

    public Object getMyPendingVolunteering(int page) {
        try{
            var request = buildRequest(
                    "/volunteering/me?page="+page,
                    "GET",
                    null,
                    true
            );
            return sendRequest(request, new TypeReference<SimplePage<VolunteeringDTO>>() {});
        }catch(IllegalArgumentException e){
            return failedErrorJsonLecture("/");
        }
    }

    public Object getVolunteeringInActivity(String activityId, int page) {
        try{
            var request = buildRequest(
                    "/volunteering/InActivity?activityId="+activityId+"&page="+page,
                    "GET",
                    null,
                    true
            );
            return sendRequest(request, new TypeReference<SimplePage<VolunteeringDTO>>() {});
        }catch(IllegalArgumentException e){
            return failedErrorJsonLecture("/");
        }
    }

    public Object createVolunteering(BaseVolunteeringRegisterDTO dto) throws IOException {
        String json = objectMapper.writeValueAsString(dto);
        try{
            var request = buildRequest(
                    "/volunteering",
                    "POST",
                    json,
                    true
            );
            return sendRequest(request, VolunteeringDTO.class);
        }catch(IllegalArgumentException e){
            return failedErrorJsonLecture("/");
        }
    }

    public Object createVolunteering(VolunteeringWrapperDTO dto) throws IOException {
        String json = objectMapper.writeValueAsString(dto);
        try{
            var request = buildRequest(
                    "/volunteering/multiple",
                    "POST",
                    json,
                    true
            );
            return sendRequest(request, Void.class);
        }catch(IllegalArgumentException e){
            return failedErrorJsonLecture("/");
        }
    }

    public Object deleteVolunteering(String id){
        try{
            var request = buildRequest(
                    "/volunteering?id="+id,
                    "DELETE",
                    null,
                    true
            );
            return sendRequest(request, Void.class);
        }catch(IllegalArgumentException e){
            return failedErrorJsonLecture("/");
        }
    }

    public Object updateVolunteering(VolunteeringUpdateDTO dto, String id) throws IOException{
        String json = objectMapper.writeValueAsString(dto);

        try{
            var request = buildRequest(
                    "/volunteering?id="+id,
                    "PUT",
                    json,
                    true
            );
            return sendRequest(request, VolunteeringDTO.class);
        }catch(IllegalArgumentException e){
            return failedErrorJsonLecture("/");
        }
    }
}
