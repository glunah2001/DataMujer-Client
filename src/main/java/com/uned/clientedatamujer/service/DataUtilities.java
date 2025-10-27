package com.uned.clientedatamujer.service;

import com.uned.clientedatamujer.dto.response.ActivityDTO;
import com.uned.clientedatamujer.dto.response.PaymentDTO;
import com.uned.clientedatamujer.dto.response.VolunteeringDTO;

public class DataUtilities {
    private static ActivityDTO lastActivityDTO;
    private static VolunteeringDTO lastVolunteeringDTO;
    private static PaymentDTO lastPaymentDTO;

    public static PaymentDTO getLastPaymentDTO() {
        return lastPaymentDTO;
    }

    public static void setLastPaymentDTO(PaymentDTO lastPaymentDTO) {
        DataUtilities.lastPaymentDTO = lastPaymentDTO;
    }

    public static VolunteeringDTO getLastVolunteeringDTO() {
        return lastVolunteeringDTO;
    }

    public static void setLastVolunteeringDTO(VolunteeringDTO lastVolunteeringDTO) {
        DataUtilities.lastVolunteeringDTO = lastVolunteeringDTO;
    }

    public static ActivityDTO getLastActivityDTO() {return lastActivityDTO;}

    public static void setLastActivityDTO(ActivityDTO lastActivityDTO) {
        DataUtilities.lastActivityDTO = lastActivityDTO;
    }

    public static void clearPaymentDTO(){setLastPaymentDTO(null);}

    public static void clearActivityDTO(){setLastActivityDTO(null);}

    public static void clearVolunteeringDTO(){setLastVolunteeringDTO(null);}
}
