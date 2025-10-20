package com.uned.clientedatamujer.controller.view;

import com.jfoenix.controls.JFXSnackbar;
import com.uned.clientedatamujer.controller.util.SceneManager;
import com.uned.clientedatamujer.dto.ApiError;
import com.uned.clientedatamujer.service.AuthService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class ForgotPasswordController extends  BaseController{

    @FXML
    private JFXSnackbar snackbarInfo;
    @FXML
    private StackPane rootPane;
    @FXML
    private TextField txtEmail;

    private final AuthService service = new AuthService();

    @FXML
    private void initialize(){
        snackbarInfo = new JFXSnackbar(rootPane);
        setRootPane(rootPane);
        setSnackBarInfo(snackbarInfo);
    }


    @FXML
    private void ToLoginView(ActionEvent event) throws IOException {
        SceneManager.toLogIn();
    }

    @FXML
    private void ToResetPasswordView(ActionEvent event){
        String email = txtEmail.getText().trim();
        if (email.isEmpty()){
            showErrorSnackBar("Ingrese su correo electrónico.");
            return;
        }
        txtEmail.clear();

        executeCall(
                () -> service.forgotPassword(email),
                (String success) -> {
                    showSuccessSnackBar(success);
                    withDelay(4, () ->{
                        hideLoading();
                        try{
                            SceneManager.changeScene(
                                    "/com/uned/clientedatamujer/reset-password-view.fxml",
                                    1080, 720, false
                            );
                        }catch(IOException e){
                            String message = "Corrupción en la ruta de recursos";
                            showErrorSnackBar(message);
                        }
                    });
                }
        );
    }
}
