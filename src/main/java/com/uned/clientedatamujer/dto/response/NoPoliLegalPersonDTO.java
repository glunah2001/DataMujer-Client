package com.uned.clientedatamujer.dto.response;

import com.uned.clientedatamujer.enums.Country;

import java.time.LocalDate;

public record NoPoliLegalPersonDTO(
        String legalId,
        String businessName,
        LocalDate foundationDate,
        String phoneNumber,
        Country country,
        String location,
        String username,
        String email
){ }
