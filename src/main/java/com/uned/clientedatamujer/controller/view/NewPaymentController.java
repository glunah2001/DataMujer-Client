package com.uned.clientedatamujer.controller.view;

import com.jfoenix.controls.JFXToggleButton;
import com.uned.clientedatamujer.controller.util.ComponentInitializer;
import com.uned.clientedatamujer.dto.request.PaymentRegisterDTO;
import com.uned.clientedatamujer.dto.response.PaymentDTO;
import com.uned.clientedatamujer.enums.Classification;
import com.uned.clientedatamujer.enums.Method;
import com.uned.clientedatamujer.service.AuthSession;
import com.uned.clientedatamujer.service.PaymentService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class NewPaymentController extends BaseSubSceneController{
    @FXML
    private TextField txtTotalAmount;
    @FXML
    private JFXToggleButton toggleState;
    @FXML
    private DatePicker dtpPayDate;
    @FXML
    private Spinner<Integer> spinnerPayHour;
    @FXML
    private Spinner<Integer> spinnerPayMinutes;
    @FXML
    private TextArea txtDescription;
    @FXML
    private ComboBox<Classification> comboBoxClassification;
    @FXML
    private ComboBox<Method> comboBoxMethod;
    private final PaymentService service = new PaymentService();

    @FXML
    private void initialize(){
        ComponentInitializer.initializeClassification(comboBoxClassification);
        ComponentInitializer.initializeMethod(comboBoxMethod);
        ComponentInitializer.initializeSpinnerHours(spinnerPayHour);
        ComponentInitializer.initializeSpinnerMinutes(spinnerPayMinutes);
        ComponentInitializer.initializeToggle(toggleState, "Pendiente", "Pagado");
        ComponentInitializer.initializeTotalAmount(txtTotalAmount);
    }


    @FXML
    private void reportPayment(ActionEvent event) {
        if(!validateData()) return;

        var data = getData();

        mainController.executeCall(
                () -> service.postPayment(data, AuthSession.getAccessToken()),
                (PaymentDTO dto) -> {
                    mainController.hideLoading();
                    String message = String.format(
                            "Se ha reportado el pago tipo %s-%s con id %d.",
                            dto.classification(),
                            dto.method(),
                            dto.id()
                    );
                    mainController.showSuccessSnackBar(message);
                    clearForm();
                },
                "Error en el reporte del Pago."
        );
    }

    private boolean validateData(){
        if(txtDescription.getText().trim().isEmpty()){
            mainController.showErrorSnackBar("Por favor, complete toda la información del pago");
            return false;
        }
        if(dtpPayDate.getValue() == null){
            mainController.showErrorSnackBar("Por favor, complete toda la información del pago");
            return false;
        }
        var amount = getTotalAmountValue();
        if(amount == null) return false;
        if(amount.compareTo(BigDecimal.ZERO) <= 0) {
            mainController.showErrorSnackBar("El monto total debe ser mayor que 0");
            return false;
        }

        return true;
    }

    private PaymentRegisterDTO getData(){
        LocalTime start = LocalTime.of(spinnerPayHour.getValue(), spinnerPayMinutes.getValue());
        LocalDateTime date = LocalDateTime.of(dtpPayDate.getValue(), start);

        return new PaymentRegisterDTO(
                txtDescription.getText(),
                comboBoxClassification.getValue(),
                comboBoxMethod.getValue(),
                date,
                toggleState.isSelected(),
                getTotalAmountValue()
        );
    }

    public BigDecimal getTotalAmountValue() {
        try {
            return new BigDecimal(txtTotalAmount.getText());
        } catch (NumberFormatException e) {
            mainController.showErrorSnackBar("Ocurrió un error al convertir el valor monetario.");
            return null;
        }
    }

    private void clearForm(){
        txtTotalAmount.clear();
        txtDescription.clear();
    }
}
