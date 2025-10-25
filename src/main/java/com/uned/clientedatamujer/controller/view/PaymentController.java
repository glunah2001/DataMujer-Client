package com.uned.clientedatamujer.controller.view;

import com.jfoenix.controls.JFXToggleButton;
import com.uned.clientedatamujer.controller.util.ComponentInitializer;
import com.uned.clientedatamujer.dto.SimplePage;
import com.uned.clientedatamujer.dto.response.PaymentDTO;
import com.uned.clientedatamujer.service.AuthSession;
import com.uned.clientedatamujer.service.PaymentService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.List;

public class PaymentController extends BaseSubSceneController{
    @FXML
    private JFXToggleButton toggleState;
    @FXML
    private ComboBox<String> comboBoxSearchType;
    @FXML
    private Button btnSearch;
    @FXML
    private VBox VBoxPayment;
    @FXML
    private Button btnPrev;
    @FXML
    private Button btnNext;
    @FXML
    private TextField txtId;
    private int currentPage;
    private final PaymentService service = new PaymentService();

    @FXML
    private void initialize(){
        ComponentInitializer.initializeToggle(toggleState, "Pendientes", "Completados");
        ComponentInitializer.configureLongOnlyTextField(txtId);
        onlyAdminPaymentSearchOptions();

        Platform.runLater(() -> {
            currentPage = 0;
            getPageDataMyPayments(currentPage);
        });
    }

    @FXML
    private void showPrevious(ActionEvent event) {
        currentPage--;
        searchPayment(null);
    }

    @FXML
    private void showNext(ActionEvent event) {
        currentPage++;
        searchPayment(null);
    }

    @FXML
    private void searchPayment(ActionEvent event) {
        int index = comboBoxSearchType.getSelectionModel().getSelectedIndex();
        if(index == 0){
            String id = txtId.getText().trim();
            if(id.isEmpty()){
                getPageDataMyPayments(currentPage);
            }else{
                getSingleData(id);
            }
        }else if(index == 1){
            getPageDataStatus(currentPage);
        }
    }

    @FXML
    private void changeSearchMethod(ActionEvent event) {
        int index = comboBoxSearchType.getSelectionModel().getSelectedIndex();
        currentPage = 0;
        txtId.setVisible(index == 0);
        txtId.setManaged(index == 0);
        btnSearch.setVisible(index == 0);
        btnSearch.setManaged(index == 0);
        toggleState.setVisible(index == 1);
        toggleState.setManaged(index == 1);
        if(index == 0){
            getPageDataMyPayments(currentPage);
        }else{
            getPageDataStatus(currentPage);
        }
    }

    private void getPageDataMyPayments(int currentPage){
        VBoxPayment.getChildren().clear();
        mainController.executeCall(
                () -> service.getMyPayment(AuthSession.getAccessToken(), currentPage),
                (SimplePage<PaymentDTO> simplePage) -> {
                    VBoxPayment.getChildren().clear();
                    allowPageableButtons(simplePage.currentPage(), simplePage.totalPages());
                    List<PaymentDTO> dto = simplePage.content();
                    dto.forEach(payment -> {
                        setCard(
                                "/com/uned/clientedatamujer/payment-container.fxml",
                                VBoxPayment,
                                payment,
                                this
                        );
                    });
                    mainController.hideLoading();
                }
        );
    }

    private void getPageDataStatus(int currentPage){
        VBoxPayment.getChildren().clear();
        mainController.executeCall(
                () -> service.getPaymentByStatus(AuthSession.getAccessToken(),
                        toggleState.isSelected(),
                        currentPage
                ),
                (SimplePage<PaymentDTO> simplePage) -> {
                    VBoxPayment.getChildren().clear();
                    allowPageableButtons(simplePage.currentPage(), simplePage.totalPages());
                    List<PaymentDTO> dto = simplePage.content();
                    dto.forEach(payment -> {
                        setCard(
                                "/com/uned/clientedatamujer/payment-container.fxml",
                                VBoxPayment,
                                payment,
                                this
                        );
                    });
                    mainController.hideLoading();
                }
        );
    }

    private void getSingleData(String id){
        VBoxPayment.getChildren().clear();
        mainController.executeCall(
                () -> service.getPaymentById(AuthSession.getAccessToken(), id),
                (PaymentDTO dto) -> {
                    allowPageableButtons(0, 0);
                    setCard(
                            "/com/uned/clientedatamujer/payment-container.fxml",
                            VBoxPayment,
                            dto,
                            this
                    );
                    mainController.hideLoading();
                }
        );
    }

    private void allowPageableButtons(int currentPage, int totalPages){
        this.currentPage = currentPage;
        if(totalPages == 0){
            btnNext.setVisible(false);
            btnNext.setManaged(false);
            btnPrev.setVisible(false);
            btnPrev.setManaged(false);
            return;
        }

        boolean allowNext = currentPage < totalPages-1;
        boolean allowPrev = currentPage > 0;

        btnNext.setVisible(allowNext);
        btnNext.setManaged(allowNext);
        btnPrev.setVisible(allowPrev);
        btnPrev.setManaged(allowPrev);
    }

    private void onlyAdminPaymentSearchOptions(){
        if(!AuthSession.getRole().equals("ROLE_ADMIN")){
            toggleState.setVisible(false);
            toggleState.setManaged(false);
            txtId.setVisible(false);
            txtId.setManaged(false);
            btnSearch.setVisible(false);
            btnSearch.setManaged(false);
            comboBoxSearchType.setVisible(false);
            comboBoxSearchType.setManaged(false);
        }else{
            toggleState.setVisible(false);
            toggleState.setManaged(false);
        }
    }

    public void refreshCurrentPage() {
        searchPayment(null);
    }
}
