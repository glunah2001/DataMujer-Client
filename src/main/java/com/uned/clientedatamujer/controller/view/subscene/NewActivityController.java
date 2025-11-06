package com.uned.clientedatamujer.controller.view.subscene;

import com.jfoenix.controls.JFXToggleButton;
import com.uned.clientedatamujer.controller.util.ComponentInitializer;
import com.uned.clientedatamujer.controller.util.UIUXFeedbackUtils;
import com.uned.clientedatamujer.controller.view.base.BaseSubSceneController;
import com.uned.clientedatamujer.dto.request.ActivityRegisterDTO;
import com.uned.clientedatamujer.dto.response.ActivityDTO;
import com.uned.clientedatamujer.service.ActivityService;
import com.uned.clientedatamujer.service.AuthSession;
import com.uned.clientedatamujer.service.DataUtilities;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class NewActivityController extends BaseSubSceneController {
    @FXML
    private TextField txtActivity;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextArea txtDescription;
    @FXML
    private JFXToggleButton toggleIsOnSite;
    @FXML
    private TextField txtLocation;
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
    private final ActivityService service = new ActivityService();

    @FXML
    private void initialize(){
        ComponentInitializer.initializeSpinnerHours(spinnerStartHour);
        ComponentInitializer.initializeSpinnerHours(spinnerEndHour);
        ComponentInitializer.initializeSpinnerMinutes(spinnerStartMinutes);
        ComponentInitializer.initializeSpinnerMinutes(spinnerEndMinutes);
        ComponentInitializer.initializeToggle(
                toggleIsOnSite,
                "Virtual",
                "Presencial"
                );
        DataUtilities.clearAll();
    }

    @FXML
    private void btnPublish(ActionEvent event) {
        if(!validateData()) return;

        var data = getData();

        mainController.executeCall(
                () -> service.postActivity(data),
                (ActivityDTO dto) -> {
                    UIUXFeedbackUtils.hideLoading();
                    String message = String.format(
                            "Se ha creado la actividad %S con id %d.",
                            dto.activity(),
                            dto.id()
                    );
                    UIUXFeedbackUtils.showSuccessSnackbar(message);
                    clearForm();
                },
                "Error en la creación de la actividad."
        );
    }

    public boolean validateData(){
        if(txtActivity.getText().isEmpty() || txtUsername.getText().isEmpty() ||
                txtDescription.getText().isEmpty() || txtLocation.getText().isEmpty()){
            UIUXFeedbackUtils.showErrorSnackbar("Por favor, complete toda la información de la actividad");
            return false;
        }

        if(dtpStartDate.getValue() == null || dtpEndDate.getValue() == null){
            UIUXFeedbackUtils.showErrorSnackbar("Por favor, coloque fechas válidas");
            return false;
        }

        return true;
    }

    private ActivityRegisterDTO getData(){
        LocalTime start = LocalTime.of(spinnerStartHour.getValue(), spinnerStartMinutes.getValue());
        LocalTime end = LocalTime.of(spinnerEndHour.getValue(), spinnerEndMinutes.getValue());
        return new ActivityRegisterDTO(txtActivity.getText(),
                txtDescription.getText(),
                txtLocation.getText(),
                toggleIsOnSite.isSelected(),
                LocalDateTime.of(dtpStartDate.getValue(), start),
                LocalDateTime.of(dtpEndDate.getValue(), end),
                txtUsername.getText()
        );
    }

    private void clearForm(){
        txtActivity.clear();
        txtUsername.clear();
        txtDescription.clear();
        txtLocation.clear();
    }

    @Override
    protected void refreshCurrentPage() {}
}
