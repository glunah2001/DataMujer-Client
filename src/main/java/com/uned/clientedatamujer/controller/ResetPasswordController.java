package com.uned.clientedatamujer.controller;

import com.jfoenix.controls.JFXSnackbar;
import com.uned.clientedatamujer.dto.ApiError;
import com.uned.clientedatamujer.dto.authentication.ResetPasswordDTO;
import com.uned.clientedatamujer.service.AuthService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class ResetPasswordController extends BaseController{

    @FXML
    private TextField txtToken;
    @FXML
    private PasswordField txtNewPassword;
    @FXML
    private JFXSnackbar snackbarInfo;
    @FXML
    private StackPane rootPane;

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
    private void resetPassword(){
        String token = txtToken.getText().trim();
        String newPassword = txtNewPassword.getText().trim();
        if(token.isEmpty() || newPassword.isEmpty()) return;
        txtToken.clear();
        txtNewPassword.clear();

        showLoading(rootPane);
        ResetPasswordDTO dto = new ResetPasswordDTO(token, newPassword);

        runAsync(() ->{
            try{
                Object result = service.resetPassword(dto);
                if(result instanceof String success){
                    runLater(() ->{
                        showSuccessSnackBar(success, snackbarInfo);
                        withDelay(2, ()->{
                            hideLoading(rootPane);
                            try{
                                SceneManager.toLogIn();
                            }catch(IOException e){
                                String message = "Corrupción en la ruta de recursos";
                                showErrorSnackBar(message, snackbarInfo);
                            }
                        });
                    });
                }else if(result instanceof ApiError errorDto){
                    runLater(() ->{
                        handleApiError(
                                rootPane,
                                snackbarInfo,
                                errorDto,
                                "No fue posible restablecer su contraseña."
                        );
                        if(errorDto.error().equalsIgnoreCase("BAD REQUEST")
                        && errorDto.details() == null){
                            withDelay(2,() ->{
                                hideLoading(rootPane);
                                try {
                                    SceneManager.toLogIn();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                        }else{
                            hideLoading(rootPane);
                        }
                    });
                }
            }catch(Exception e){
                hideLoading(rootPane);
                e.printStackTrace();
            }
        });
    }
}
