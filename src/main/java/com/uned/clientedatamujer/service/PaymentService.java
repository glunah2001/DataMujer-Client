package com.uned.clientedatamujer.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.uned.clientedatamujer.dto.SimplePage;
import com.uned.clientedatamujer.dto.request.PaymentRegisterDTO;
import com.uned.clientedatamujer.dto.response.PaymentDTO;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;

public class PaymentService extends BaseHttpClient {

    public Object getPaymentById(String accessJwt, String id) {
        String url = URL + "/payment?id=" + id;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("Content-Type", "application/json")
                .header("AUTHORIZATION", "Bearer " + accessJwt)
                .build();

        return sendRequest(request, PaymentDTO.class);
    }

    public Object getMyPayment(String accessJwt, int page){
        String url = URL + "/payment/me?page="+page;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("Content-Type", "application/json")
                .header("AUTHORIZATION", "Bearer "+accessJwt)
                .build();

        return sendRequest(request, new TypeReference<SimplePage<PaymentDTO>>() {});
    }

    public Object getPaymentByStatus(String accessJwt, boolean status, int page){
        String url = URL + "/payment/status?isPaid="+status+"&page="+page;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("Content-Type", "application/json")
                .header("AUTHORIZATION", "Bearer "+accessJwt)
                .build();

        return sendRequest(request, new TypeReference<SimplePage<PaymentDTO>>() {});
    }

    public Object deletePayment(String accessJwt, String id) {
        String url = URL + "/payment?id="+id;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .DELETE()
                .header("Content-Type", "application/json")
                .header("AUTHORIZATION", "Bearer "+accessJwt)
                .build();

        return sendRequest(request, Void.class);
    }

    public Object unpay(String accessJwt, String id) {
        String url = URL + "/payment/unpaid?id="+id;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .PUT(HttpRequest.BodyPublishers.noBody())
                .header("Content-Type", "application/json")
                .header("AUTHORIZATION", "Bearer "+accessJwt)
                .build();

        return sendRequest(request, PaymentDTO.class);
    }

    public Object postPayment(PaymentRegisterDTO dto, String accessJwt) throws IOException{
        String url = URL + "/payment";

        String json = objectMapper.writeValueAsString(dto);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .header("AUTHORIZATION", "Bearer "+accessJwt)
                .build();

        return sendRequest(request, PaymentDTO.class);
    }
}
