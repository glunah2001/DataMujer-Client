package com.uned.clientedatamujer.controller.view.subscene;

import com.uned.clientedatamujer.controller.util.ComponentInitializer;
import com.uned.clientedatamujer.controller.util.UIUXFeedbackUtils;
import com.uned.clientedatamujer.controller.view.base.BaseSubSceneController;
import com.uned.clientedatamujer.dto.SimplePage;
import com.uned.clientedatamujer.dto.response.ActivityDTO;
import com.uned.clientedatamujer.service.ActivityService;
import com.uned.clientedatamujer.service.util.DataUtilities;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.List;

public class ActivityController extends BaseSubSceneController {

    @FXML
    private TextField txtID;
    @FXML
    private Button btnPrev;
    @FXML
    private Button btnNext;
    @FXML
    private VBox VBoxActivities;
    private final ActivityService service = new ActivityService();

    @FXML
    private void initialize(){
        ComponentInitializer.configureLongOnlyTextField(txtID);
        setPrev(btnPrev);
        setNext(btnNext);
        setVBox(VBoxActivities);
        Platform.runLater(() -> {
            DataUtilities.clearAll();
            currentPage = 0;
            getPageData();
        });
    }

    @FXML
    private void searchActivity(ActionEvent event) {
        String id = txtID.getText().trim();
        if(id.isEmpty()){
            currentPage = 0;
            getPageData();
        }
        else getSingleData(id);
    }

    @FXML
    private void showPrevious(ActionEvent event) {
        currentPage--;
        getPageData();
    }

    @FXML
    private void showNext(ActionEvent event) {
        currentPage++;
        getPageData();
    }

    private void getPageData() {
        mainController.executeCall(
                () -> service.getNonFinishedActivities(currentPage),
                (SimplePage<ActivityDTO> simplePage) -> {
                    VBoxActivities.getChildren().clear();
                    allowPageableButtons(simplePage.currentPage(), simplePage.totalPages());
                    List<ActivityDTO> dto = simplePage.content();
                    dto.forEach(this::addCard);
                    UIUXFeedbackUtils.hideLoading();
                }
        );
    }

    private void getSingleData(String id){
        VBoxActivities.getChildren().clear();
        mainController.executeCall(
                () -> service.getActivityById(id),
                (ActivityDTO dto) -> {
                    allowPageableButtons(0, 0);
                    addCard(dto);
                    UIUXFeedbackUtils.hideLoading();
                }
        );
    }

    private void addCard(ActivityDTO dto){
        setCard(
                "/com/uned/clientedatamujer/views/card/activity-container.fxml",
                dto,
                this
        );
    }

    @Override
    public void refreshCurrentPage() {
        getPageData();
    }
}
