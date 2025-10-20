package com.uned.clientedatamujer.controller.view;

import com.uned.clientedatamujer.dto.response.ActivityDTO;
import com.uned.clientedatamujer.service.AuthSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ActivityCardController implements BaseCardController<ActivityDTO>{

    @FXML
    private Label labelID;
    @FXML
    private TextArea txtDescription;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnVolunteering;

    @FXML
    private void deleteActivity(ActionEvent event) {
        System.out.println("ELIMINAR");
    }

    @FXML
    private void applyToVolunteering(ActionEvent event) {
        System.out.println("VOLUNTARIADO");
    }

    @FXML
    private void applyToParticipate(ActionEvent event) {
        System.out.println("PARTICIPAR");
    }

    @Override
    public void setData(ActivityDTO dto){
        labelID.setText(
                String.format("ID #%d - %S", dto.id(), dto.activity())
        );
        txtDescription.setText(
                String.format("""
                MODALIDAD: %S
                UBICACIÓN/PLATAFORMA: %s
                INICIO: %s
                FIN: %s
                
                %s
                """,
                dto.isOnSite() ? "PRESENCIAL" : "VIRTUAL",
                dto.location(),
                textFormatter(dto.startDate()),
                textFormatter(dto.endDate()),
                dto.description())
        );

        if(AuthSession.getRole().equals("ROLE_STANDARD")){
            btnVolunteering.setVisible(false);
            btnVolunteering.setManaged(false);
            btnDelete.setVisible(false);
            btnDelete.setManaged(false);
        }
    }

    private String textFormatter(LocalDateTime date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy  HH:mm");
        return formatter.format(date);
    }
}
