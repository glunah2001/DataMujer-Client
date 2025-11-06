package com.uned.clientedatamujer.controller.view.card;

import com.uned.clientedatamujer.controller.util.SceneManager;
import com.uned.clientedatamujer.controller.util.UIUXFeedbackUtils;
import com.uned.clientedatamujer.controller.view.base.BaseCardController;
import com.uned.clientedatamujer.controller.view.base.BaseSubSceneController;
import com.uned.clientedatamujer.controller.view.subscene.VolunteeringController;
import com.uned.clientedatamujer.dto.response.ActivityDTO;
import com.uned.clientedatamujer.dto.response.VolunteeringDTO;
import com.uned.clientedatamujer.service.ActivityService;
import com.uned.clientedatamujer.service.util.DataUtilities;
import com.uned.clientedatamujer.service.VolunteeringService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.io.IOException;
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
                USUARIO: %s (%S)
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
        parentController.getMainController().executeCall(
                () -> service.deleteVolunteering(String.valueOf(data.id())),
                (_) -> {
                    UIUXFeedbackUtils.showSuccessSnackbar("Voluntariado eliminada exitosamente");
                    if(parentController instanceof VolunteeringController volunteeringController)
                        volunteeringController.refreshCurrentPage();
                }
        );
    }

    @FXML
    private void updateVolunteering(ActionEvent event) {
        var service = new ActivityService();
        parentController.getMainController().executeCall(
                () -> service.getActivityById(String.valueOf(data.activityId())),
                (ActivityDTO dto) -> {
                    UIUXFeedbackUtils.hideLoading();
                    DataUtilities.setLastActivityDTO(dto);
                    DataUtilities.setLastVolunteeringDTO(data);
                    try{
                        SceneManager.loadSubScene(
                                parentController.getMainController().getSubScenePane(),
                                "/com/uned/clientedatamujer/views/subscene/update-volunteering-subscene.fxml",
                                parentController.getMainController()
                        );
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
        );
    }

    private String textFormatter(LocalDateTime date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy  HH:mm");
        return formatter.format(date);
    }
}
