package com.uned.clientedatamujer.dto;

import java.util.List;

public record SimplePage<T>(
    List<T> content,
    long totalElements,
    int totalPages,
    int currentPage
) { }
