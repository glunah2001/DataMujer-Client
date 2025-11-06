package com.uned.clientedatamujer.service;

import com.uned.clientedatamujer.dto.SimplePage;
import com.uned.clientedatamujer.dto.response.*;

import java.util.List;

public class DataUtilities {
    private static ActivityDTO lastActivityDTO;
    private static VolunteeringDTO lastVolunteeringDTO;
    private static PaymentDTO lastPaymentDTO;
    private static List<?> lastContent;

    public static PaymentDTO getLastPaymentDTO() {return lastPaymentDTO;}
    public static VolunteeringDTO getLastVolunteeringDTO() {return lastVolunteeringDTO;}
    public static ActivityDTO getLastActivityDTO() {return lastActivityDTO;}
    public static List<?> getLastContent() {return lastContent;}

    public static void setLastPaymentDTO(PaymentDTO lastPaymentDTO) {
        DataUtilities.lastPaymentDTO = lastPaymentDTO;
    }
    public static void setLastVolunteeringDTO(VolunteeringDTO lastVolunteeringDTO) {
        DataUtilities.lastVolunteeringDTO = lastVolunteeringDTO;
    }
    public static void setLastActivityDTO(ActivityDTO lastActivityDTO) {
        DataUtilities.lastActivityDTO = lastActivityDTO;
    }
    public static void setLastContent(List<?> lastContent) {DataUtilities.lastContent = lastContent;}

    public static void clearPaymentDTO(){setLastPaymentDTO(null);}
    public static void clearActivityDTO(){setLastActivityDTO(null);}
    public static void clearVolunteeringDTO(){setLastVolunteeringDTO(null);}
    public static void clearLastContent(){setLastContent(null);}
    public static void clearAll() {
        if(lastActivityDTO != null) clearActivityDTO();
        if(lastVolunteeringDTO != null) clearVolunteeringDTO();
        if(lastPaymentDTO != null) clearPaymentDTO();
        if(lastContent != null) clearLastContent();
    }

    public static SimplePage<PhysicalPersonDTO> mapToPhysicalProfile(SimplePage<NoPoliPhysicalPersonDTO> plainPage) {
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

    public static SimplePage<LegalPersonDTO> mapToLegalProfile(SimplePage<NoPoliLegalPersonDTO> plainPage) {
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
