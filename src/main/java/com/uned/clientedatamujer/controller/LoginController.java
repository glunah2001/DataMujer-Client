package com.uned.clientedatamujer.controller;

import com.jfoenix.controls.JFXSnackbar;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class LoginController extends BaseController{

    @FXML
    private StackPane rootPane;
    @FXML
    private JFXSnackbar snackBarInfo;
    @FXML
    private TextField txtUser;
    @FXML
    private PasswordField txtPassword;

    @FXML
    private void initialize(){
        snackBarInfo = new JFXSnackbar(rootPane);
    }

    @FXML
    private void ToForgotPasswordView(ActionEvent event) throws IOException {
        SceneManager.changeScene(
                "/com/uned/clientedatamujer/forgot-password-view.fxml",
                1080, 720, false
        );
    }

    @FXML
    private void ToTypePersonView(ActionEvent event) throws IOException {
        SceneManager.changeScene(
                "/com/uned/clientedatamujer/type-person-view.fxml",
                1080, 720, false
        );
    }

    public void login(ActionEvent event) {
        String user = txtUser.getText().trim();
        String password = txtPassword.getText().trim();
        if(user.isEmpty() || password.isEmpty()){
            showErrorSnackBar("Ingrese sus datos de acceso.", snackBarInfo);
            return;
        }
    }

    private void clearForm(){
        txtUser.clear();
        txtPassword.clear();
    }
}
