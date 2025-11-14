package com.uned.clientedatamujer.controller.view.scene;

import com.jfoenix.controls.JFXSnackbar;
import com.uned.clientedatamujer.controller.util.ComponentInitializer;
import com.uned.clientedatamujer.controller.util.SceneManager;
import com.uned.clientedatamujer.controller.util.UIUXFeedbackUtils;
import com.uned.clientedatamujer.controller.view.base.BaseController;
import com.uned.clientedatamujer.dto.request.CommonUpdateDTO;
import com.uned.clientedatamujer.dto.request.LegalPersonUpdateDTO;
import com.uned.clientedatamujer.dto.request.PhysicalPersonUpdateDTO;
import com.uned.clientedatamujer.dto.response.LegalPersonDTO;
import com.uned.clientedatamujer.dto.response.PhysicalPersonDTO;
import com.uned.clientedatamujer.dto.response.ProfileDTO;
import com.uned.clientedatamujer.enums.Country;
import com.uned.clientedatamujer.service.AuthService;
import com.uned.clientedatamujer.service.util.AuthSession;
import com.uned.clientedatamujer.service.UserService;
import com.uned.clientedatamujer.service.util.TermsReader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;

public class MyProfileController extends BaseController {

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
    private Label labelRules;
    @FXML
    private Label labelLaw;
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
        snackBarInfo = new JFXSnackbar(rootPane);
        UIUXFeedbackUtils.setRootPane(rootPane);
        UIUXFeedbackUtils.setSnackbar(snackBarInfo);
        ComponentInitializer.initializeCountry(comboBoxCountry);
        ComponentInitializer.initializePhone(txtPhone);
        prepareSceneElements();
        getData();
        setDocumentData();
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
        authService.logout();
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
        executeCall(
                userService::getMyProfile,
                (ProfileDTO newData) -> {
                    myData = newData;
                    loadData();
                }
        );
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
        UIUXFeedbackUtils.hideLoading();
    }

    private void setDocumentData(){
        String dataMujerRules = TermsReader.loadText("/com/uned/clientedatamujer/terms/DataMujerRules.txt");
        String law = TermsReader.loadText("/com/uned/clientedatamujer/terms/LeyN8968.txt");
        labelRules.setText(dataMujerRules);
        labelLaw.setText(law);
    }

    private void sendUpdateRequest(Object dto){
        executeCall(
                () -> userService.updateProfile(dto),
                (Object response) -> {
                    if(response instanceof PhysicalPersonDTO newData){
                        myData = newData;
                        loadData();
                    }else if(response instanceof LegalPersonDTO newData){
                        myData = newData;
                        loadData();
                    }
                    UIUXFeedbackUtils.hideLoading();
                    UIUXFeedbackUtils.showSuccessSnackbar("Actualización realizada satisfactoriamente");
                },
                "Error al Actualizar sus datos."
        );
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
            UIUXFeedbackUtils.showErrorSnackbar("Por favor, rellene todos los datos.");
            return false;
        }

        if(AuthSession.getPersonType().equals("FISICA") && (
                txtFSurname.getText().isEmpty() ||
                txtSSurname.getText().isEmpty() || txtProfession.getText().isEmpty())) {
            UIUXFeedbackUtils.showErrorSnackbar("Por favor, rellene todos los datos.");
            return false;
        }

        if(AuthSession.getPersonType().equals("FISICA")){
            int age = Period.between(dtpDate.getValue(), LocalDate.now()).getYears();
            if(age < 16 || age > 90){
                UIUXFeedbackUtils.showErrorSnackbar("Seleccione un año de nacimiento válido.");
                return false;
            }
        }else if(dtpDate.getValue().isAfter(LocalDate.now())){
            UIUXFeedbackUtils.showErrorSnackbar("Seleccione un año de fundación válido.");
            return false;
        }

        if(ComponentInitializer.isComboBoxValueNull(comboBoxCountry)){
            UIUXFeedbackUtils.showErrorSnackbar("Seleccione un páis.");
            return false;
        }
        return true;
    }
}
