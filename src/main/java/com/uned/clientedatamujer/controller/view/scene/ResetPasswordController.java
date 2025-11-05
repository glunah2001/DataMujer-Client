package com.uned.clientedatamujer.controller.view.scene;

import com.jfoenix.controls.JFXSnackbar;
import com.uned.clientedatamujer.controller.util.SceneManager;
import com.uned.clientedatamujer.controller.util.UIUXFeedbackUtils;
import com.uned.clientedatamujer.controller.view.base.BaseController;
import com.uned.clientedatamujer.dto.ApiError;
import com.uned.clientedatamujer.dto.authentication.ResetPasswordDTO;
import com.uned.clientedatamujer.service.AuthService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class ResetPasswordController extends BaseController {

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
        UIUXFeedbackUtils.setRootPane(rootPane);
        UIUXFeedbackUtils.setSnackbar(snackbarInfo);
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
            UIUXFeedbackUtils.showErrorSnackbar("No se puede hacer un reset de contraseña si no completa todos los datos.");
            return;
        }
        txtToken.clear();
        txtNewPassword.clear();

        ResetPasswordDTO dto = new ResetPasswordDTO(token, newPassword);

        executeCall(
                () -> service.resetPassword(dto),
                (String success) -> {
                    UIUXFeedbackUtils.showSuccessSnackbar(success);
                    withDelay(3, ()->{
                        UIUXFeedbackUtils.hideLoading();
                        try{
                            SceneManager.toLogIn();
                        }catch(IOException e){
                            String message = "Corrupción en la ruta de recursos";
                            UIUXFeedbackUtils.showErrorSnackbar(message);
                        }
                    });
                },
                (ApiError error) -> {
                    if(error.error().equalsIgnoreCase("BAD REQUEST")
                            && error.details() == null){
                        withDelay(3,() ->{
                            UIUXFeedbackUtils.hideLoading();
                            try {
                                SceneManager.toLogIn();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        return;
                    }
                    UIUXFeedbackUtils.hideLoading();
                }
                ,"No fue posible restablecer su contraseña."
        );
    }
}
