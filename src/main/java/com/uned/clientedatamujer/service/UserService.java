package com.uned.clientedatamujer.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.uned.clientedatamujer.dto.SimplePage;
import com.uned.clientedatamujer.dto.request.LegalPersonUpdateDTO;
import com.uned.clientedatamujer.dto.request.PhysicalPersonUpdateDTO;
import com.uned.clientedatamujer.dto.response.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;

public class UserService extends BaseHttpClient{

    public Object getMyProfile(){
        String url = URL + "/user/me";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("AUTHORIZATION", "Bearer "+AuthSession.getAccessToken())
                .build();

        return sendRequest(request, ProfileDTO.class);
    }

    public Object getUserBySurname(int currentPage, String param) {
        String url = URL + "/user/search/surname?surname="+param+"&page="+currentPage;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("Content-Type", "application/json")
                .header("AUTHORIZATION", "Bearer "+AuthSession.getAccessToken())
                .build();

        return sendRequest(request, new TypeReference<SimplePage<NoPoliPhysicalPersonDTO>>() {});
    }

    public Object getUserByBusiness(int currentPage, String param) {
        String url = URL + "/user/search/business?businessName="+param+"&page="+currentPage;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("Content-Type", "application/json")
                .header("AUTHORIZATION", "Bearer "+AuthSession.getAccessToken())
                .build();

        return sendRequest(request, new TypeReference<SimplePage<NoPoliLegalPersonDTO>>() {});
    }

    public Object getUserByName(int currentPage, String param) {
        String url = URL + "/user/search/name?name="+param+"&page="+currentPage;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("Content-Type", "application/json")
                .header("AUTHORIZATION", "Bearer "+AuthSession.getAccessToken())
                .build();

        return sendRequest(request, new TypeReference<SimplePage<NoPoliPhysicalPersonDTO>>() {});
    }

    public Object getUserByLegalId(String param) {
        String url = URL + "/user/search/legal-id?id="+param;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("AUTHORIZATION", "Bearer "+AuthSession.getAccessToken())
                .build();

        return sendRequest(request, ProfileDTO.class);
    }

    public Object getUserByNationalId(String param) {
        String url = URL + "/user/search/national-id?id="+param;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("AUTHORIZATION", "Bearer "+AuthSession.getAccessToken())
                .build();

        return sendRequest(request, ProfileDTO.class);
    }

    public Object getUserByUsername(String param) {
        String url = URL + "/user/search/username?username="+param;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("AUTHORIZATION", "Bearer "+AuthSession.getAccessToken())
                .build();

        return sendRequest(request, ProfileDTO.class);
    }

    public Object updateProfile(Object object) throws IOException {
        String url = URL + "/user/me/";
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
                .header("AUTHORIZATION", "Bearer "+AuthSession.getAccessToken())
                .build();
        if(AuthSession.getPersonType().equals("FISICA")){
            return sendRequest(request, PhysicalPersonDTO.class);
        }else{
            return sendRequest(request, LegalPersonDTO.class);
        }
    }

    public Object updateAffiliate(String username){
        String url = URL + "/user/set/Affiliate?username="+username;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .PUT(HttpRequest.BodyPublishers.noBody())
                .header("Content-Type", "application/json")
                .header("AUTHORIZATION", "Bearer "+AuthSession.getAccessToken())
                .build();
        return sendRequest(request, String.class);
    }

    public Object updateRole(String username, String role){
        String url = URL + "/user/set/Role?username="+username+"&role="+role;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .PUT(HttpRequest.BodyPublishers.noBody())
                .header("Content-Type", "application/json")
                .header("AUTHORIZATION", "Bearer "+AuthSession.getAccessToken())
                .build();
        return sendRequest(request, String.class);
    }
}
