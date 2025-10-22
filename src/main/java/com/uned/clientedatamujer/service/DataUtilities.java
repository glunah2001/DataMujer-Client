package com.uned.clientedatamujer.service;

import com.uned.clientedatamujer.dto.response.ActivityDTO;
import com.uned.clientedatamujer.dto.response.VolunteeringDTO;

public class DataUtilities {
    private static ActivityDTO lastActivityDTO;
    private static VolunteeringDTO lastVolunteeringDTO;

    public static VolunteeringDTO getLastVolunteeringDTO() {
        return lastVolunteeringDTO;
    }

    public static void setLastVolunteeringDTO(VolunteeringDTO lastVolunteeringDTO) {
        DataUtilities.lastVolunteeringDTO = lastVolunteeringDTO;
    }

    public static void clearVolunteeringDTO(){setLastVolunteeringDTO(null);}

    public static ActivityDTO getLastActivityDTO() {return lastActivityDTO;}

    public static void setLastActivityDTO(ActivityDTO lastActivityDTO) {
        DataUtilities.lastActivityDTO = lastActivityDTO;
    }

    public static void clearActivityDTO(){setLastActivityDTO(null);}
}
