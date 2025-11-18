package com.uned.clientedatamujer.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record VolunteeringRegisterDTO(
        @Valid
        BaseVolunteeringRegisterDTO volunteeringData,
        @NotBlank(message = "EL dato \"Nombre de Usuario\" es obligatorio")
        String username
) { }
