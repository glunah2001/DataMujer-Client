package com.uned.clientedatamujer.dto.response;

import java.time.LocalDateTime;

public record ActivityDTO(
        long id,
        String activity,
        String description,
        String location,
        boolean isOnSite,
        LocalDateTime startDate,
        LocalDateTime endDate
) { }
