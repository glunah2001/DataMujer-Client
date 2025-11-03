package com.uned.clientedatamujer.controller.view;

import com.uned.clientedatamujer.controller.util.SceneManager;
import com.uned.clientedatamujer.dto.response.ActivityDTO;
import com.uned.clientedatamujer.dto.response.ParticipationDTO;
import com.uned.clientedatamujer.service.ActivityService;
import com.uned.clientedatamujer.service.AuthSession;
import com.uned.clientedatamujer.service.DataUtilities;
import com.uned.clientedatamujer.service.ParticipationService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ActivityCardController implements BaseCardController<ActivityDTO>{

    public Button btnMultipleParticipation;
    @FXML
    private Label labelID;
    @FXML
    private TextArea txtDescription;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnVolunteering;
    private BaseSubSceneController parentController;
    private ActivityDTO data;

    @FXML
    private void initialize(){
        btnMultipleParticipation.setVisible(!AuthSession.getRole().equals("ROLE_STANDARD"));
        btnMultipleParticipation.setManaged(!AuthSession.getRole().equals("ROLE_STANDARD"));
    }


    @FXML
    private void deleteActivity(ActionEvent event) {
        var service = new ActivityService();
        parentController.mainController.executeCall(
                () -> service.deleteActivity(AuthSession.getAccessToken(), String.valueOf(data.id())),
                (_) -> {
                    parentController.mainController.hideLoading();
                    parentController.mainController.showSuccessSnackBar("Actividad eliminada exitosamente");
                    if(parentController instanceof ActivityController activityController)
                        activityController.refreshCurrentPage();
                }
        );
    }

    @FXML
    private void applyToVolunteering(ActionEvent event) {
        DataUtilities.setLastActivityDTO(data);
        if(AuthSession.getRole().equals("STANDARD")){
            parentController.mainController.getSubScenePane().getChildren().clear();
            parentController.mainController.showErrorSnackBar(
                    "Usted no cuenta con la autorización para realizar esta operación."
            );
            return;
        }
        try{
            SceneManager.loadSubScene(
                    parentController.mainController.getSubScenePane(),
                    "/com/uned/clientedatamujer/views/subscene/new-volunteering-subscene.fxml",
                    parentController.mainController,
                    parentController.rootPane,
                    parentController.snackBarInfo
            );
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void applyToParticipate(ActionEvent event) {
        createParticipation();
    }

    @Override
    public void setData(ActivityDTO dto){
        data = dto;
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


    @Override
    public void setParentController(BaseSubSceneController parent) {
        this.parentController = parent;
    }

    private void createParticipation(){
        var service = new ParticipationService();
        parentController.mainController.executeCall(
                () -> service.createParticipation(AuthSession.getAccessToken(), data.id()),
                (ParticipationDTO dto) -> {
                    parentController.mainController.hideLoading();
                    parentController.mainController.showSuccessSnackBar(
                            "Se ha registrado exitosamente en la actividad "+dto.activityId()
                                    + " compruébelo en la sección \"Mis Participaciones\""
                    );
                }
        );
    }

    public void multipleParticipation(ActionEvent event) {
        DataUtilities.setLastActivityDTO(data);
        try{
            SceneManager.loadSubScene(
                    parentController.mainController.getSubScenePane(),
                    "/com/uned/clientedatamujer/views/subscene/new-participation-subscene.fxml",
                    parentController.mainController,
                    parentController.rootPane,
                    parentController.snackBarInfo
            );
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
