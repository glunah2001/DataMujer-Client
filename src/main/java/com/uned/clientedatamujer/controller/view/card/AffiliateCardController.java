package com.uned.clientedatamujer.controller.view.card;

import com.uned.clientedatamujer.controller.view.base.BaseCardController;
import com.uned.clientedatamujer.controller.view.base.BaseSubSceneController;
import com.uned.clientedatamujer.dto.response.AffiliatesPaymentReportDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.time.LocalDate;
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

    @Override
    public String textFormatter(LocalDateTime date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy  HH:mm");
        return formatter.format(date);
    }

    @Override
    public String textFormatter(LocalDate date){return "";}

}
