package com.uned.clientedatamujer.controller.view.subscene;

import com.uned.clientedatamujer.controller.util.SceneManager;
import com.uned.clientedatamujer.controller.util.UIUXFeedbackUtils;
import com.uned.clientedatamujer.controller.view.base.BaseFormController;
import com.uned.clientedatamujer.controller.view.base.BaseSubSceneController;
import com.uned.clientedatamujer.dto.request.ParticipationWrapperDTO;
import com.uned.clientedatamujer.service.AuthSession;
import com.uned.clientedatamujer.service.DataUtilities;
import com.uned.clientedatamujer.service.ParticipationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;

public class NewParticipationController extends BaseSubSceneController {
    @FXML
    private VBox VBoxParticipationForm;
    @FXML
    private Button btnAddParticipation;
    private final ParticipationService service = new ParticipationService();

    @FXML
    private void initialize(){
        addForm();
    }

    @FXML
    private void addParticipation(ActionEvent event) {
        UIUXFeedbackUtils.showLoading();
        addForm();
        UIUXFeedbackUtils.hideLoading();
        DataUtilities.clearVolunteeringDTO();
        DataUtilities.clearPaymentDTO();
        DataUtilities.clearLastContent();
    }

    @FXML
    private void verifyData(ActionEvent event) {
        if (VBoxParticipationForm.getChildren().isEmpty()){
            UIUXFeedbackUtils.showErrorSnackbar("No hay datos para construir su solicitud");
            return;
        }
        var participants = new ArrayList<String>();
        for (Node node : VBoxParticipationForm.getChildren()) {
            var data = getSingle(node);
            if(data == null){
                UIUXFeedbackUtils.showErrorSnackbar("Por favor, complete la información en todos los campos.");
                return;
            }else if(data.isBlank()){
                UIUXFeedbackUtils.showErrorSnackbar("Por favor, complete la información en todos los campos.");
                return;
            }
            participants.add(data);
        }
        if(participants.isEmpty())return;
        var dto = new ParticipationWrapperDTO(
                DataUtilities.getLastActivityDTO().id(),
                participants
        );
        sendData(dto);
    }

    private void sendData(ParticipationWrapperDTO dto){
        mainController.executeCall(
                () -> service.createParticipations(dto),
                ( _) -> {
                    UIUXFeedbackUtils.showSuccessSnackbar("Participaciones insertadas exitosamente" +
                            " para la actividad #"+dto.activityId());
                    mainController.withDelay(4, () ->{
                        UIUXFeedbackUtils.hideLoading();
                        try {
                            DataUtilities.clearActivityDTO();
                            SceneManager.loadSubScene(mainController.getSubScenePane(),
                                    "/com/uned/clientedatamujer/views/subscene/activities-subscene.fxml",
                                    mainController
                            );
                        } catch (IOException e) {
                            UIUXFeedbackUtils.showErrorSnackbar("Ocurrió una corrupción en los datos");
                        }
                    });
                }, "Error en el formulario de participantes."
        );
    }

    private String getSingle(Node node){
        Object userData = node.getUserData();
        if (userData instanceof BaseFormController<?> baseController) {
            Object dto = baseController.sendData();
            if (dto instanceof String username) {
                return username;
            }
        }
        return null;
    }

    private void addForm(){
        setForm(
                "/com/uned/clientedatamujer/views/form/user-form.fxml",
                VBoxParticipationForm,
                this,
                String.class
        );
    }
}
