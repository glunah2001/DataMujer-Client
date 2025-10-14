package com.uned.clientedatamujer.controller;

import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXToggleButton;
import com.uned.clientedatamujer.dto.ApiError;
import com.uned.clientedatamujer.dto.request.CommonRegisterDTO;
import com.uned.clientedatamujer.dto.request.PhysicalPersonRegisterDTO;
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
import java.time.Period;
import java.util.Arrays;

public class PhysicalPersonController extends BaseController{

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
        initializeCountry();
        initializeToggle();
        initializePhone();
        initializeCedula();
        snackBarInfo = new JFXSnackbar(rootPane);
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

        showLoading(rootPane);

        runAsync(()->{
            try{
                Object result = service.register(physical);
                if(result instanceof String success){
                    runLater(() -> {
                        showSuccessSnackBar(success,snackBarInfo);
                        withDelay(4,() ->{
                            hideLoading(rootPane);
                            try{
                                clearForm();
                                SceneManager.toLogIn();
                            }catch (IOException e){
                                String message = "Corrupción en la ruta de recursos";
                                showErrorSnackBar(message, snackBarInfo);
                            }
                        });
                    });
                }else if(result instanceof ApiError error){
                    try {
                        runLater(()->{
                            hideLoading(rootPane);
                            handleApiError(rootPane, snackBarInfo, error, "Error en los datos de registro");
                        });
                    }catch(Exception e){
                        hideLoading(rootPane);
                        e.printStackTrace();
                    }
                }
            }catch(Exception e){
                runLater(()-> hideLoading(rootPane));
                e.printStackTrace();
            }
        });
    }

    @FXML
    public void clearCedula(ActionEvent event) {txtCedula.clear();}

    private void initializeCountry(){
        comboCountry.getItems().addAll(Arrays.asList(Country.values()));
        comboCountry.setConverter(new StringConverter<>() {
            @Override
            public String toString(Country country) {
                if (country == null) return "";
                String formatted = country.name().toUpperCase().replace("_", " ");
                return Character.toUpperCase(formatted.charAt(0)) + formatted.substring(1);
            }

            @Override
            public Country fromString(String string) {
                if (string == null || string.isEmpty()) return null;
                return Arrays.stream(Country.values())
                        .filter(c -> c.name().replace("_", " ").equalsIgnoreCase(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        comboCountry.setValue(Country.COSTA_RICA);
    }

    private void initializeToggle(){
        toggleDocumentType.setText("Cédula Nacional");
        toggleDocumentType.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                toggleDocumentType.setText("DIMEX");
            } else {
                toggleDocumentType.setText("Cédula Nacional");
            }
        });
    }

    private void initializePhone(){
        txtPhone.textProperty().addListener((obs,
                                             oldText,
                                             newText) -> {
            if (!newText.matches("[+\\d\\s]*")) {
                txtPhone.setText(newText.replaceAll("[^+\\d\\s]", ""));
                return;
            }

            if (newText.chars().filter(ch -> ch == '+').count() > 1) {
                txtPhone.setText(oldText);
                return;
            }

            if (newText.length() > 1 && newText.charAt(0) != '+') {
                txtPhone.setText("+" + newText.replaceAll("\\+", ""));
                return;
            }

            if (newText.length() > 17) {
                txtPhone.setText(oldText);
                return;
            }

            if (!newText.isEmpty() && !newText.matches("^\\+[1-9]\\d{0,2}\\s\\d{0,14}$")) {
                if (!newText.matches("^\\+[1-9]?\\d{0,2}\\s?\\d{0,14}$")) {
                    txtPhone.setText(oldText);
                }
            }
        });
    }

    private void initializeCedula(){
        txtCedula.textProperty().addListener((obs,
                                              oldText,
                                              newText) -> {
            if (!newText.matches("\\d*")) {
                txtCedula.setText(newText.replaceAll("[^\\d]", ""));
                return;
            }

            int maxLength = toggleDocumentType.isSelected() ? 12 : 9;

            if (newText.length() > maxLength) {
                txtCedula.setText(oldText);
            }
        });
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
        if(!isCountrySelected()) {
            showErrorSnackBar("Seleccione por favor un país de residencia.", snackBarInfo);
            return false;
        }
        if(physical.birthDate() == null){
            showErrorSnackBar("Por favor, indique su fecha de nacimiento.", snackBarInfo);
            return false;
        }

        if(physical.name().isEmpty() || physical.firstSurname().isEmpty() ||
        physical.secondSurname().isEmpty() || physical.commonRegisterDTO().phoneNumber().isEmpty() ||
        physical.commonRegisterDTO().location().isEmpty() || physical.profession().isEmpty() ||
        physical.commonRegisterDTO().email().isEmpty() || physical.commonRegisterDTO().username().isEmpty() ||
        physical.commonRegisterDTO().password().isEmpty() || physical.nationalId().isEmpty()){
            showErrorSnackBar("Por favor, rellene todos los datos.", snackBarInfo);
            return false;
        }


        int age = Period.between(physical.birthDate(), LocalDate.now()).getYears();
        if(age < 16 || age > 90){
            showErrorSnackBar("Seleccione un año de nacimiento válido.", snackBarInfo);
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
