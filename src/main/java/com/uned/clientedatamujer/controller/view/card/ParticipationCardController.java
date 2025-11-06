package com.uned.clientedatamujer.controller.view.card;

import com.uned.clientedatamujer.controller.util.UIUXFeedbackUtils;
import com.uned.clientedatamujer.controller.view.base.BaseCardController;
import com.uned.clientedatamujer.controller.view.base.BaseSubSceneController;
import com.uned.clientedatamujer.controller.view.subscene.ParticipationController;
import com.uned.clientedatamujer.dto.response.ParticipationDTO;
import com.uned.clientedatamujer.enums.ParticipationState;
import com.uned.clientedatamujer.service.AuthSession;
import com.uned.clientedatamujer.service.ParticipationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ParticipationCardController implements BaseCardController<ParticipationDTO> {


    @FXML
    private Label labelID;
    @FXML
    private TextArea txtDescription;
    @FXML
    private Button btnStart;
    @FXML
    private Button btnCancel;

    private BaseSubSceneController parentController;
    private ParticipationDTO data;
    private final ParticipationService service = new ParticipationService();


    @Override
    public void setData(ParticipationDTO dto) {
        data = dto;
        labelID.setText(
                String.format("ID #%d - (%d) %S",
                        dto.id(),
                        dto.activityId(),
                        dto.activity()
                )
        );
        txtDescription.setText(
                String.format("""
                INFORMACIÓN DE PARTICIPACIÓN:
                USUARIO: %s
                FECHA DE REGISTRO: %s
                FECHA DE INICIO: %s
                FECHA DE Cierre: %s
                ESTADO: %S
                
                RESUMEN DE ACTIVIDAD:
                MODALIDAD: %S
                UBICACIÓN/PLATAFORMA: %s
                
                %s
                """,
                        dto.username(),
                        textFormatter(dto.registerDate()),
                        isDateRegistered(dto.startDate()),
                        isDateRegistered(dto.endDate()),
                        dto.participationState(),

                        dto.isOnSite() ? "PRESENCIAL" : "VIRTUAL",
                        dto.location(),
                        dto.description()
                )
        );
        if(!data.username().equals(AuthSession.getSubject())){
            btnStart.setVisible(false);
            btnStart.setVisible(false);
            btnCancel.setVisible(false);
            btnCancel.setVisible(false);
            return;
        }

        if(data.participationState() != ParticipationState.PENDIENTE){
            btnStart.setVisible(false);
            btnStart.setVisible(false);
        }

        if(data.participationState() == ParticipationState.CANCELADO ||
        data.participationState() == ParticipationState.COMPLETADO){
            btnCancel.setVisible(false);
            btnCancel.setVisible(false);
        }
    }

    @Override
    public void setParentController(BaseSubSceneController parent) {
        this.parentController = parent;
    }

    @FXML
    private void deleteParticipation(ActionEvent event) {
        parentController.getMainController().executeCall(
                () -> service.deleteParticipation(String.valueOf(data.id())),
                (_) -> {
                    UIUXFeedbackUtils.showSuccessSnackbar("Participación eliminada exitosamente");
                    if(parentController instanceof ParticipationController participationController)
                        participationController.refreshCurrentPage();
                }
        );
    }

    @FXML
    private void startParticipation(ActionEvent event) {
        parentController.getMainController().executeCall(
                () -> service.startParticipation(String.valueOf(data.id())),
                (_) -> {
                    UIUXFeedbackUtils.showSuccessSnackbar("Participación iniciada exitosamente");
                    if(parentController instanceof ParticipationController participationController)
                        participationController.refreshCurrentPage();
                }
        );
    }

    @FXML
    private void cancelParticipation(ActionEvent event) {
        parentController.getMainController().executeCall(
                () -> service.cancelParticipation(String.valueOf(data.id())),
                (_) -> {
                    UIUXFeedbackUtils.showSuccessSnackbar("Participación cancelada exitosamente");
                    if(parentController instanceof ParticipationController participationController)
                        participationController.refreshCurrentPage();
                }
        );
    }

    private String textFormatter(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return formatter.format(date);
    }

    private String isDateRegistered(LocalDate date){
        return date == null ? "No Registrada": textFormatter(date);
    }
}
