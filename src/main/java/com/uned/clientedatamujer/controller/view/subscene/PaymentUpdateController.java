package com.uned.clientedatamujer.controller.view.subscene;

import com.uned.clientedatamujer.controller.util.ComponentInitializer;
import com.uned.clientedatamujer.controller.util.UIUXFeedbackUtils;
import com.uned.clientedatamujer.controller.view.base.BaseSubSceneController;
import com.uned.clientedatamujer.dto.ApiError;
import com.uned.clientedatamujer.dto.response.PaymentDTO;
import com.uned.clientedatamujer.service.AuthSession;
import com.uned.clientedatamujer.service.DataUtilities;
import com.uned.clientedatamujer.service.PaymentService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class PaymentUpdateController extends BaseSubSceneController {

    @FXML
    private DatePicker dtpPayDate;
    @FXML
    private Spinner<Integer> spinnerPayMinutes;
    @FXML
    private Spinner<Integer> spinnerPayHour;
    @FXML
    private TextField txtId;
    @FXML
    private TextArea txtDescription;
    private final PaymentService service = new PaymentService();

    @FXML
    private void initialize(){
        ComponentInitializer.initializeSpinnerHours(spinnerPayHour);
        ComponentInitializer.initializeSpinnerMinutes(spinnerPayMinutes);
        UIUXFeedbackUtils.showLoading();
        setData();
        UIUXFeedbackUtils.hideLoading();
    }

    @FXML
    private void update(ActionEvent event) {
        if(!validateData()) return;
        var time = LocalTime.of(spinnerPayHour.getValue(), spinnerPayMinutes.getValue());
        var date = dtpPayDate.getValue();
        var dateTime = LocalDateTime.of(date, time);
        mainController.executeCall(
                () -> service.pay(AuthSession.getAccessToken(), txtId.getText(), dateTime),
                (PaymentDTO dto) -> {
                    UIUXFeedbackUtils.showSuccessSnackbar("Éxito en el reporte de actualización de pago #"+dto.id()
                    +" a estado: PAGADO");
                    mainController.withDelay(4, () -> {
                        UIUXFeedbackUtils.hideLoading();
                        DataUtilities.clearPaymentDTO();
                        mainController.forceLoadPayments();
                    });
                },
                (ApiError error) -> {
                    if(error.status() == 404){
                        mainController.withDelay(4, () -> {
                            UIUXFeedbackUtils.hideLoading();
                            DataUtilities.clearVolunteeringDTO();
                            mainController.forceLoadPayments();
                        });
                    }else
                        UIUXFeedbackUtils.hideLoading();
                },
                "Error en los datos del pago"
        );
    }

    private void setData(){
        txtId.setText(String.valueOf(DataUtilities.getLastPaymentDTO().id()));
        txtDescription.setText(String.format("""
                EMITIDO POR: %s
                CLASIFICACIÓN: %S
                MÉTODO DE PAGO: %S
                
                %S
                """,
                DataUtilities.getLastPaymentDTO().username(),
                DataUtilities.getLastPaymentDTO().classification(),
                DataUtilities.getLastPaymentDTO().method(),
                DataUtilities.getLastPaymentDTO().description()));
    }

    private boolean validateData(){
        if(dtpPayDate.getValue() == null){
            UIUXFeedbackUtils.showErrorSnackbar("Por favor, seleccione una fecha válida.");
            return false;
        }
        if(dtpPayDate.getValue().isAfter(LocalDate.now())){
            UIUXFeedbackUtils.showErrorSnackbar("La fecha de pago no debe ser una fecha futura.");
            return false;
        }
        return true;
    }

}
