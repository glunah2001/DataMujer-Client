package com.uned.clientedatamujer.controller.view.scene;

import com.jfoenix.controls.JFXSnackbar;
import com.uned.clientedatamujer.controller.util.SceneManager;
import com.uned.clientedatamujer.controller.util.UIUXFeedbackUtils;
import com.uned.clientedatamujer.controller.view.base.BaseController;
import com.uned.clientedatamujer.dto.ApiError;
import com.uned.clientedatamujer.dto.authentication.UserLoginDTO;
import com.uned.clientedatamujer.dto.token.TokenResponse;
import com.uned.clientedatamujer.service.AuthService;
import com.uned.clientedatamujer.service.AuthSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class LoginController extends BaseController {

    @FXML
    private StackPane rootPane;
    @FXML
    private JFXSnackbar snackBarInfo;
    @FXML
    private TextField txtUser;
    @FXML
    private PasswordField txtPassword;
    private final AuthService service = new AuthService();

    @FXML
    private void initialize(){
        snackBarInfo = new JFXSnackbar(rootPane);
        UIUXFeedbackUtils.setRootPane(rootPane);
        UIUXFeedbackUtils.setSnackbar(snackBarInfo);
    }

    @FXML
    private void ToForgotPasswordView(ActionEvent event) throws IOException {
        SceneManager.changeScene(
                "/com/uned/clientedatamujer/views/scene/forgot-password-view.fxml",
                1080, 720, false
        );
    }

    @FXML
    private void ToTypePersonView(ActionEvent event) throws IOException {
        SceneManager.changeScene(
                "/com/uned/clientedatamujer/views/scene/type-person-view.fxml",
                1080, 720, false
        );
    }

    public void login(ActionEvent event) {
        String user = txtUser.getText().trim();
        String password = txtPassword.getText().trim();
        if(user.isEmpty() || password.isEmpty()){
            UIUXFeedbackUtils.showErrorSnackbar("Ingrese sus datos de acceso.");
            return;
        }

        var dto = new UserLoginDTO(user, password);

        executeCall(
                () -> service.login(dto),
                (TokenResponse response) -> {
                    UIUXFeedbackUtils.hideLoading();
                    try{
                        AuthSession.setTokens(response);
                        SceneManager.toMainView(1080, 720);
                    }catch(IOException e){
                        UIUXFeedbackUtils.showErrorSnackbar("Corrupción en la ruta de recursos");
                    }
                },
                (_) -> {
                    UIUXFeedbackUtils.hideLoading();
                    clearForm();
                },
                "Error con los datos de inicio de sesión."
        );
    }

    private void clearForm(){
        txtUser.clear();
        txtPassword.clear();
    }
}
