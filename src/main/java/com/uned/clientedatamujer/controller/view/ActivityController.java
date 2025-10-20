package com.uned.clientedatamujer.controller.view;

import com.uned.clientedatamujer.controller.util.ComponentInitializer;
import com.uned.clientedatamujer.dto.SimplePage;
import com.uned.clientedatamujer.dto.response.ActivityDTO;
import com.uned.clientedatamujer.service.ActivityService;
import com.uned.clientedatamujer.service.AuthSession;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.List;

public class ActivityController extends BaseSubSceneController{

    @FXML
    private TextField txtID;
    @FXML
    private Button btnPrev;
    @FXML
    private Button btnNext;
    @FXML
    private VBox VBoxActivities;
    private final ActivityService activityService = new ActivityService();

    private int currentPage;

    @FXML
    private void initialize(){
        setRootPane(rootPane);
        setSnackBarInfo(snackBarInfo);
        ComponentInitializer.configureLongOnlyTextField(txtID);
        Platform.runLater(() -> {
            currentPage = 0;
            getPageData(currentPage);
        });
    }

    @FXML
    private void searchActivity(ActionEvent event) {
        String id = txtID.getText().trim();
        if(id.isEmpty()){
            currentPage = 0;
            getPageData(currentPage);
        }
        else getSingleData(id);
    }

    @FXML
    private void showPrevious(ActionEvent event) {
        currentPage--;
        getPageData(currentPage);
    }

    @FXML
    private void showNext(ActionEvent event) {
        currentPage++;
        getPageData(currentPage);
    }

    private void getPageData(int page) {
        mainController.executeCall(
                () -> activityService.getNonFinishedActivities(AuthSession.getAccessToken(), page),
                (SimplePage<ActivityDTO> simplePage) -> {
                    VBoxActivities.getChildren().clear();
                    allowPageableButtons(simplePage.currentPage(), simplePage.totalPages());
                    List<ActivityDTO> dto = simplePage.content();
                    dto.forEach(activity -> {
                        setCard(
                                "/com/uned/clientedatamujer/activity-container.fxml",
                                VBoxActivities,
                                activity
                        );
                    });
                }
        );
    }

    private void getSingleData(String id){
        VBoxActivities.getChildren().clear();
        mainController.executeCall(
                () -> activityService.getActivityById(AuthSession.getAccessToken(), id),
                (ActivityDTO dto) -> {
                    allowPageableButtons(0, 0);
                    setCard(
                            "/com/uned/clientedatamujer/activity-container.fxml",
                            VBoxActivities,
                            dto
                    );
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
}
