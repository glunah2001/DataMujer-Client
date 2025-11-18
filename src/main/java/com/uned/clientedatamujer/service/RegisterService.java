package com.uned.clientedatamujer.service;

import com.uned.clientedatamujer.dto.request.LegalPersonRegisterDTO;
import com.uned.clientedatamujer.dto.request.PhysicalPersonRegisterDTO;
import com.uned.clientedatamujer.dto.response.ActivityDTO;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;

public class RegisterService extends BaseHttpClient{

    public Object register(Object data) throws IOException {
        String url = "";
        String json = "";
        if(data instanceof PhysicalPersonRegisterDTO dto){
            url = "/register/physical";
            json = objectMapper.writeValueAsString(dto);
        }else if(data instanceof LegalPersonRegisterDTO dto){
            url = "/register/legal";
            json = objectMapper.writeValueAsString(dto);
        }

        try{
            var request = buildRequest(
                    url,
                    "POST",
                    json,
                    false
            );
            return sendRequest(request, String.class);
        }catch(IllegalArgumentException e){
            return failedErrorJsonLecture("/");
        }

    }
}
