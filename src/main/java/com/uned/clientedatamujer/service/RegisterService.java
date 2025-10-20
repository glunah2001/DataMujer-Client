package com.uned.clientedatamujer.service;
import com.uned.clientedatamujer.dto.request.LegalPersonRegisterDTO;
import com.uned.clientedatamujer.dto.request.PhysicalPersonRegisterDTO;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;

public class RegisterService extends BaseHttpClient{

    public Object register(Object data) throws IOException {
        String url = "";
        String json = "";
        if(data instanceof PhysicalPersonRegisterDTO dto){
            url = URL + "/register/physical";
            json = objectMapper.writeValueAsString(dto);
        }else if(data instanceof LegalPersonRegisterDTO dto){
            url = URL + "/register/legal";
            json = objectMapper.writeValueAsString(dto);
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();
        return sendRequest(request, String.class);
    }
}
