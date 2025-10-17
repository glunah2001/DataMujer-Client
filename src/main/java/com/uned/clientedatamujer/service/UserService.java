package com.uned.clientedatamujer.service;

import com.uned.clientedatamujer.dto.request.LegalPersonUpdateDTO;
import com.uned.clientedatamujer.dto.request.PhysicalPersonUpdateDTO;
import com.uned.clientedatamujer.dto.response.LegalPersonDTO;
import com.uned.clientedatamujer.dto.response.PhysicalPersonDTO;
import com.uned.clientedatamujer.dto.response.ProfileDTO;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;

public class UserService extends BaseHttpClient{

    public Object getMyProfile(){
        String url = URL + "/user/me";
        String accessJwt = AuthSession.getAccessToken();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("AUTHORIZATION", "Bearer "+accessJwt)
                .build();

        return sendRequest(request, ProfileDTO.class);
    }

    public Object updateProfile(Object object) throws IOException {
        String url = URL + "/user/me/";
        String accessJwt = AuthSession.getAccessToken();
        String json = "";
        if(object instanceof PhysicalPersonUpdateDTO dto){
            url = url + "physical";
            json = objectMapper.writeValueAsString(dto);
        }else if(object instanceof LegalPersonUpdateDTO dto){
            url = url + "legal";
            json = objectMapper.writeValueAsString(dto);
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "Application/json")
                .header("AUTHORIZATION", "Bearer "+accessJwt)
                .build();
        if(AuthSession.getPersonType().equals("FISICA")){
            return sendRequest(request, PhysicalPersonDTO.class);
        }else{
            return sendRequest(request, LegalPersonDTO.class);
        }
    }
}
