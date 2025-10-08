package com.uned.clientedatamujer.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class LoginController {

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
}
