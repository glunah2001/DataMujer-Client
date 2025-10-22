package com.uned.clientedatamujer.service;

import com.uned.clientedatamujer.dto.response.ActivityDTO;

public class DataUtilities {
    private static ActivityDTO lastActivityDTO;

    public static ActivityDTO getLastActivityDTO() {return lastActivityDTO;}

    public static void setLastActivityDTO(ActivityDTO lastActivityDTO) {
        DataUtilities.lastActivityDTO = lastActivityDTO;
    }

    public static void clearActivityDTO(){setLastActivityDTO(null);}
}
