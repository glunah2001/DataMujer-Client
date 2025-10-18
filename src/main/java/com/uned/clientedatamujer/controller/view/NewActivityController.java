package com.uned.clientedatamujer.controller.view;

import com.jfoenix.controls.JFXToggleButton;
import com.uned.clientedatamujer.dto.ApiError;
import com.uned.clientedatamujer.dto.request.ActivityRegisterDTO;
import com.uned.clientedatamujer.dto.response.ActivityDTO;
import com.uned.clientedatamujer.service.ActivityService;
import com.uned.clientedatamujer.service.AuthSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class NewActivityController extends BaseSubSceneController{
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
    public void initialize(){
        initializeSpinner(spinnerStartHour, 23);
        initializeSpinner(spinnerEndHour, 23);
        initializeSpinner(spinnerStartMinutes, 59);
        initializeSpinner(spinnerEndMinutes, 59);
        initializeToggle();
        setRootPane(rootPane);
        setSnackBarInfo(snackBarInfo);
    }

    @FXML
    private void btnPublish(ActionEvent event) {
        if(!validateData()) return;

        var data = getData();

        mainController.showLoading();
        mainController.runAsync(() ->{
            try{
                Object response = service.postActivity(data, AuthSession.getAccessToken());
                if(response instanceof ActivityDTO dto){
                    mainController.runLater(()->{
                        mainController.hideLoading();
                        String message = String.format(
                                "Se ha creado la actividad %S con id %d.",
                                dto.activity(),
                                dto.id()
                        );
                        mainController.showSuccessSnackBar(message);
                        clearForm();
                    });
                }else if(response instanceof ApiError error){
                    mainController.runLater(() ->{
                        mainController.hideLoading();
                        mainController.handleApiError(error, "Error en la creación " +
                                "de la actividad.");
                    });
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        });
    }

    private void initializeToggle(){
        toggleIsOnSite.setText("Virtual");
        toggleIsOnSite.selectedProperty()
                .addListener((observable,
                              oldValue,
                              newValue) -> {
            if (newValue) {
                toggleIsOnSite.setText("Presencial");
            } else {
                toggleIsOnSite.setText("Virtual");
            }
        });
    }

    private void initializeSpinner(Spinner<Integer> spinner, int max){
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, max, 0);
        valueFactory.setWrapAround(true);
        spinner.setValueFactory(valueFactory);
    }

    public boolean validateData(){
        if(txtActivity.getText().isEmpty() || txtUsername.getText().isEmpty() ||
                txtDescription.getText().isEmpty() || txtLocation.getText().isEmpty()){
            mainController.showErrorSnackBar("Por favor, complete toda la información de la actividad");
            return false;
        }

        if(dtpStartDate.getValue() == null || dtpEndDate.getValue() == null){
            mainController.showErrorSnackBar("Por favor, coloque fechas válidas");
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
}
