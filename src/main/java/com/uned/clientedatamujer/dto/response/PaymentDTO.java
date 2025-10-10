package com.uned.clientedatamujer.dto.response;

import com.uned.clientedatamujer.enums.Classification;
import com.uned.clientedatamujer.enums.Method;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentDTO(
        long id,
        String username,
        String description,
        Classification classification,
        Method method,
        LocalDateTime paymentDate,
        Boolean isPaid,
        BigDecimal totalAmount
) { }
