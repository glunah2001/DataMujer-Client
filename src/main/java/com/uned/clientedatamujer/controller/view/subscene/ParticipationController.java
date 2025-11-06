package com.uned.clientedatamujer.controller.view.subscene;

import com.uned.clientedatamujer.controller.util.ComponentInitializer;
import com.uned.clientedatamujer.controller.util.UIUXFeedbackUtils;
import com.uned.clientedatamujer.controller.view.base.BaseSubSceneController;
import com.uned.clientedatamujer.dto.SimplePage;
import com.uned.clientedatamujer.dto.response.ParticipationDTO;
import com.uned.clientedatamujer.service.AuthSession;
import com.uned.clientedatamujer.service.DataUtilities;
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
import java.util.Objects;

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
            getPageDataMyPending(currentPage);
        });
    }

    @FXML
    private void showPrevious(ActionEvent event) {
        currentPage--;
        getPageDataMyPending(currentPage);
    }

    @FXML
    private void showNext(ActionEvent event) {
        currentPage++;
        getPageDataMyPending(currentPage);
    }

    @FXML
    private void searchParticipation(ActionEvent event) {
        String id = txtId.getText().trim();
        if(id.isEmpty()){
            currentPage = 0;
            getPageDataMyPending(currentPage);
            return;
        }
        int index = comboBoxSearchType.getSelectionModel().getSelectedIndex();
        if(index == 0){
            currentPage = 0;
            getPageDataInActivity(currentPage, id);
        }else if(index == 1){
            getSingleData(id);
        }
    }

    private void getPageDataMyPending(int page) {
        mainController.executeCall(
                () -> service.getMyParticipation(page),
                (SimplePage<ParticipationDTO> simplePage) -> {
                    VBoxParticipations.getChildren().clear();
                    allowPageableButtons(simplePage.currentPage(), simplePage.totalPages());
                    List<ParticipationDTO> dto = simplePage.content();
                    dto.forEach(participation -> {
                        setCard(
                                "/com/uned/clientedatamujer/views/card/participation-container.fxml",
                                VBoxParticipations,
                                participation,
                                this
                        );
                    });
                    DataUtilities.clearLastContent();
                    UIUXFeedbackUtils.hideLoading();
                    allowPrintButtons(false);
                }
        );
    }

    private void getPageDataInActivity(int page, String id) {
        mainController.executeCall(
                () -> service.getParticipationInActivity(id, page),
                (SimplePage<ParticipationDTO> simplePage) -> {
                    VBoxParticipations.getChildren().clear();
                    allowPageableButtons(simplePage.currentPage(), simplePage.totalPages());
                    List<ParticipationDTO> dto = simplePage.content();
                    dto.forEach(participation -> {
                        setCard(
                                "/com/uned/clientedatamujer/views/card/participation-container.fxml",
                                VBoxParticipations,
                                participation,
                                this
                        );
                    });
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
                    setCard(
                            "/com/uned/clientedatamujer/views/card/participation-container.fxml",
                            VBoxParticipations,
                            dto,
                            this
                    );
                    DataUtilities.clearLastContent();
                    UIUXFeedbackUtils.hideLoading();
                    allowPrintButtons(false);
                }
        );
    }

    @Override
    public void refreshCurrentPage() {
        getPageDataMyPending(currentPage);
    }

    @FXML
    private void showPrint(ActionEvent event) {
        ReportService.genReportParticipation();
    }
}
