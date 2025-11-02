package com.uned.clientedatamujer.controller.view;

import com.uned.clientedatamujer.controller.util.ComponentInitializer;
import com.uned.clientedatamujer.dto.SimplePage;
import com.uned.clientedatamujer.dto.response.VolunteeringDTO;
import com.uned.clientedatamujer.service.AuthSession;
import com.uned.clientedatamujer.service.ReportService;
import com.uned.clientedatamujer.service.VolunteeringService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Objects;

public class VolunteeringController extends BaseSubSceneController{
    @FXML
    private ComboBox<String> comboBoxSearchType;
    @FXML
    private TextField txtId;
    @FXML
    private Button btnPrev;
    @FXML
    private Button btnNext;
    @FXML
    private Button btnPrint;
    @FXML
    private VBox VBoxVolunteering;

    private int currentPage;
    private final VolunteeringService service = new VolunteeringService();

    @FXML
    private void initialize(){
        setRootPane(rootPane);
        setSnackBarInfo(snackBarInfo);
        ComponentInitializer.configureLongOnlyTextField(txtId);
        if(AuthSession.getRole().equals("ROLE_MENTOR")){
            comboBoxSearchType.getItems().remove("ID");
        }
        Platform.runLater(() -> {
            currentPage = 0;
            getPageDataMyPending();
        });
    }

    private void getPageDataMyPending() {
        mainController.executeCall(
                () -> service.getMyPendingVolunteering(AuthSession.getAccessToken(), currentPage),
                (SimplePage<VolunteeringDTO> simplePage) -> {
                    VBoxVolunteering.getChildren().clear();
                    allowPageableButtons(simplePage.currentPage(), simplePage.totalPages());
                    List<VolunteeringDTO> dto = simplePage.content();
                    dto.forEach(volunteering -> {
                        setCard(
                                "/com/uned/clientedatamujer/views/card/volunteering-container.fxml",
                                VBoxVolunteering,
                                volunteering,
                                this
                        );
                    });
                    mainController.hideLoading();
                }
        );
        allowPrintButtons(false);
    }

    private void getPageDataInActivity(String id) {
        mainController.executeCall(
                () -> service.getVolunteeringInActivity(AuthSession.getAccessToken(), id, currentPage),
                (SimplePage<VolunteeringDTO> simplePage) -> {
                    VBoxVolunteering.getChildren().clear();
                    allowPageableButtons(simplePage.currentPage(), simplePage.totalPages());
                    List<VolunteeringDTO> dto = simplePage.content();
                    dto.forEach(volunteering -> {
                        setCard(
                                "/com/uned/clientedatamujer/views/card/volunteering-container.fxml",
                                VBoxVolunteering,
                                volunteering,
                                this
                        );
                    });
                    mainController.hideLoading();
                }
        );
        allowPrintButtons(true);
    }

    private void getSingleData(String id){
        VBoxVolunteering.getChildren().clear();
        mainController.executeCall(
                () -> service.getVolunteeringById(AuthSession.getAccessToken(), id),
                (VolunteeringDTO dto) -> {
                    allowPageableButtons(0, 0);
                    setCard(
                            "/com/uned/clientedatamujer/views/card/volunteering-container.fxml",
                            VBoxVolunteering,
                            dto,
                            this
                    );
                    mainController.hideLoading();
                }
        );
        allowPrintButtons(false);
    }

    @FXML
    private void showPrevious(ActionEvent event) {
        currentPage--;
        getPageDataMyPending();
    }

    @FXML
    private void showNext(ActionEvent event) {
        currentPage++;
        getPageDataMyPending();
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

    private void allowPrintButtons(boolean allow){
        if(!Objects.equals(AuthSession.getRole(), "ROLE_ADMIN")) return;
        if(allow && VBoxVolunteering.getChildren().isEmpty()){
            btnPrint.setVisible(false);
            btnPrint.setManaged(false);
            return;
        }
        btnPrint.setVisible(allow);
        btnPrint.setManaged(allow);
    }

    @FXML
    private void searchVolunteering(ActionEvent event) {
        String id = txtId.getText().trim();
        if(id.isEmpty()){
            currentPage = 0;
            getPageDataMyPending();
            return;
        }
        int index = comboBoxSearchType.getSelectionModel().getSelectedIndex();
        if(index == 0){
            currentPage = 0;
            getPageDataInActivity(id);
        }else if(index == 1){
            getSingleData(id);
        }
    }

    @FXML
    private void showPrint(ActionEvent event) {
        String id = txtId.getText().trim();
        if(id.isEmpty()) return;
        mainController.executeCall(
                () -> service.getVolunteeringInActivity(AuthSession.getAccessToken(), id, currentPage),
                (SimplePage<VolunteeringDTO> simplePage) -> {
                    ReportService.genReportVolunteering(simplePage.content());
                    mainController.hideLoading();
                }
        );
    }

    public void refreshCurrentPage() {
        getPageDataMyPending();
    }
}
