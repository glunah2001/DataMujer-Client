package com.uned.clientedatamujer.controller.view;

import com.uned.clientedatamujer.dto.SimplePage;
import com.uned.clientedatamujer.dto.response.ActivityDTO;
import com.uned.clientedatamujer.service.ActivityService;
import com.uned.clientedatamujer.service.AuthSession;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class ActivityController extends BaseSubSceneController{

    @FXML
    private TextField txtID;
    @FXML
    private Button btnSearch;
    @FXML
    private Button btnPrev;
    @FXML
    private Button btnNext;
    @FXML
    private VBox VBoxActivities;
    private final ActivityService activityService = new ActivityService();

    @FXML
    private void initialize(){
        setRootPane(rootPane);
        setSnackBarInfo(snackBarInfo);
        Platform.runLater(() -> {
            getPageData(0);
        });
    }

    @FXML
    private void searchActivity(ActionEvent event) {
        String id = txtID.getText().trim();
        if(id.isEmpty()) getPageData(0);
        else System.out.println("afafafafa");
    }

    @FXML
    private void showPrevious(ActionEvent event) {
    }

    @FXML
    private void showNext(ActionEvent event) {
    }

    private void getPageData(int page) {
        mainController.executeCall(
                () -> activityService.getNonFinishedActivities(AuthSession.getAccessToken(), page),
                (SimplePage<ActivityDTO> simplePage) -> {
                    List<ActivityDTO> dto = simplePage.content();
                    dto.forEach(activity -> {
                        try {
                            FXMLLoader loader = new FXMLLoader(
                                    getClass().getResource("/com/uned/clientedatamujer/activity-container.fxml")
                            );
                            HBox item = loader.load();
                            ActivityCardController controller = loader.getController();
                            controller.setData(activity);

                            item.maxWidthProperty().bind(VBoxActivities.widthProperty());

                            VBoxActivities.getChildren().add(item);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                },
                null
        );
    }

    private ActivityDTO getSingleData(Long id){
        return null;
    }
}
