package com.uned.clientedatamujer.controller.view;

import com.jfoenix.controls.JFXToggleButton;
import com.uned.clientedatamujer.controller.util.ComponentInitializer;
import com.uned.clientedatamujer.controller.util.SceneManager;
import com.uned.clientedatamujer.dto.request.BaseVolunteeringRegisterDTO;
import com.uned.clientedatamujer.dto.request.VolunteeringRegisterDTO;
import com.uned.clientedatamujer.dto.request.VolunteeringWrapperDTO;
import com.uned.clientedatamujer.dto.response.VolunteeringDTO;
import com.uned.clientedatamujer.service.AuthSession;
import com.uned.clientedatamujer.service.DataUtilities;
import com.uned.clientedatamujer.service.VolunteeringService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;

public class NewVolunteeringController extends BaseSubSceneController{

    @FXML
    private VBox VBoxVolunteeringForm;
    @FXML
    private JFXToggleButton toggleVolunteeringType;
    @FXML
    private Button btnAddVolunteering;
    private final VolunteeringService service = new VolunteeringService();

    @FXML
    private void initialize(){
        ComponentInitializer.initializeToggle(
                toggleVolunteeringType,
                "Mi voluntariado",
                "Multiple"
        );
        Platform.runLater(() -> {
            toggleVolunteeringType.setSelected(false);
            changeVolunteeringType(null);
        });
    }


    @FXML
    private void addVolunteering(ActionEvent event) {
        mainController.showLoading();
        addForm();
        mainController.hideLoading();
    }

    @FXML
    private void verifyData(ActionEvent event) {
        if (VBoxVolunteeringForm.getChildren().isEmpty()){
            mainController.showErrorSnackBar("No hay datos para construir su solicitud");
            return;
        }
        if(isToggleActive()){
            var data = getMultiple();
            if(data == null){
                mainController.showErrorSnackBar("Complete toda la información de los voluntariados.");
                return;
            }
            if(!validateEmptyData(data)){return;}
            sendData(data);
        }else{
            var data = getSingle(VBoxVolunteeringForm.getChildren().getFirst());
            if(data == null){
                mainController.showErrorSnackBar("Complete toda la información su voluntariado.");
                return;
            }
            if(validateRole(data.volunteeringData())){
                mainController.showErrorSnackBar("Complete toda la información su voluntariado.");
                return;
            }
            sendData(data.volunteeringData());
        }
    }

    @FXML
    private void changeVolunteeringType(ActionEvent event) {
        mainController.showLoading();
        VBoxVolunteeringForm.getChildren().clear();
        addForm();
        btnAddVolunteering.setVisible(isToggleActive());
        btnAddVolunteering.setManaged(isToggleActive());
        mainController.hideLoading();
    }

    private void addForm(){
        setForm(
                "/com/uned/clientedatamujer/views/form/volunteering-form.fxml",
                VBoxVolunteeringForm,
                this,
                VolunteeringRegisterDTO.class
        );
    }

    private VolunteeringRegisterDTO getSingle(Node node){
        Object userData = node.getUserData();
        if (userData instanceof BaseFormController<?> baseController) {
            Object dto = baseController.sendData();
            if (dto instanceof VolunteeringRegisterDTO volunteeringDTO) {
                return volunteeringDTO;
            }
        }
        return null;
    }

    private VolunteeringWrapperDTO getMultiple(){
        var volunteering = new ArrayList<VolunteeringRegisterDTO>();
        for (Node node : VBoxVolunteeringForm.getChildren()) {
            var data = getSingle(node);
            if(data == null) return null;
            volunteering.add(data);
        }
        return new VolunteeringWrapperDTO(
                DataUtilities.getLastActivityDTO().id(),
                volunteering
        );
    }

    private void sendData(BaseVolunteeringRegisterDTO dto){
        mainController.executeCall(
                () -> service.createVolunteering(AuthSession.getAccessToken(), dto),
                (VolunteeringDTO response) ->{
                    mainController.showSuccessSnackBar("Se ha insertado su voluntariado Id#"+response.id());
                    mainController.withDelay(4, () ->{
                        mainController.hideLoading();
                        try {
                            DataUtilities.clearActivityDTO();
                            SceneManager.loadSubScene(mainController.getSubScenePane(),
                                    "/com/uned/clientedatamujer/views/subscene/activities-subscene.fxml",
                                    mainController,
                                    rootPane,
                                    snackBarInfo);
                        } catch (IOException e) {
                            mainController.showErrorSnackBar("Ocurrió una corrupción en los datos");
                        }
                    });
                }, "Error en el formulario de voluntariado."
        );
    }

    private void sendData(VolunteeringWrapperDTO dto){
        mainController.executeCall(
                () -> service.createVolunteering(AuthSession.getAccessToken(), dto),
                (_) ->{
                    mainController.showSuccessSnackBar("Voluntariados insertados exitosamente" +
                            " para la actividad #"+dto.activityId());
                    mainController.withDelay(4, () ->{
                        mainController.hideLoading();
                        try {
                            DataUtilities.clearActivityDTO();
                            SceneManager.loadSubScene(mainController.getSubScenePane(),
                                    "/com/uned/clientedatamujer/views/subscene/activities-subscene.fxml",
                                    mainController,
                                    rootPane,
                                    snackBarInfo);
                        } catch (IOException e) {
                            mainController.showErrorSnackBar("Ocurrió una corrupción en los datos");
                        }
                    });
                }, "Error en el formulario de voluntariado."
        );
    }

    private boolean validateEmptyData(VolunteeringWrapperDTO dto){
        if (dto.volunteering().stream()
                .anyMatch(
                        v ->
                                v.username() == null || v.username().isBlank())
        ){
            mainController.showErrorSnackBar("Existe un formulario que no indica el usuario.");
            return false;
        }

        if(dto.volunteering().stream()
                .map(VolunteeringRegisterDTO::volunteeringData)
                .anyMatch(this::validateRole)){
            mainController.showErrorSnackBar("Existe un formulario que no indica el rol.");
            return false;
        }
        return true;
    }

    private boolean validateRole(BaseVolunteeringRegisterDTO dto){
        return dto == null || dto.activityRole().isBlank();
    }

    public boolean isToggleActive(){
        return toggleVolunteeringType.isSelected();
    }
}
