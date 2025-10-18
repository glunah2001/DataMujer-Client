package com.uned.clientedatamujer.controller.view;

import com.jfoenix.controls.JFXSnackbar;
import com.uned.clientedatamujer.controller.util.SceneManager;
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

public class LoginController extends BaseController{

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
        setRootPane(rootPane);
        setSnackBarInfo(snackBarInfo);
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
            showErrorSnackBar("Ingrese sus datos de acceso.");
            return;
        }

        var dto = new UserLoginDTO(user, password);

        showLoading();
        runAsync(()->{
            try{
                Object object = service.login(dto);
                if(object instanceof TokenResponse response){
                    hideLoading();
                    runLater(() ->{
                        try{
                            AuthSession.setTokens(response);
                            SceneManager.toMainView(1080, 720);
                        }catch(IOException e){
                            String message = "Corrupción en la ruta de recursos";
                            showErrorSnackBar(message);
                        }
                    });
                }else if(object instanceof ApiError error){
                    runLater(()->{
                        handleApiError(
                                error,
                                "Error con los datos de inicio de sesión."
                        );
                        hideLoading();
                        clearForm();
                    });
                }
            }catch(Exception e){
                runLater(this::hideLoading);
                e.printStackTrace();
            }
        });
    }

    private void clearForm(){
        txtUser.clear();
        txtPassword.clear();
    }
}
