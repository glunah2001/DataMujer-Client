package com.uned.clientedatamujer.dto.response;

import com.uned.clientedatamujer.enums.Country;

import java.time.LocalDate;

public record NoPoliPhysicalPersonDTO(
        String nationalId,
        String firstSurname,
        String secondSurname,
        String name,
        String profession,
        LocalDate birthDate,
        String phoneNumber,
        Country country,
        String location,
        String username,
        String email
){ }
