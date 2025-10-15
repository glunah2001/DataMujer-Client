package com.uned.clientedatamujer.controller;

import com.jfoenix.controls.JFXSnackbar;
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
    }


    @FXML
    private void ToLoginView(ActionEvent event) throws IOException {
        SceneManager.toLogIn();
    }

    @FXML
    private void ToResetPasswordView(ActionEvent event){
        String email = txtEmail.getText().trim();
        if (email.isEmpty()){
            showErrorSnackBar("Ingrese su correo electrónico.", snackbarInfo);
            return;
        }
        txtEmail.clear();

        showLoading(rootPane);
        runAsync(() ->{
            try{
                Object result = service.forgotPassword(email);

                if(result instanceof String success){
                    runLater(() ->{
                        showSuccessSnackBar(success,snackbarInfo);
                        withDelay(4, () ->{
                            hideLoading(rootPane);
                            try{
                                SceneManager.changeScene(
                                        "/com/uned/clientedatamujer/reset-password-view.fxml",
                                        1080, 720, false
                                );
                            }catch(IOException e){
                                String message = "Corrupción en la ruta de recursos";
                                showErrorSnackBar(message, snackbarInfo);
                            }
                        });
                    });
                }else if(result instanceof ApiError errorDto){
                    runLater(() -> {
                        handleApiError(
                                rootPane,
                                snackbarInfo,
                                errorDto,
                                null
                        );
                        hideLoading(rootPane);
                    });
                }
            }catch(Exception e){
                runLater(() ->{
                    hideLoading(rootPane);
                });
                e.printStackTrace();
            }
        });
    }
}
