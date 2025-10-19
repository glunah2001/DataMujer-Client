package com.uned.clientedatamujer.controller.view;

import com.jfoenix.controls.JFXSnackbar;
import com.uned.clientedatamujer.controller.util.ComponentInitializer;
import com.uned.clientedatamujer.controller.util.SceneManager;
import com.uned.clientedatamujer.dto.ApiError;
import com.uned.clientedatamujer.dto.request.CommonRegisterDTO;
import com.uned.clientedatamujer.dto.request.LegalPersonRegisterDTO;
import com.uned.clientedatamujer.enums.Country;
import com.uned.clientedatamujer.service.RegisterService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;

public class LegalPersonController extends BaseController{

    @FXML
    private StackPane rootPane;
    @FXML
    private JFXSnackbar snackBarInfo;
    @FXML
    private TextField txtCedula;
    @FXML
    private TextField txtName;
    @FXML
    private DatePicker datePickerFoundation;
    @FXML
    private TextField txtPhone;
    @FXML
    private ComboBox<Country> comboCountry;
    @FXML
    private TextField txtLocation;
    @FXML
    private TextField txtMail;
    @FXML
    private TextField txtUser;
    @FXML
    private PasswordField txtPassword;

    private final RegisterService service = new RegisterService();

    @FXML
    private void initialize(){
        ComponentInitializer.initializeCountry(comboCountry);
        ComponentInitializer.initializePhone(txtPhone);
        ComponentInitializer.initializeLegalCedula(txtCedula);
        snackBarInfo = new JFXSnackbar(rootPane);
        setRootPane(rootPane);
        setSnackBarInfo(snackBarInfo);
    }

    @FXML
    private void ToLoginView(ActionEvent event) throws IOException {
        SceneManager.toLogIn();
    }

    @FXML
    public void registerPerson(ActionEvent event) {
        var common = getCommonData();
        var legal = getLegalData(common);
        if(!validateFormData(legal)) return;

        showLoading();

        executeCall(
                () -> service.register(legal),
                (String success) -> {
                    showSuccessSnackBar(success);
                    withDelay(4, () ->{
                        hideLoading();
                        try{
                            clearForm();
                            SceneManager.toLogIn();
                        }catch(IOException e){
                            String message = "Corrupción en la ruta de recursos";
                            showErrorSnackBar(message);
                        }
                    });
                },
                "Error en los datos de registro"
        );
    }

    private boolean isCountrySelected() {
        Country selected = comboCountry.getValue();
        return selected != null;
    }

    private CommonRegisterDTO getCommonData(){
        return new CommonRegisterDTO(
                txtUser.getText().trim(),
                txtMail.getText().trim(),
                txtPassword.getText().trim(),
                txtPhone.getText().trim(),
                comboCountry.getValue(),
                txtLocation.getText().trim()
        );
    }
    private LegalPersonRegisterDTO getLegalData(CommonRegisterDTO common){
        return new LegalPersonRegisterDTO(
                common,
                txtCedula.getText().trim(),
                txtName.getText().trim(),
                datePickerFoundation.getValue()
        );
    }

    private boolean validateFormData(LegalPersonRegisterDTO legal){
        if(!isCountrySelected()) {
            showErrorSnackBar("Seleccione por favor un país de residencia.");
            return false;
        }
        if(legal.foundationDate() == null){
            showErrorSnackBar("Por favor, indique su fecha de nacimiento.");
            return false;
        }

        if(legal.businessName().isEmpty() || legal.commonRegisterDTO().phoneNumber().isEmpty() ||
                legal.commonRegisterDTO().location().isEmpty() || legal.commonRegisterDTO().email().isEmpty() ||
                legal.commonRegisterDTO().username().isEmpty() || legal.commonRegisterDTO().password().isEmpty() ||
                legal.legalId().isEmpty()){
            showErrorSnackBar("Por favor, rellene todos los datos.");
            return false;
        }

        if(datePickerFoundation.getValue().isAfter(LocalDate.now())){
            showErrorSnackBar("Seleccione una fecha de fundación válida");
            return false;
        }

        return true;
    }

    private void clearForm() {
        txtUser.clear();
        txtMail.clear();
        txtPassword.clear();
        txtPhone.clear();
        txtLocation.clear();
        txtCedula.clear();
        txtName.clear();
    }

}
