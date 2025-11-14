package com.uned.clientedatamujer.controller.view.scene;

import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXToggleButton;
import com.uned.clientedatamujer.controller.util.ComponentInitializer;
import com.uned.clientedatamujer.controller.util.SceneManager;
import com.uned.clientedatamujer.controller.util.UIUXFeedbackUtils;
import com.uned.clientedatamujer.controller.view.base.BaseController;
import com.uned.clientedatamujer.dto.request.CommonRegisterDTO;
import com.uned.clientedatamujer.dto.request.PhysicalPersonRegisterDTO;
import com.uned.clientedatamujer.dto.response.PhysicalPersonDTO;
import com.uned.clientedatamujer.enums.Country;
import com.uned.clientedatamujer.service.RegisterService;
import com.uned.clientedatamujer.service.util.TermsReader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;

public class PhysicalPersonController extends BaseController {

    @FXML
    private StackPane rootPane;
    @FXML
    private JFXSnackbar snackBarInfo;
    @FXML
    private TextField txtCedula;
    @FXML
    private JFXToggleButton toggleDocumentType;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtFSurname;
    @FXML
    private TextField txtSSurname;
    @FXML
    private DatePicker dtpBirthDate;
    @FXML
    private TextField txtPhone;
    @FXML
    private ComboBox<Country> comboCountry;
    @FXML
    private TextField txtProfession;
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
        snackBarInfo = new JFXSnackbar(rootPane);
        UIUXFeedbackUtils.setRootPane(rootPane);
        UIUXFeedbackUtils.setSnackbar(snackBarInfo);
        ComponentInitializer.initializeCountry(comboCountry);
        ComponentInitializer.initializeToggle(
                toggleDocumentType,
                "Cédula Nacional",
                "DIMEX"
                );
        ComponentInitializer.initializePhone(txtPhone);
        ComponentInitializer.initializePhysicalCedula(txtCedula, toggleDocumentType);
    }

    @FXML
    private void ToLoginView(ActionEvent event) throws IOException {
        SceneManager.toLogIn();
    }

    @FXML
    public void registerPerson(ActionEvent event) {
        var common = getCommonData();
        var physical = getPhysicalData(common);
        if(!validateFormData(physical)) return;

        String dataMujerRules = TermsReader.loadText("/com/uned/clientedatamujer/terms/DataMujerRules.txt");
        String law = TermsReader.loadText("/com/uned/clientedatamujer/terms/LeyN8968.txt");

        UIUXFeedbackUtils.showTwoStepTermsDialog(
                "TERMINOS Y CONDICIONES: \nReglamento Data Mujer",
                dataMujerRules,
                "TERMINOS Y CONDICIONES: \nLey N° 8968",
                law,
                () -> {register(physical);}
        );
    }

    @FXML
    public void clearCedula(ActionEvent event) {txtCedula.clear();}

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

    private void register(PhysicalPersonRegisterDTO physical){
        executeCall(
                () -> service.register(physical),
                (String success) -> {
                    UIUXFeedbackUtils.showSuccessSnackbar(success);
                    withDelay(4, () ->{
                        UIUXFeedbackUtils.hideLoading();
                        try{
                            clearForm();
                            SceneManager.toLogIn();
                        }catch(IOException e){
                            String message = "Corrupción en la ruta de recursos";
                            UIUXFeedbackUtils.showErrorSnackbar(message);
                        }
                    });
                },
                "Error en los datos de registro"
        );
    }

    private PhysicalPersonRegisterDTO getPhysicalData(CommonRegisterDTO common){
        return new PhysicalPersonRegisterDTO(common,
                txtCedula.getText().trim(),
                txtFSurname.getText().trim(),
                txtSSurname.getText().trim(),
                txtName.getText().trim(),
                txtProfession.getText().trim(),
                dtpBirthDate.getValue()
                );
    }

    private boolean validateFormData(PhysicalPersonRegisterDTO physical){
        if(ComponentInitializer.isComboBoxValueNull(comboCountry)) {
            UIUXFeedbackUtils.showErrorSnackbar("Seleccione por favor un país de residencia.");
            return false;
        }
        if(physical.birthDate() == null){
            UIUXFeedbackUtils.showErrorSnackbar("Por favor, indique su fecha de nacimiento.");
            return false;
        }

        if(physical.name().isEmpty() || physical.firstSurname().isEmpty() ||
        physical.secondSurname().isEmpty() || physical.commonRegisterDTO().phoneNumber().isEmpty() ||
        physical.commonRegisterDTO().location().isEmpty() || physical.profession().isEmpty() ||
        physical.commonRegisterDTO().email().isEmpty() || physical.commonRegisterDTO().username().isEmpty() ||
        physical.commonRegisterDTO().password().isEmpty() || physical.nationalId().isEmpty()){
            UIUXFeedbackUtils.showErrorSnackbar("Por favor, rellene todos los datos.");
            return false;
        }


        int age = Period.between(physical.birthDate(), LocalDate.now()).getYears();
        if(age < 16 || age > 90){
            UIUXFeedbackUtils.showErrorSnackbar("Seleccione un año de nacimiento válido.");
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
        txtFSurname.clear();
        txtSSurname.clear();
        txtName.clear();
        txtProfession.clear();
    }
}
