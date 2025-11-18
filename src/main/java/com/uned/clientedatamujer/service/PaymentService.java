package com.uned.clientedatamujer.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.uned.clientedatamujer.dto.SimplePage;
import com.uned.clientedatamujer.dto.request.PaymentRegisterDTO;
import com.uned.clientedatamujer.dto.response.ActivityDTO;
import com.uned.clientedatamujer.dto.response.AffiliatesPaymentReportDTO;
import com.uned.clientedatamujer.dto.response.PaymentDTO;
import com.uned.clientedatamujer.service.util.AuthSession;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.time.LocalDateTime;

public class PaymentService extends BaseHttpClient {

    public Object getPaymentById(String id) {
       try{
            var request = buildRequest(
                    "/payment?id=" + id,
                    "GET",
                    null,
                    true
            );
            return sendRequest(request, PaymentDTO.class);
        }catch(IllegalArgumentException e){
            return failedErrorJsonLecture("/");
        }
    }

    public Object getMyPayment(int page){
        try{
            var request = buildRequest(
                    "/payment/me?page="+page,
                    "GET",
                    null,
                    true
            );
            return sendRequest(request, new TypeReference<SimplePage<PaymentDTO>>() {});
        }catch(IllegalArgumentException e){
            return failedErrorJsonLecture("/");
        }
    }

    public Object getPaymentByStatus(boolean status, int page){
        try{
            var request = buildRequest(
                    "/payment/status?isPaid="+status+"&page="+page,
                    "GET",
                    null,
                    true
            );
            return sendRequest(request, new TypeReference<SimplePage<PaymentDTO>>() {});
        }catch(IllegalArgumentException e){
            return failedErrorJsonLecture("/");
        }
    }

    public Object getAffiliateReport(int page) {
        try{
            var request = buildRequest(
                    "/payment/affiliates-report?page="+page,
                    "GET",
                    null,
                    true
            );
            return sendRequest(request, new TypeReference<SimplePage<AffiliatesPaymentReportDTO>>() {});
        }catch(IllegalArgumentException e){
            return failedErrorJsonLecture("/");
        }
    }

    public Object deletePayment(String id) {
        try{
            var request = buildRequest(
                    "/payment?id="+id,
                    "DELETE",
                    null,
                    true
            );
            return sendRequest(request, Void.class);
        }catch(IllegalArgumentException e){
            return failedErrorJsonLecture("/");
        }
    }

    public Object pay(String id, LocalDateTime dateTime){
        try{
            var request = buildRequest(
                    "/payment/paid?id="+id+"&date="+dateTime,
                    "PUT",
                    null,
                    true
            );
            return sendRequest(request, PaymentDTO.class);
        }catch(IllegalArgumentException e){
            return failedErrorJsonLecture("/");
        }
    }

    public Object unpay(String id) {
        try{
            var request = buildRequest(
                    "/payment/unpaid?id="+id,
                    "PUT",
                    null,
                    true
            );
            return sendRequest(request, PaymentDTO.class);
        }catch(IllegalArgumentException e){
            return failedErrorJsonLecture("/");
        }
    }

    public Object postPayment(PaymentRegisterDTO dto) throws IOException{
        String json = objectMapper.writeValueAsString(dto);
        try{
            var request = buildRequest(
                    "/payment",
                    "POST",
                    json,
                    true
            );
            return sendRequest(request, PaymentDTO.class);
        }catch(IllegalArgumentException e){
            return failedErrorJsonLecture("/");
        }
    }
}
