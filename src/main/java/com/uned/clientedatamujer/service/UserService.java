package com.uned.clientedatamujer.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.uned.clientedatamujer.dto.SimplePage;
import com.uned.clientedatamujer.dto.request.LegalPersonUpdateDTO;
import com.uned.clientedatamujer.dto.request.PhysicalPersonUpdateDTO;
import com.uned.clientedatamujer.dto.response.LegalPersonDTO;
import com.uned.clientedatamujer.dto.response.PhysicalPersonDTO;
import com.uned.clientedatamujer.dto.response.ProfileDTO;
import com.uned.clientedatamujer.dto.response.VolunteeringDTO;

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
                .header("Content-Type", "application/json")
                .header("AUTHORIZATION", "Bearer "+accessJwt)
                .build();
        if(AuthSession.getPersonType().equals("FISICA")){
            return sendRequest(request, PhysicalPersonDTO.class);
        }else{
            return sendRequest(request, LegalPersonDTO.class);
        }
    }

    public Object getUserBySurname(String accessJwt, int currentPage, String param) {
        String url = URL + "/user/search/surname?surname="+param+"&page="+currentPage;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("Content-Type", "application/json")
                .header("AUTHORIZATION", "Bearer "+accessJwt)
                .build();

        return sendRequest(request, new TypeReference<SimplePage<ProfileDTO>>() {});
    }

    public Object getUserByBusiness(String accessJwt, int currentPage, String param) {
        String url = URL + "/user/search/business?businessName="+param+"&page="+currentPage;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("Content-Type", "application/json")
                .header("AUTHORIZATION", "Bearer "+accessJwt)
                .build();

        return sendRequest(request, new TypeReference<SimplePage<ProfileDTO>>() {});
    }

    public Object getUserByName(String accessJwt, int currentPage, String param) {
        String url = URL + "/user/search/name?name="+param+"&page="+currentPage;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("Content-Type", "application/json")
                .header("AUTHORIZATION", "Bearer "+accessJwt)
                .build();

        return sendRequest(request, new TypeReference<SimplePage<ProfileDTO>>() {});
    }

    public Object getUserByLegalId(String accessJwt, String param) {
        String url = URL + "/user/search/legal-id?="+param;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("AUTHORIZATION", "Bearer "+accessJwt)
                .build();

        return sendRequest(request, ProfileDTO.class);
    }

    public Object getUserByNationalId(String accessJwt, String param) {
        String url = URL + "/user/search/national-id?="+param;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("AUTHORIZATION", "Bearer "+accessJwt)
                .build();

        return sendRequest(request, ProfileDTO.class);
    }

    public Object getUserByUsername(String accessJwt, String param) {
        String url = URL + "/user/search/username?="+param;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("AUTHORIZATION", "Bearer "+accessJwt)
                .build();

        return sendRequest(request, ProfileDTO.class);
    }
}
