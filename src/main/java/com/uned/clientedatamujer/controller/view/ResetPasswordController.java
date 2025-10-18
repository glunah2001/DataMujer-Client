package com.uned.clientedatamujer.controller.view;

import com.jfoenix.controls.JFXSnackbar;
import com.uned.clientedatamujer.controller.util.SceneManager;
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
        setRootPane(rootPane);
        setSnackBarInfo(snackbarInfo);
    }

    @FXML
    private void ToLoginView(ActionEvent event) throws IOException {
        SceneManager.toLogIn();
    }

    @FXML
    private void resetPassword(){
        String token = txtToken.getText().trim();
        String newPassword = txtNewPassword.getText().trim();
        if(token.isEmpty() || newPassword.isEmpty()){
            showErrorSnackBar("No se puede hacer un reset de contraseña si no completa todos los datos.");
            return;
        }
        txtToken.clear();
        txtNewPassword.clear();

        ResetPasswordDTO dto = new ResetPasswordDTO(token, newPassword);

        executeCall(
                () -> service.resetPassword(dto),
                (String success) -> {
                    showSuccessSnackBar(success);
                    withDelay(3, ()->{
                        hideLoading();
                        try{
                            SceneManager.toLogIn();
                        }catch(IOException e){
                            String message = "Corrupción en la ruta de recursos";
                            showErrorSnackBar(message);
                        }
                    });
                },
                (ApiError error) -> {
                    if(error.error().equalsIgnoreCase("BAD REQUEST")
                            && error.details() == null){
                        withDelay(3,() ->{
                            hideLoading();
                            try {
                                SceneManager.toLogIn();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        return;
                    }
                    hideLoading();
                }
                ,"No fue posible restablecer su contraseña."
        );
    }
}
