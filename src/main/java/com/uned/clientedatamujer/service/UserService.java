package com.uned.clientedatamujer.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.uned.clientedatamujer.dto.SimplePage;
import com.uned.clientedatamujer.dto.request.LegalPersonUpdateDTO;
import com.uned.clientedatamujer.dto.request.PhysicalPersonUpdateDTO;
import com.uned.clientedatamujer.dto.response.*;
import com.uned.clientedatamujer.service.util.AuthSession;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;

public class UserService extends BaseHttpClient{

    public Object getMyProfile(){
        try{
            var request = buildRequest(
                    "/user/me",
                    "GET",
                    null,
                    true
            );
            return sendRequest(request, ProfileDTO.class);
        }catch(IllegalArgumentException e){
            return failedErrorJsonLecture("/");
        }
    }

    public Object getUserBySurname(int currentPage, String param) {
        try{
            var request = buildRequest(
                    "/user/search/surname?surname="+param+"&page="+currentPage,
                    "GET",
                    null,
                    true
            );
            return sendRequest(request, new TypeReference<SimplePage<NoPoliPhysicalPersonDTO>>() {});
        }catch(IllegalArgumentException e){
            return failedErrorJsonLecture("/");
        }
    }

    public Object getUserByBusiness(int currentPage, String param) {
        try{
            var request = buildRequest(
                    "/user/search/business?businessName="+param+"&page="+currentPage,
                    "GET",
                    null,
                    true
            );
            return sendRequest(request, new TypeReference<SimplePage<NoPoliLegalPersonDTO>>() {});
        }catch(IllegalArgumentException e){
            return failedErrorJsonLecture("/");
        }
    }

    public Object getUserByName(int currentPage, String param) {
        try{
            var request = buildRequest(
                    "/user/search/name?name="+param+"&page="+currentPage,
                    "GET",
                    null,
                    true
            );
            return sendRequest(request, new TypeReference<SimplePage<NoPoliPhysicalPersonDTO>>() {});
        }catch(IllegalArgumentException e){
            return failedErrorJsonLecture("/");
        }
    }

    public Object getUserByLegalId(String param) {
        try{
            var request = buildRequest(
                    "/user/search/legal-id?id="+param,
                    "GET",
                    null,
                    true
            );
            return sendRequest(request, ProfileDTO.class);
        }catch(IllegalArgumentException e){
            return failedErrorJsonLecture("/");
        }
    }

    public Object getUserByNationalId(String param) {
        try{
            var request = buildRequest(
                    "/user/search/national-id?id="+param,
                    "GET",
                    null,
                    true
            );
            return sendRequest(request, ProfileDTO.class);
        }catch(IllegalArgumentException e){
            return failedErrorJsonLecture("/");
        }
    }

    public Object getUserByUsername(String param) {
        try{
            var request = buildRequest(
                    "/user/search/username?username="+param,
                    "GET",
                    null,
                    true
            );
            return sendRequest(request, ProfileDTO.class);
        }catch(IllegalArgumentException e){
            return failedErrorJsonLecture("/");
        }
    }

    public Object updateProfile(Object object) throws IOException {
        String url = "/user/me/";
        String json = "";
        if(object instanceof PhysicalPersonUpdateDTO dto){
            url = url + "physical";
            json = objectMapper.writeValueAsString(dto);
        }else if(object instanceof LegalPersonUpdateDTO dto){
            url = url + "legal";
            json = objectMapper.writeValueAsString(dto);
        }

        try{
            var request = buildRequest(
                    url,
                    "PUT",
                    json,
                    true
            );
            if(AuthSession.getPersonType().equals("FISICA")){
                return sendRequest(request, PhysicalPersonDTO.class);
            }else{
                return sendRequest(request, LegalPersonDTO.class);
            }
        }catch(IllegalArgumentException e){
            return failedErrorJsonLecture("/");
        }
    }

    public Object updateAffiliate(String username){
        try{
            var request = buildRequest(
                    "/user/set/Affiliate?username="+username,
                    "PUT",
                    null,
                    true
            );
            return sendRequest(request, String.class);
        }catch(IllegalArgumentException e){
            return failedErrorJsonLecture("/");
        }
    }

    public Object updateRole(String username, String role){
        try{
            var request = buildRequest(
                    "/user/set/Role?username="+username+"&role="+role,
                    "PUT",
                    null,
                    true
            );
            return sendRequest(request, String.class);
        }catch(IllegalArgumentException e){
            return failedErrorJsonLecture("/");
        }
    }
}
