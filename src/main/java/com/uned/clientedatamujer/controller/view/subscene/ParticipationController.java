package com.uned.clientedatamujer.controller.view.subscene;

import com.uned.clientedatamujer.controller.util.ComponentInitializer;
import com.uned.clientedatamujer.controller.util.UIUXFeedbackUtils;
import com.uned.clientedatamujer.controller.view.base.BaseSubSceneController;
import com.uned.clientedatamujer.dto.SimplePage;
import com.uned.clientedatamujer.dto.response.ParticipationDTO;
import com.uned.clientedatamujer.service.util.AuthSession;
import com.uned.clientedatamujer.service.util.DataUtilities;
import com.uned.clientedatamujer.service.ParticipationService;
import com.uned.clientedatamujer.service.ReportService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.List;

public class ParticipationController extends BaseSubSceneController {

    @FXML
    private VBox VBoxParticipations;
    @FXML
    private Button btnPrint;
    @FXML
    private Button btnPrev;
    @FXML
    private Button btnNext;
    @FXML
    private TextField txtId;
    @FXML
    private ComboBox<String> comboBoxSearchType;
    @FXML
    private Button btnSearch;

    private final ParticipationService service = new ParticipationService();
    private int currentPage;


    @FXML
    private void initialize(){
        ComponentInitializer.configureLongOnlyTextField(txtId);
        if(AuthSession.getRole().equals("ROLE_STANDARD")){
            btnSearch.setManaged(false);
            btnSearch.setVisible(false);
            txtId.setVisible(false);
            txtId.setManaged(false);
            comboBoxSearchType.setVisible(false);
            comboBoxSearchType.setManaged(false);
        }else if(AuthSession.getRole().equals("ROLE_MENTOR")){
            comboBoxSearchType.getItems().remove("ID");
        }
        setPrev(btnPrev);
        setNext(btnNext);
        setVBox(VBoxParticipations);
        setPrint(btnPrint);
        Platform.runLater(() -> {
            DataUtilities.clearAll();
            currentPage = 0;
            getPageDataMyPending();
        });
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
    private void searchParticipation(ActionEvent event) {
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

    private void getPageDataMyPending() {
        mainController.executeCall(
                () -> service.getMyParticipation(currentPage),
                (SimplePage<ParticipationDTO> simplePage) -> {
                    VBoxParticipations.getChildren().clear();
                    allowPageableButtons(simplePage.currentPage(), simplePage.totalPages());
                    List<ParticipationDTO> dto = simplePage.content();
                    dto.forEach(this::addCard);
                    DataUtilities.clearLastContent();
                    UIUXFeedbackUtils.hideLoading();
                    allowPrintButtons(false);
                }
        );
    }

    private void getPageDataInActivity(String id) {
        mainController.executeCall(
                () -> service.getParticipationInActivity(id, currentPage),
                (SimplePage<ParticipationDTO> simplePage) -> {
                    VBoxParticipations.getChildren().clear();
                    allowPageableButtons(simplePage.currentPage(), simplePage.totalPages());
                    List<ParticipationDTO> dto = simplePage.content();
                    dto.forEach(this::addCard);
                    DataUtilities.setLastContent(dto);
                    UIUXFeedbackUtils.hideLoading();
                    allowPrintButtons(true);
                }
        );
    }

    private void getSingleData(String id){
        VBoxParticipations.getChildren().clear();
        mainController.executeCall(
                () -> service.getParticipationById(id),
                (ParticipationDTO dto) -> {
                    allowPageableButtons(0, 0);
                    addCard(dto);
                    DataUtilities.clearLastContent();
                    UIUXFeedbackUtils.hideLoading();
                    allowPrintButtons(false);
                }
        );
    }

    private void addCard(ParticipationDTO dto){
        setCard(
                "/com/uned/clientedatamujer/views/card/participation-container.fxml",
                dto,
                this
        );
    }

    @Override
    public void refreshCurrentPage() {
        getPageDataMyPending();
    }

    @FXML
    private void showPrint(ActionEvent event) {
        UIUXFeedbackUtils.showLoading();
        mainController.runAsync(
                () -> {
                    ReportService.genReportParticipation();
                    mainController.runLater(UIUXFeedbackUtils::hideLoading);
                }
        );
    }
}
