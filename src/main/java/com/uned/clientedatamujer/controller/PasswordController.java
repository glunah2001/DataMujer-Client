package com.uned.clientedatamujer.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class PasswordController {

    @FXML
    private StackPane rootPane;

    @FXML
    private void ToLoginView(ActionEvent event) throws IOException {
        SceneManager.toLogIn();
    }

    @FXML
    private void ToResetPasswordView(ActionEvent event) throws IOException{
        SceneManager.changeScene(
                "/com/uned/clientedatamujer/reset-password-view.fxml",
                1080, 720, false
        );
    }

}
