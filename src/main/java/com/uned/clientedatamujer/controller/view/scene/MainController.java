package com.uned.clientedatamujer.controller.view.scene;

import com.jfoenix.controls.JFXSnackbar;
import com.uned.clientedatamujer.controller.util.SceneManager;
import com.uned.clientedatamujer.controller.util.UIUXFeedbackUtils;
import com.uned.clientedatamujer.controller.view.base.BaseController;
import com.uned.clientedatamujer.service.util.AuthSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MainController extends BaseController {
    @FXML
    private JFXSnackbar snackBarInfo;
    @FXML
    private StackPane rootPane;
    @FXML
    private Button btnNewActivity;
    @FXML
    private Button btnMyVolunteering;
    @FXML
    private Button btnAdminOps;
    @FXML
    private StackPane SubScenePane;

    @FXML
    private void initialize(){
        snackBarInfo = new JFXSnackbar(rootPane);
        UIUXFeedbackUtils.setRootPane(rootPane);
        UIUXFeedbackUtils.setSnackbar(snackBarInfo);
        UIUXFeedbackUtils.showLoading();
        restrictButtons();
        UIUXFeedbackUtils.hideLoading();
        UIUXFeedbackUtils.showSuccessSnackbar("Bienvenido "+AuthSession.getSubject());
    }

    @FXML
    private void toMyProfile(ActionEvent event) {
        int width = (int) rootPane.getWidth();
        int height = (int) rootPane.getHeight();
        try {
            SceneManager.toMyProfile(width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void restrictButtons(){
        resetButtons();
        String role = AuthSession.getRole();
        if(role.equals("ROLE_ADMIN")) {
            return;
        }else{
            btnAdminOps.setVisible(false);
            btnAdminOps.setManaged(false);
        }

        if(role.equals("ROLE_STANDARD")){
            btnNewActivity.setVisible(false);
            btnNewActivity.setManaged(false);
            btnMyVolunteering.setVisible(false);
            btnMyVolunteering.setManaged(false);
        }
    }

    private void resetButtons(){
        btnNewActivity.setVisible(true);
        btnNewActivity.setManaged(true);
        btnMyVolunteering.setVisible(true);
        btnMyVolunteering.setManaged(true);
        btnAdminOps.setVisible(true);
        btnAdminOps.setManaged(true);
    }

    @FXML
    private void loadNewActivity(ActionEvent event) {
        if(AuthSession.getRole().equals("STANDARD")){
            SubScenePane.getChildren().clear();
            UIUXFeedbackUtils.showErrorSnackbar("Usted no cuenta con la autorización para realizar esta operación.");
            return;
        }

        try {
            SceneManager.loadSubScene(
                    SubScenePane,
                    "/com/uned/clientedatamujer/views/subscene/new-activity-subscene.fxml",
                    this
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void loadActivities(ActionEvent event) {
        try{
            SceneManager.loadSubScene(
                    SubScenePane,
                    "/com/uned/clientedatamujer/views/subscene/activities-subscene.fxml",
                    this
            );
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void loadVolunteering(ActionEvent event) {
        try{
            SceneManager.loadSubScene(
                    SubScenePane,
                    "/com/uned/clientedatamujer/views/subscene/volunteering-subscene.fxml",
                    this
            );
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void loadParticipation(ActionEvent event){
        try{
            SceneManager.loadSubScene(
                    SubScenePane,
                    "/com/uned/clientedatamujer/views/subscene/participations-subscene.fxml",
                    this
            );
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void loadNewPayment(ActionEvent event) {
        try{
            SceneManager.loadSubScene(
                    SubScenePane,
                    "/com/uned/clientedatamujer/views/subscene/new-payment-subscene.fxml",
                    this
            );
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void loadPayments(ActionEvent event) {
        try{
            SceneManager.loadSubScene(
                    SubScenePane,
                    "/com/uned/clientedatamujer/views/subscene/payment-subscene.fxml",
                    this
            );
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void loadAdminOps(ActionEvent event) {
        try{
            SceneManager.loadSubScene(
                    SubScenePane,
                    "/com/uned/clientedatamujer/views/subscene/admin-subscene.fxml",
                    this
            );
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public StackPane getSubScenePane() {return SubScenePane;}
    public void forceLoadPayments(){loadPayments(null);}
    public void forceLoadVolunteering(){loadVolunteering(null);}
}
