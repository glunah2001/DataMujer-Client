package com.uned.clientedatamujer.controller.view;

import com.uned.clientedatamujer.dto.response.VolunteeringDTO;
import com.uned.clientedatamujer.service.ActivityService;
import com.uned.clientedatamujer.service.AuthSession;
import com.uned.clientedatamujer.service.VolunteeringService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VolunteeringCardController implements BaseCardController<VolunteeringDTO> {
    @FXML
    private Label labelID;
    @FXML
    private TextArea txtDescription;

    private BaseSubSceneController parentController;
    private VolunteeringDTO data;

    @Override
    public void setData(VolunteeringDTO dto) {
        data = dto;
        labelID.setText(
                String.format("ID VOLUNTARIADO #%d - (#%d) %S",
                        dto.id(),
                        dto.activityId(),
                        dto.activity()
                )
        );
        txtDescription.setText(
                String.format("""
                INFORMACIÓN DE VOLUNTARIADO:
                USUARIO: %S (%S)
                INICIO DE TURNO: %s
                FIN DE TURNO: %s
                
                RESUMEN DE ACTIVIDAD
                MODALIDAD: %S
                UBICACIÓN/PLATAFORMA: %s
                
                %s
                """,
                dto.username(),
                dto.activityRole(),
                textFormatter(dto.startShift()),
                textFormatter(dto.endShift()),
                dto.isOnSite() ? "PRESENCIAL" : "VIRTUAL",
                dto.location(),
                dto.description())
        );
    }

    @Override
    public void setParentController(BaseSubSceneController parent) {
        this.parentController = parent;
    }

    @FXML
    private void deleteVolunteering(ActionEvent event) {
        var service = new VolunteeringService();
        parentController.mainController.executeCall(
                () -> service.deleteVolunteering(AuthSession.getAccessToken(), String.valueOf(data.id())),
                (_) -> {
                    parentController.mainController.showSuccessSnackBar("Voluntariado eliminada exitosamente");
                    if(parentController instanceof VolunteeringController volunteeringController)
                        volunteeringController.refreshCurrentPage();
                }
        );
    }

    @FXML
    private void updateVolunteering(ActionEvent event) {
    }

    private String textFormatter(LocalDateTime date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy  HH:mm");
        return formatter.format(date);
    }
}
