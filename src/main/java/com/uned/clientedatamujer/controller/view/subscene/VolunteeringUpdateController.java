package com.uned.clientedatamujer.controller.view.subscene;

import com.uned.clientedatamujer.controller.util.ComponentInitializer;
import com.uned.clientedatamujer.controller.util.UIUXFeedbackUtils;
import com.uned.clientedatamujer.controller.view.base.BaseSubSceneController;
import com.uned.clientedatamujer.dto.ApiError;
import com.uned.clientedatamujer.dto.request.VolunteeringUpdateDTO;
import com.uned.clientedatamujer.dto.response.VolunteeringDTO;
import com.uned.clientedatamujer.service.AuthSession;
import com.uned.clientedatamujer.service.DataUtilities;
import com.uned.clientedatamujer.service.VolunteeringService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class VolunteeringUpdateController extends BaseSubSceneController {

    @FXML
    private TextField txtID;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtRole;
    @FXML
    private DatePicker dtpStartDate;
    @FXML
    private Spinner<Integer> spinnerStartHour;
    @FXML
    private Spinner<Integer> spinnerStartMinutes;
    @FXML
    private DatePicker dtpEndDate;
    @FXML
    private Spinner<Integer> spinnerEndHour;
    @FXML
    private Spinner<Integer> spinnerEndMinutes;
    private final VolunteeringService service = new VolunteeringService();

    @FXML
    private void initialize(){
        ComponentInitializer.initializeSpinnerHours(spinnerStartHour);
        ComponentInitializer.initializeSpinnerHours(spinnerStartMinutes);
        ComponentInitializer.initializeSpinnerMinutes(spinnerEndHour);
        ComponentInitializer.initializeSpinnerMinutes(spinnerEndMinutes);

        setData();
    }

    @FXML
    private void reset(ActionEvent event){
        setData();
    }

    @FXML
    private void update(ActionEvent event) {
        if(!validateData()){
            UIUXFeedbackUtils.showErrorSnackbar("Por favor, rellene toda la información.");
            return;
        }
        var dto = getData();
        mainController.executeCall(
                () -> service.updateVolunteering(AuthSession.getAccessToken(), dto, txtID.getText()),
                (VolunteeringDTO response) ->{
                    UIUXFeedbackUtils.showSuccessSnackbar("Voluntariado #"+response.id()+" se ha actualizado, " +
                            "correctamente.");
                    mainController.withDelay(4, () ->{
                        UIUXFeedbackUtils.hideLoading();
                        DataUtilities.clearVolunteeringDTO();
                        mainController.forceLoadVolunteering();
                    });
                },
                (ApiError error) -> {
                    if(error.status() == 404){
                        mainController.withDelay(4, () -> {
                            UIUXFeedbackUtils.hideLoading();
                            DataUtilities.clearVolunteeringDTO();
                            mainController.forceLoadVolunteering();
                        });
                    }else
                        UIUXFeedbackUtils.hideLoading();
                },
                "Error en los datos del voluntariado"
        );
    }

    private boolean validateData() {
        if(dtpStartDate.getValue() == null || dtpEndDate.getValue() == null) return false;
        if(txtRole.getText().trim().isBlank()) return false;
        return !txtUsername.getText().trim().isBlank();
    }

    private void setData(){
        txtID.setText(String.valueOf(DataUtilities.getLastVolunteeringDTO().id()));
        txtUsername.setText(DataUtilities.getLastVolunteeringDTO().username());
        txtRole.setText(DataUtilities.getLastVolunteeringDTO().activityRole());

        var startShift = DataUtilities.getLastVolunteeringDTO().startShift();
        var endShift = DataUtilities.getLastVolunteeringDTO().endShift();

        dtpStartDate.setValue(LocalDate.from(startShift));
        dtpEndDate.setValue(LocalDate.from(endShift));
        spinnerStartHour.getValueFactory().setValue(
                startShift.getHour()
        );
        spinnerStartHour.getValueFactory().setValue(
                startShift.getHour()
        );
        spinnerEndHour.getValueFactory().setValue(
                endShift.getHour()
        );
        spinnerEndMinutes.getValueFactory().setValue(
                endShift.getMinute()
        );
    }

    private VolunteeringUpdateDTO getData(){
        var startTime = LocalTime.of(spinnerStartHour.getValue(), spinnerStartMinutes.getValue());
        var endTime = LocalTime.of(spinnerEndHour.getValue(), spinnerEndMinutes.getValue());

        var startShift = LocalDateTime.of(dtpStartDate.getValue(), startTime);
        var endShift = LocalDateTime.of(dtpEndDate.getValue(), endTime);

        return new VolunteeringUpdateDTO(startShift, endShift, txtRole.getText().trim());
    }

}
