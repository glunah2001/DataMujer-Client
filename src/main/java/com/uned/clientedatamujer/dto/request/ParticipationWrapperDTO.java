package com.uned.clientedatamujer.dto.request;

import java.util.List;

public record ParticipationWrapperDTO(
        Long activityId,
        List<String> usernames
) {}
