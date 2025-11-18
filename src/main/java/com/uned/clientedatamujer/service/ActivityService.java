package com.uned.clientedatamujer.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.uned.clientedatamujer.dto.SimplePage;
import com.uned.clientedatamujer.dto.request.ActivityRegisterDTO;
import com.uned.clientedatamujer.dto.response.ActivityDTO;
import com.uned.clientedatamujer.service.util.AuthSession;
import com.uned.clientedatamujer.service.util.DataUtilities;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;

public class ActivityService extends BaseHttpClient{

    public Object postActivity(ActivityRegisterDTO dto) throws IOException {
        String json = objectMapper.writeValueAsString(dto);

        try{
            var request = buildRequest(
                    "/activity",
                    "POST",
                    json,
                    true
            );
            return sendRequest(request, ActivityDTO.class);
        }catch(IllegalArgumentException e){
            return failedErrorJsonLecture("/");
        }
    }

    public Object getActivityById(String id) {
        try{
            var request = buildRequest(
                    "/activity?id="+id,
                    "GET",
                    null,
                    true
            );
            return sendRequest(request, ActivityDTO.class);
        }catch(IllegalArgumentException e){
            return failedErrorJsonLecture("/");
        }
    }

    public Object getNonFinishedActivities(int page) {
        try{
            var request = buildRequest(
                    "/activity/all?page="+page,
                    "GET",
                    null,
                    true
            );
            return sendRequest(request, new TypeReference<SimplePage<ActivityDTO>>() {});
        }catch(IllegalArgumentException e){
            return failedErrorJsonLecture("/");
        }
    }

    public Object deleteActivity(String id){
        try{
            var request = buildRequest(
                    "/activity?id="+id,
                    "DELETE",
                    null,
                    true
            );
            return sendRequest(request, Void.class);
        }catch(IllegalArgumentException e){
            return failedErrorJsonLecture("/");
        }
    }

}
