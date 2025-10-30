package com.uned.clientedatamujer.service;

import com.uned.clientedatamujer.dto.SimplePage;
import com.uned.clientedatamujer.dto.response.*;

import java.util.List;

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

    public static SimplePage<PhysicalPersonDTO> mapToPhysicalProfile(SimplePage<PhysicalPersonPageDTO> plainPage) {
        List<PhysicalPersonDTO> dtos = plainPage.content().stream()
                .map(p -> new PhysicalPersonDTO(
                        p.nationalId(),
                        p.firstSurname(),
                        p.secondSurname(),
                        p.name(),
                        p.profession(),
                        p.birthDate(),
                        p.phoneNumber(),
                        p.country(),
                        p.location(),
                        p.username(),
                        p.email()
                ))
                .toList();

        return new SimplePage<>(
                dtos,
                plainPage.totalElements(),
                plainPage.totalPages(),
                plainPage.currentPage()
        );
    }

    public static SimplePage<LegalPersonDTO> mapToLegalProfile(SimplePage<LegalPersonPageDTO> plainPage) {
        List<LegalPersonDTO> dtos = plainPage.content().stream()
                .map(p -> new LegalPersonDTO(
                        p.legalId(),
                        p.businessName(),
                        p.foundationDate(),
                        p.phoneNumber(),
                        p.country(),
                        p.location(),
                        p.username(),
                        p.email()
                ))
                .toList();

        return new SimplePage<>(
                dtos,
                plainPage.totalElements(),
                plainPage.totalPages(),
                plainPage.currentPage()
        );
    }
}
