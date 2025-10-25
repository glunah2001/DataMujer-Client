package com.uned.clientedatamujer.controller.view;

import com.uned.clientedatamujer.controller.util.SceneManager;
import com.uned.clientedatamujer.dto.response.PaymentDTO;
import com.uned.clientedatamujer.service.AuthSession;
import com.uned.clientedatamujer.service.PaymentService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PaymentCardController implements BaseCardController<PaymentDTO> {
    @FXML
    private Label labelID;
    @FXML
    private TextArea txtDescription;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnUnpay;
    @FXML
    private Button btnPay;
    private BaseSubSceneController parentController;
    private PaymentDTO data;
    private final PaymentService service = new PaymentService();


    @FXML
    private void deletePayment(ActionEvent event) {
        parentController.mainController.executeCall(
                () -> service.deletePayment(AuthSession.getAccessToken(), String.valueOf(data.id())),
                (_) -> {
                    parentController.mainController.showSuccessSnackBar("Pago eliminado exitosamente");
                    if(parentController instanceof PaymentController paymentController)
                        paymentController.refreshCurrentPage();
                }
        );
    }

    @FXML
    private void pay(ActionEvent event) {
        try {
            SceneManager.loadSubScene(parentController.mainController.getSubScenePane(),
                    "/com/uned/clientedatamujer/update-payment-subscene.fxml",
                    parentController.mainController,
                    parentController.rootPane,
                    parentController.snackBarInfo
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void unpay(ActionEvent event) {
        parentController.mainController.executeCall(
                () -> service.unpay(AuthSession.getAccessToken(), String.valueOf(data.id())),
                (PaymentDTO dto) -> {
                    parentController.mainController.showSuccessSnackBar("Pago #"+dto.id()+
                            " actualizado exitosamente a estado: PENDIENTE");
                    if(parentController instanceof PaymentController paymentController)
                        paymentController.refreshCurrentPage();
                }
        );
    }

    @Override
    public void setData(PaymentDTO dto) {
        data = dto;
        labelID.setText(
                String.format("ID PAGO #%d", dto.id())
        );
        txtDescription.setText(
                String.format("""
                INFORMACIÓN DE PAGO:
                USUARIO: %s
                CLASIFICACIÓN: %S
                MÉTODO: %S
                ESTADO: %S
                FECHA DE PAGO: %S
                
                %s
                """,
                        dto.username(),
                        dto.classification(),
                        dto.method(),
                        dto.isPaid() ? "PAGADO" : "PENDIENTE",
                        isDateRegistered(dto.paymentDate()),
                        dto.description()
                )
        );

        btnPay.setVisible(!data.isPaid());
        btnPay.setManaged(!data.isPaid());
        if(!AuthSession.getRole().equals("ROLE_ADMIN")){
            btnDelete.setVisible(false);
            btnDelete.setManaged(false);
            btnUnpay.setVisible(false);
            btnUnpay.setVisible(false);
        }else{
            btnUnpay.setVisible(data.isPaid());
            btnUnpay.setVisible(data.isPaid());
        }
    }

    @Override
    public void setParentController(BaseSubSceneController parent) {
        parentController = parent;
    }

    private String textFormatter(LocalDateTime date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy  HH:mm");
        return formatter.format(date);
    }

    private String isDateRegistered(LocalDateTime date){
        return date == null ? "No Registrada": textFormatter(date);
    }
}
