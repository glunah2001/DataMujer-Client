package com.uned.clientedatamujer.controller.view;

import com.uned.clientedatamujer.dto.response.AffiliatesPaymentReportDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AffiliateCardController implements BaseCardController<AffiliatesPaymentReportDTO> {


    @FXML
    private Label labelID;
    @FXML
    private TextArea txtDescription;

    @Override
    public void setData(AffiliatesPaymentReportDTO dto) {
        labelID.setText(dto.username());
        txtDescription.setText(
                String.format("""
                ÚLTIMA FECHA DE PAGO: %s
                FECHA DE VENCIMIENTO: %s
                TOTAL CONTRIBUIDO: %s
                ESTADO ACTUAL: %s
                """,
                        textFormatter(dto.lastPaymentDate()),
                        textFormatter(dto.affiliateExpirationDate()),
                        ("₡ "+dto.totalPaid()),
                        dto.isAffiliate() ? "AFILIADO" : "SIN AFILIAR"
                )
        );
    }

    @Override
    public void setParentController(BaseSubSceneController parent) {}

    private String textFormatter(LocalDateTime date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy  HH:mm");
        return formatter.format(date);
    }
}
