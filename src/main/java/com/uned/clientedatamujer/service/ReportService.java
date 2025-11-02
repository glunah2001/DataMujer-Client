package com.uned.clientedatamujer.service;

import com.uned.clientedatamujer.dto.response.AffiliatesPaymentReportDTO;
import com.uned.clientedatamujer.dto.response.ParticipationDTO;
import com.uned.clientedatamujer.dto.response.VolunteeringDTO;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportService {

    public static void genReportVolunteering(List<VolunteeringDTO> content) {
        try {
            JasperReport jasperReport = getReportTemplate(
                    "/com/uned/clientedatamujer/reports/VolunteeringReport.jasper"
            );

            Map<String, Object> parameters = new HashMap<>();
            loadImage(parameters);

            VolunteeringDTO ref = content.isEmpty() ? null : content.getFirst();
            if(ref == null) return;
            loadInActivityReportHeader(ref, parameters);

            List<Map<String, Object>> reportData = content.stream()
                    .map(dto -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", dto.id());
                        map.put("username", dto.username());
                        map.put("startShift", formatDateTime(dto.startShift()));
                        map.put("endShift", formatDateTime(dto.endShift()));
                        map.put("activityRole", dto.activityRole());
                        return map;
                    })
                    .collect(Collectors.toList());

            createDataSource(reportData, parameters, VolunteeringDTO.class);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
            createDoc(jasperPrint, "ReporteVoluntariado");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void genReportParticipation(List<ParticipationDTO> content) {
        try {
            JasperReport jasperReport = getReportTemplate(
                    "/com/uned/clientedatamujer/reports/ParticipationReport.jasper"
            );

            Map<String, Object> parameters = new HashMap<>();
            loadImage(parameters);

            ParticipationDTO ref = content.isEmpty() ? null : content.getFirst();
            if(ref == null) return;
            loadInActivityReportHeader(ref, parameters);

            List<Map<String, Object>> reportData = content.stream()
                    .map(dto -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", dto.id());
                        map.put("username", dto.username());
                        map.put("registrationDate", dto.registerDate());
                        map.put("startDate", dto.startDate());
                        map.put("endDate", dto.endDate());
                        map.put("participationState", dto.participationState().toString());
                        return map;
                    })
                    .collect(Collectors.toList());

            createDataSource(reportData, parameters, ParticipationDTO.class);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
            createDoc(jasperPrint, "ReporteParticipacion");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void genReportAffiliate(List<AffiliatesPaymentReportDTO> content) {
        try {
            JasperReport jasperReport = getReportTemplate(
                    "/com/uned/clientedatamujer/reports/AffiliateReport.jasper"
            );

            Map<String, Object> parameters = new HashMap<>();
            loadImage(parameters);

            List<Map<String, Object>> reportData = content.stream()
                    .map(dto -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("username", dto.username());
                        map.put("lastaymentDate", formatDateTime(dto.lastPaymentDate()));
                        map.put("expirationDate", formatDateTime(dto.affiliateExpirationDate()));
                        map.put("totalAmount", ("C "+dto.totalPaid()));
                        map.put("state", dto.isAffiliate() ? "Afiliado" : "Sin afiliar");
                        return map;
                    })
                    .collect(Collectors.toList());

            createDataSource(reportData, parameters, AffiliatesPaymentReportDTO.class);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
            createDoc(jasperPrint, "ReporteAfiliados");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return dateTime.format(formatter);
    }


    private static JasperReport getReportTemplate(String url) throws JRException {
        var inputStream = ReportService.class.getResourceAsStream(url);
        return (JasperReport) JRLoader.loadObject(inputStream);
    }

    private static void loadImage(Map<String, Object> parameters){
        InputStream logoStream = ReportService.class.getResourceAsStream(
                "/com/uned/clientedatamujer/images/DataMujer.jpg"
        );
        parameters.put("LogoDataMujer", logoStream);
    }

    private static <T> void loadInActivityReportHeader(T ref, Map<String, Object> parameters){
        switch(ref){
            case VolunteeringDTO content -> {
                parameters.put("ActividadID", content.activityId());
                parameters.put("ActividadNombre", content.activity());
                parameters.put("description", content.description());
                parameters.put("activityType", content.isOnSite() ? "Presencial" : "Virtual");
            }
            case ParticipationDTO content -> {
                parameters.put("ActividadID", content.activityId());
                parameters.put("ActividadNombre", content.activity());
                parameters.put("description", content.description());
                parameters.put("activityType", content.isOnSite() ? "Presencial" : "Virtual");
            }
            default -> {return;}
        }
    }

    private static <T> void createDataSource(List<Map<String, Object>> reportData,
                                         Map<String, Object> parameters,
                                         Class<T> dataSourceType){
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportData);
        String dataSourceName = "";

        if(dataSourceType.equals(VolunteeringDTO.class)){
            dataSourceName = "VolunteeringDataSource";
        }else if(dataSourceType.equals(ParticipationDTO.class)){
            dataSourceName = "ParticipationDataSource";
        }else if(dataSourceType.equals(AffiliatesPaymentReportDTO.class)){
            dataSourceName = "AffiliateDataSource";
        }
        parameters.put(dataSourceName, dataSource);
    }

    private static void createDoc(JasperPrint jasperPrint, String docName) throws IOException, JRException {
        Path downloads = Paths.get(System.getProperty("user.home"), "Downloads");
        if (!Files.exists(downloads)) {
            Files.createDirectories(downloads);
        }
        String extension = ".pdf";
        Path pdfPath = downloads.resolve(docName + extension);

        int counter = 1;
        while (Files.exists(pdfPath)) {
            String newName = docName + "(" + counter + ")" + extension;
            pdfPath = downloads.resolve(newName);
            counter++;
        }

        JasperExportManager.exportReportToPdfFile(jasperPrint, pdfPath.toString());
    }
}
