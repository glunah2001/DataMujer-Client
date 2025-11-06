package com.uned.clientedatamujer.controller.view.subscene;

import com.uned.clientedatamujer.controller.util.ComponentInitializer;
import com.uned.clientedatamujer.controller.util.UIUXFeedbackUtils;
import com.uned.clientedatamujer.controller.view.base.BaseSubSceneController;
import com.uned.clientedatamujer.dto.SimplePage;
import com.uned.clientedatamujer.dto.response.VolunteeringDTO;
import com.uned.clientedatamujer.service.util.AuthSession;
import com.uned.clientedatamujer.service.util.DataUtilities;
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

public class VolunteeringController extends BaseSubSceneController {
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
        ComponentInitializer.configureLongOnlyTextField(txtId);
        if(AuthSession.getRole().equals("ROLE_MENTOR")){
            comboBoxSearchType.getItems().remove("ID");
        }

        setPrev(btnPrev);
        setNext(btnNext);
        setPrint(btnPrint);
        setVBox(VBoxVolunteering);

        Platform.runLater(() -> {
            DataUtilities.clearAll();
            currentPage = 0;
            getPageDataMyPending();
        });
    }

    private void getPageDataMyPending() {
        mainController.executeCall(
                () -> service.getMyPendingVolunteering(currentPage),
                (SimplePage<VolunteeringDTO> simplePage) -> {
                    VBoxVolunteering.getChildren().clear();
                    allowPageableButtons(simplePage.currentPage(), simplePage.totalPages());
                    List<VolunteeringDTO> dto = simplePage.content();
                    dto.forEach(this::addCard);
                    UIUXFeedbackUtils.hideLoading();
                }
        );
        DataUtilities.clearLastContent();
        allowPrintButtons(false);
    }

    private void getPageDataInActivity(String id) {
        mainController.executeCall(
                () -> service.getVolunteeringInActivity(id, currentPage),
                (SimplePage<VolunteeringDTO> simplePage) -> {
                    VBoxVolunteering.getChildren().clear();
                    allowPageableButtons(simplePage.currentPage(), simplePage.totalPages());
                    List<VolunteeringDTO> dto = simplePage.content();
                    dto.forEach(this::addCard);
                    DataUtilities.setLastContent(dto);
                    UIUXFeedbackUtils.hideLoading();
                    allowPrintButtons(true);
                }
        );
    }

    private void getSingleData(String id){
        VBoxVolunteering.getChildren().clear();
        mainController.executeCall(
                () -> service.getVolunteeringById(id),
                (VolunteeringDTO dto) -> {
                    allowPageableButtons(0, 0);
                    addCard(dto);
                    DataUtilities.clearLastContent();
                    UIUXFeedbackUtils.hideLoading();
                    allowPrintButtons(false);
                }
        );
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
        ReportService.genReportVolunteering();
    }

    private void addCard(VolunteeringDTO dto){
        setCard(
                "/com/uned/clientedatamujer/views/card/volunteering-container.fxml",
                dto,
                this
        );
    }

    @Override
    public void refreshCurrentPage() {
        getPageDataMyPending();
    }
}
