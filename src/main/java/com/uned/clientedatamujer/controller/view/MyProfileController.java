package com.uned.clientedatamujer.controller.view;

import com.jfoenix.controls.JFXSnackbar;
import com.uned.clientedatamujer.controller.util.SceneManager;
import com.uned.clientedatamujer.dto.ApiError;
import com.uned.clientedatamujer.dto.request.CommonUpdateDTO;
import com.uned.clientedatamujer.dto.request.LegalPersonUpdateDTO;
import com.uned.clientedatamujer.dto.request.PhysicalPersonUpdateDTO;
import com.uned.clientedatamujer.dto.response.LegalPersonDTO;
import com.uned.clientedatamujer.dto.response.PhysicalPersonDTO;
import com.uned.clientedatamujer.dto.response.ProfileDTO;
import com.uned.clientedatamujer.enums.Country;
import com.uned.clientedatamujer.service.AuthService;
import com.uned.clientedatamujer.service.AuthSession;
import com.uned.clientedatamujer.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;

public class MyProfileController extends BaseController{

    @FXML
    private Button btnUpdateProfile;
    @FXML
    private JFXSnackbar snackBarInfo;
    @FXML
    private StackPane rootPane;
    @FXML
    private Label labelUsername;
    @FXML
    private Label labelId;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtFSurname;
    @FXML
    private TextField txtSSurname;
    @FXML
    private TextField txtProfession;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtPhone;
    @FXML
    private TextField txtLocation;
    @FXML
    private DatePicker dtpDate;
    @FXML
    private ComboBox<Country> comboBoxCountry;

    private final UserService userService = new UserService();
    private final AuthService authService = new AuthService();

    private ProfileDTO myData;

    @FXML
    private void initialize(){
        showLoading();
        snackBarInfo = new JFXSnackbar(rootPane);
        setRootPane(rootPane);
        setSnackBarInfo(snackBarInfo);
        initializeCountry();
        initializePhone();
        prepareSceneElements();
        getData();
        loadData();
        hideLoading();
    }

    @FXML
    private void updateProfile(ActionEvent event) {
        if (!validateData()) return;
        if (AuthSession.getPersonType().equals("FISICA")) {
            var dto = getPhysicalData();
            sendUpdateRequest(dto);
        } else {
            var dto = getLegalData();
            sendUpdateRequest(dto);
        }
    }

    @FXML
    private void toMainView(ActionEvent event) {
        int width = (int) rootPane.getWidth();
        int height = (int) rootPane.getHeight();
        try {
            SceneManager.toMainView(width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void resetData(ActionEvent event) {
        loadData();
    }

    @FXML
    private void logout(ActionEvent event) {
        authService.logout(AuthSession.getAccessToken());
        try {
            SceneManager.toLogIn();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void prepareSceneElements(){
        boolean isPhysical = AuthSession.getPersonType().equals("FISICA");

        labelUsername.setText("[USERNAME]");
        labelId.setText("[ID]");

        txtFSurname.setVisible(isPhysical);
        txtFSurname.setManaged(isPhysical);
        txtSSurname.setVisible(isPhysical);
        txtSSurname.setManaged(isPhysical);
        txtProfession.setVisible(isPhysical);
        txtProfession.setManaged(isPhysical);

        if(isPhysical){
            txtName.setPromptText("NOMBRE");
            dtpDate.setPromptText("FECHA DE NACIMIENTO");
        }else{
            txtName.setPromptText("NOMBRE DE ENTIDAD");
            dtpDate.setPromptText("FECHA DE FUNDACIÓN");
        }
    }

    private void getData(){
        Object result = userService.getMyProfile();
        if(result instanceof ApiError error){
            showErrorSnackBar(error.message());
        }else if(result instanceof ProfileDTO person){
            myData = person;
        }
    }

    private void loadData(){
        switch (myData) {
            case null -> {
                btnUpdateProfile.setDisable(true);
                return;
            }
            case PhysicalPersonDTO physical -> {
                labelUsername.setText(physical.username());
                labelId.setText(physical.nationalId());
                txtName.setText(physical.name());
                txtFSurname.setText(physical.firstSurname());
                txtSSurname.setText(physical.secondSurname());
                txtProfession.setText(physical.profession());
                dtpDate.setValue(physical.birthDate());

                txtEmail.setText(physical.email());
                txtPhone.setText(physical.phoneNumber());
                txtLocation.setText(physical.location());
                comboBoxCountry.setValue(physical.country());
            }
            case LegalPersonDTO legal -> {
                labelUsername.setText(legal.username());
                labelId.setText(legal.legalId());
                txtName.setText(legal.businessName());
                dtpDate.setValue(legal.foundationDate());

                txtEmail.setText(legal.email());
                txtPhone.setText(legal.phoneNumber());
                txtLocation.setText(legal.location());
                comboBoxCountry.setValue(legal.country());
            }
        }
        btnUpdateProfile.setDisable(false);
    }

    private void sendUpdateRequest(Object dto){
        showLoading();
        runAsync(() -> {
            try {
                Object response = userService.updateProfile(dto);
                if (response instanceof PhysicalPersonDTO newData) {
                    myData = newData;
                    loadData();
                    runLater(() -> {
                        hideLoading();
                        showSuccessSnackBar("Actualización realizada satisfactoriamente");
                    });
                } else if (response instanceof LegalPersonDTO newData) {
                    myData = newData;
                    loadData();
                    runLater(() -> {
                        hideLoading();
                        showSuccessSnackBar("Actualización realizada satisfactoriamente");
                    });
                } else if (response instanceof ApiError error) {
                    runLater(() -> {
                        hideLoading();
                        loadData();
                        handleApiError(error, "Error al Actualizar sus datos.");
                    });
                }
            } catch (Exception e) {
                runLater(this::hideLoading);
                e.printStackTrace();
            }
        });
    }

    private PhysicalPersonUpdateDTO getPhysicalData(){
        String name = txtName.getText().trim();
        String fSurname = txtFSurname.getText().trim();
        String sSurname = txtSSurname.getText().trim();
        String profession = txtProfession.getText().trim();
        LocalDate birthDate = dtpDate.getValue();
        var common = getCommonData();

        return new PhysicalPersonUpdateDTO(common, fSurname, sSurname, name, profession, birthDate);
    }

    private LegalPersonUpdateDTO getLegalData(){
        String businessName = txtName.getText().trim();
        LocalDate foundationDate = dtpDate.getValue();
        var common = getCommonData();

        return new LegalPersonUpdateDTO(common, businessName, foundationDate);
    }

    private CommonUpdateDTO getCommonData(){
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();
        String location = txtLocation.getText().trim();
        Country country = comboBoxCountry.getValue();
        return new CommonUpdateDTO(email, phone, country, location);
    }

    private boolean validateData(){
        if(txtName.getText().isEmpty() || txtEmail.getText().isEmpty() || txtPhone.getText().isEmpty() ||
                txtLocation.getText().isEmpty()) {
            showErrorSnackBar("Por favor, rellene todos los datos.");
            return false;
        }


        if(AuthSession.getPersonType().equals("FISICA") && (
                txtFSurname.getText().isEmpty() ||
                txtSSurname.getText().isEmpty() || txtProfession.getText().isEmpty())) {
            showErrorSnackBar("Por favor, rellene todos los datos.");
            return false;
        }

        if(AuthSession.getPersonType().equals("FISICA")){
            int age = Period.between(dtpDate.getValue(), LocalDate.now()).getYears();
            if(age < 16 || age > 90){
                showErrorSnackBar("Seleccione un año de nacimiento válido.");
                return false;
            }
        }else if(dtpDate.getValue().isAfter(LocalDate.now())){
            showErrorSnackBar("Seleccione un año de fundación válido.");
            return false;
        }

        if(!isCountrySelected()){
            showErrorSnackBar("Seleccione un páis.");
            return false;
        }
        return true;
    }

    private boolean isCountrySelected() {
        Country selected = comboBoxCountry.getValue();
        return selected != null;
    }

    private void initializeCountry(){
        comboBoxCountry.getItems().addAll(Arrays.asList(Country.values()));
        comboBoxCountry.setConverter(new StringConverter<>() {
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
        comboBoxCountry.setValue(Country.COSTA_RICA);
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
}
