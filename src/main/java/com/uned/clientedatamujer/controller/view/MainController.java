package com.uned.clientedatamujer.controller.view;

import com.jfoenix.controls.JFXSnackbar;
import com.uned.clientedatamujer.controller.util.SceneManager;
import com.uned.clientedatamujer.service.AuthSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MainController extends BaseController{
    @FXML
    private JFXSnackbar snackBarInfo;
    @FXML
    private StackPane rootPane;
    @FXML
    private Button btnNewActivity;
    @FXML
    private Button btnMyVolunteering;
    @FXML
    private Button btnGenReport;
    @FXML
    private Button btnAdminOps;
    @FXML
    private StackPane SubScenePane;

    @FXML
    private void initialize(){
        snackBarInfo = new JFXSnackbar(rootPane);
        setRootPane(rootPane);
        setSnackBarInfo(snackBarInfo);
        showLoading();
        restrictButtons();
        hideLoading();
        showSuccessSnackBar("Bienvenido "+AuthSession.getSubject());
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
            btnGenReport.setVisible(false);
            btnGenReport.setManaged(false);
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
        btnGenReport.setVisible(true);
        btnGenReport.setManaged(true);
        btnAdminOps.setVisible(true);
        btnAdminOps.setManaged(true);
    }

    @FXML
    private void loadNewActivity(ActionEvent event) {
        if(AuthSession.getRole().equals("STANDARD")){
            SubScenePane.getChildren().clear();
            showErrorSnackBar("Usted no cuenta con la autorización para realizar esta operación.");
            return;
        }

        try {
            SceneManager.loadSubScene(
                    SubScenePane,
                    "/com/uned/clientedatamujer/new-activity-subscene.fxml",
                    this,
                    rootPane,
                    snackBarInfo
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
                    "/com/uned/clientedatamujer/activities-subscene.fxml",
                    this,
                    rootPane,
                    snackBarInfo
            );
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void loadVolunteering(ActionEvent event) {
        try{
            SceneManager.loadSubScene(
                    SubScenePane,
                    "/com/uned/clientedatamujer/volunteering-subscene.fxml",
                    this,
                    rootPane,
                    snackBarInfo
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
                    "/com/uned/clientedatamujer/participations-subscene.fxml",
                    this,
                    rootPane,
                    snackBarInfo
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
                    "/com/uned/clientedatamujer/new-payment-subscene.fxml",
                    this,
                    rootPane,
                    snackBarInfo
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
                    "/com/uned/clientedatamujer/payment-subscene.fxml",
                    this,
                    rootPane,
                    snackBarInfo
            );
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public StackPane getSubScenePane() {return SubScenePane;}


}
