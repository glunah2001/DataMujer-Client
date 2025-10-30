package com.uned.clientedatamujer.controller.view;
import com.uned.clientedatamujer.dto.response.LegalPersonDTO;
import com.uned.clientedatamujer.dto.response.PhysicalPersonDTO;
import com.uned.clientedatamujer.dto.response.ProfileDTO;
import com.uned.clientedatamujer.service.AuthSession;
import com.uned.clientedatamujer.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UserCardController implements BaseCardController<ProfileDTO> {
    @FXML
    private Label labelID;
    @FXML
    private TextArea txtDescription;
    @FXML
    private ComboBox<String> comboBoxRole;
    private BaseSubSceneController parentController;
    private ProfileDTO data;
    private final UserService service = new UserService();

    @Override
    public void setData(ProfileDTO dto) {
        data = dto;
        if(data instanceof PhysicalPersonDTO pp){
            setProfileData(pp);
        }else if(data instanceof LegalPersonDTO lp){
            setProfileData(lp);
        }
    }

    @Override
    public void setParentController(BaseSubSceneController parent) {
        parentController = parent;
    }

    @FXML
    private void changeAffiliate(ActionEvent event) {
        parentController.mainController.executeCall(
                () -> service.updateAffiliate(AuthSession.getAccessToken(), getUsername()),
                (String response) -> {
                    parentController.mainController.hideLoading();
                    parentController.mainController.showSuccessSnackBar(response);
                    if(parentController instanceof AdminController adminController){
                        adminController.refreshCurrentPage();
                    }
                }
        );
    }

    @FXML
    private void changeRole(ActionEvent event) {
        var index = comboBoxRole.getSelectionModel().getSelectedIndex();
        parentController.mainController.executeCall(
                () -> service.updateRole(AuthSession.getAccessToken(), getUsername(), String.valueOf(index)),
                (String response) -> {
                    parentController.mainController.hideLoading();
                    parentController.mainController.showSuccessSnackBar(response);
                    if(parentController instanceof AdminController adminController){
                        adminController.refreshCurrentPage();
                    }
                }
        );
    }

    private void setProfileData(LegalPersonDTO lp) {
        labelID.setText(
                String.format("(%s) %S",
                        lp.legalId(),
                        lp.username()
                )
        );
        txtDescription.setText(
                String.format("""
                %s
                FUNDADA EL: %s
                
                INFORMACIÓN DE CONTACTO:
                EMAIL: %s
                TELÉFONO: %s
                
                %S, %s
                """,
                        lp.businessName(),
                        textFormatter(lp.foundationDate()),
                        lp.email(),
                        lp.phoneNumber(),
                        lp.country(),
                        lp.location()
                )
        );
        disableComboBox();
    }

    private void setProfileData(PhysicalPersonDTO pp) {
        labelID.setText(
                String.format("(%s) %S",
                        pp.nationalId(),
                        pp.username()
                )
        );
        txtDescription.setText(
                String.format("""
                %s %s %s (%S)
                FECHA DE NACIMIENTO: %s
                
                INFORMACIÓN DE CONTACTO:
                EMAIL: %s
                TELÉFONO: %s
                
                %S, %s
                """,
                        pp.name(),
                        pp.firstSurname(),
                        pp.secondSurname(),
                        pp.profession(),
                        textFormatter(pp.birthDate()),
                        pp.email(),
                        pp.phoneNumber(),
                        pp.country(),
                        pp.location()
                )
        );
        disableComboBox();
    }

    private String textFormatter(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return formatter.format(date);
    }

    private void showRole() {
        int index = switch(AuthSession.getRole()){
            case "ROLE_MENTOR" ->  1;
            case "ROLE_ADMIN" -> 2;
            default -> 0;
        };

        comboBoxRole.getSelectionModel().select(index);
    }

    private void disableComboBox(){
        if(AuthSession.getSubject().equals(getUsername())){
            comboBoxRole.setVisible(false);
            comboBoxRole.setManaged(false);
        }else{
            showRole();
        }
    }

    private String getUsername(){
        switch(data){
            case PhysicalPersonDTO pp -> {return pp.username();}
            case LegalPersonDTO lp -> {return lp.username();}
        }
    }
}
