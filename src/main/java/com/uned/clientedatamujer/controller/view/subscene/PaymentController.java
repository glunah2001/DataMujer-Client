package com.uned.clientedatamujer.controller.view.subscene;

import com.jfoenix.controls.JFXToggleButton;
import com.uned.clientedatamujer.controller.util.ComponentInitializer;
import com.uned.clientedatamujer.controller.util.UIUXFeedbackUtils;
import com.uned.clientedatamujer.controller.view.base.BaseSubSceneController;
import com.uned.clientedatamujer.dto.SimplePage;
import com.uned.clientedatamujer.dto.response.AffiliatesPaymentReportDTO;
import com.uned.clientedatamujer.dto.response.PaymentDTO;
import com.uned.clientedatamujer.service.util.AuthSession;
import com.uned.clientedatamujer.service.util.DataUtilities;
import com.uned.clientedatamujer.service.PaymentService;
import com.uned.clientedatamujer.service.ReportService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.List;

public class PaymentController extends BaseSubSceneController {
    @FXML
    private Button btnPrint;
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

        setPrev(btnPrev);
        setNext(btnNext);
        setPrint(btnPrint);
        setVBox(VBoxPayment);

        Platform.runLater(() -> {
            DataUtilities.clearAll();
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
        }else if(index == 1){
            getPageDataStatus(currentPage);
        }else{
            getPageDataAffiliate();
        }
    }

    @FXML
    private void showPrint(ActionEvent event) {
        ReportService.genReportAffiliate();
    }

    private void getPageDataAffiliate() {
        VBoxPayment.getChildren().clear();
        mainController.executeCall(
                () -> service.getAffiliateReport(currentPage),
                (SimplePage<AffiliatesPaymentReportDTO> simplePage) -> {
                    VBoxPayment.getChildren().clear();
                    allowPageableButtons(simplePage.currentPage(), simplePage.totalPages());
                    List<AffiliatesPaymentReportDTO> dto = simplePage.content();
                    dto.forEach(this::addCard);
                    DataUtilities.setLastContent(dto);
                    UIUXFeedbackUtils.hideLoading();
                    allowPrintButtons(true);
                }
        );
    }

    private void getPageDataMyPayments(int currentPage){
        VBoxPayment.getChildren().clear();
        mainController.executeCall(
                () -> service.getMyPayment(currentPage),
                (SimplePage<PaymentDTO> simplePage) -> {
                    VBoxPayment.getChildren().clear();
                    allowPageableButtons(simplePage.currentPage(), simplePage.totalPages());
                    List<PaymentDTO> dto = simplePage.content();
                    dto.forEach(this::addCard);
                    DataUtilities.clearLastContent();
                    UIUXFeedbackUtils.hideLoading();
                    allowPrintButtons(false);
                }
        );
    }

    private void getPageDataStatus(int currentPage){
        VBoxPayment.getChildren().clear();
        mainController.executeCall(
                () -> service.getPaymentByStatus(
                        toggleState.isSelected(),
                        currentPage
                ),
                (SimplePage<PaymentDTO> simplePage) -> {
                    VBoxPayment.getChildren().clear();
                    allowPageableButtons(simplePage.currentPage(), simplePage.totalPages());
                    List<PaymentDTO> dto = simplePage.content();
                    dto.forEach(this::addCard);
                    DataUtilities.clearLastContent();
                    UIUXFeedbackUtils.hideLoading();
                    allowPrintButtons(false);
                }
        );
    }

    private void getSingleData(String id){
        VBoxPayment.getChildren().clear();
        mainController.executeCall(
                () -> service.getPaymentById(id),
                (PaymentDTO dto) -> {
                    allowPageableButtons(0, 0);
                    addCard(dto);
                    DataUtilities.clearLastContent();
                    UIUXFeedbackUtils.hideLoading();
                    allowPrintButtons(false);
                }
        );
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

    private void addCard(PaymentDTO dto){
        setCard(
                "/com/uned/clientedatamujer/views/card/payment-container.fxml",
                dto,
                this
        );
    }

    private void addCard(AffiliatesPaymentReportDTO dto) {
        setCard(
                "/com/uned/clientedatamujer/views/card/affiliate-report-container.fxml",
                dto,
                this
        );
    }

    @Override
    public void refreshCurrentPage() {
        searchPayment(null);
    }
}
