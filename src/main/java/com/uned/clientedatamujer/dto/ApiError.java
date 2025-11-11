package com.uned.clientedatamujer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public record ApiError(
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS") LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        List<String> details
) { }
