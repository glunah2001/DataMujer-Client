package com.uned.clientedatamujer.controller.view.subscene;

import com.uned.clientedatamujer.controller.util.ComponentInitializer;
import com.uned.clientedatamujer.controller.util.UIUXFeedbackUtils;
import com.uned.clientedatamujer.controller.view.base.BaseSubSceneController;
import com.uned.clientedatamujer.dto.SimplePage;
import com.uned.clientedatamujer.dto.response.*;
import com.uned.clientedatamujer.service.AuthSession;
import com.uned.clientedatamujer.service.DataUtilities;
import com.uned.clientedatamujer.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.List;

public class AdminController extends BaseSubSceneController {

    @FXML
    private TextField txtParam;
    @FXML
    private ComboBox<String> comboBoxParamType;
    @FXML
    private Button btnPrev;
    @FXML
    private Button btnNext;
    @FXML
    private VBox VBoxUser;
    private final UserService service = new UserService();
    private String lastSearch = null;
    private int lastIndex = 0;
    private int currentPage;

    @FXML
    private void initialize(){
        ComponentInitializer.initializeParam(txtParam, comboBoxParamType);
        allowPageableButtons(0,0);
    }

    @FXML
    private void searchUser(ActionEvent event) {
        if(txtParam.getText().isEmpty()) return;
        lastSearch = txtParam.getText();
        lastIndex = comboBoxParamType.getSelectionModel().getSelectedIndex();
        switch (lastIndex){
            case 0 -> searchUserByUsername(lastSearch);
            case 1 -> searchUserByNationalId(lastSearch);
            case 2 -> searchUserByLegalId(lastSearch);
            case 3 -> searchUserByName(lastSearch);
            case 4 -> searchUserByBusiness(lastSearch);
            case 5 -> searchUserBySurnames(lastSearch);
        }
    }

    private void searchUserBySurnames(String param) {
        mainController.executeCall(
                () -> service.getUserBySurname(currentPage, param),
                (SimplePage<NoPoliPhysicalPersonDTO> simplePage) -> {
                    clearVBox();
                    var response = DataUtilities.mapToPhysicalProfile(simplePage);
                    allowPageableButtons(response.currentPage(), response.totalPages());
                    List<PhysicalPersonDTO> dto = response.content();
                    dto.forEach(person -> {
                        setCard(
                                "/com/uned/clientedatamujer/views/card/user-container.fxml",
                                VBoxUser,
                                person,
                                this
                        );
                    });
                    UIUXFeedbackUtils.hideLoading();
                }
        );
    }

    private void searchUserByBusiness(String param) {
        mainController.executeCall(
                () -> service.getUserByBusiness(currentPage, param),
                (SimplePage<NoPoliLegalPersonDTO> simplePage) -> {
                    clearVBox();
                    var response = DataUtilities.mapToLegalProfile(simplePage);
                    allowPageableButtons(response.currentPage(), response.totalPages());
                    List<LegalPersonDTO> dto = response.content();
                    dto.forEach(person -> {
                        setCard(
                                "/com/uned/clientedatamujer/views/card/user-container.fxml",
                                VBoxUser,
                                person,
                                this
                        );
                    });
                    UIUXFeedbackUtils.hideLoading();
                }
        );
    }

    private void searchUserByName(String param) {
        mainController.executeCall(
                () -> service.getUserByName(currentPage, param),
                (SimplePage<NoPoliPhysicalPersonDTO> simplePage) -> {
                    clearVBox();
                    var response = DataUtilities.mapToPhysicalProfile(simplePage);
                    allowPageableButtons(response.currentPage(), response.totalPages());
                    List<PhysicalPersonDTO> dto = response.content();
                    dto.forEach(person -> {
                        setCard(
                                "/com/uned/clientedatamujer/views/card/user-container.fxml",
                                VBoxUser,
                                person,
                                this
                        );
                    });
                    UIUXFeedbackUtils.hideLoading();
                }
        );
    }

    private void searchUserByLegalId(String param) {
        clearVBox();
        mainController.executeCall(
                () -> service.getUserByLegalId(param),
                (LegalPersonDTO dto) -> {
                    allowPageableButtons(0, 0);
                    setCard(
                            "/com/uned/clientedatamujer/views/card/user-container.fxml",
                            VBoxUser,
                            dto,
                            this
                    );
                    UIUXFeedbackUtils.hideLoading();
                }
        );
    }

    private void searchUserByNationalId(String param) {
        clearVBox();
        mainController.executeCall(
                () -> service.getUserByNationalId(param),
                (PhysicalPersonDTO dto) -> {
                    allowPageableButtons(0, 0);
                    setCard(
                            "/com/uned/clientedatamujer/views/card/user-container.fxml",
                            VBoxUser,
                            dto,
                            this
                    );
                    UIUXFeedbackUtils.hideLoading();
                }
        );
    }

    private void searchUserByUsername(String param) {
        clearVBox();
        mainController.executeCall(
                () -> service.getUserByUsername(param),
                (ProfileDTO dto) -> {
                    allowPageableButtons(0, 0);
                    setCard(
                            "/com/uned/clientedatamujer/views/card/user-container.fxml",
                            VBoxUser,
                            dto,
                            this
                    );
                    UIUXFeedbackUtils.hideLoading();
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

    @FXML
    private void showPrevious(ActionEvent event) {
        resetToPreviousSearch();
        currentPage--;
        searchUser(null);
    }

    @FXML
    private void showNext(ActionEvent event) {
        resetToPreviousSearch();
        currentPage++;
        searchUser(null);
    }

    public void refreshCurrentPage() {
        resetToPreviousSearch();
        searchUser(null);
    }

    private void resetToPreviousSearch(){
        if (lastSearch == null || lastIndex < 0) {
            clearVBox();
            allowPageableButtons(0, 0);
            return;
        }
        comboBoxParamType.getSelectionModel().select(lastIndex);
        txtParam.setText(lastSearch);
    }

    private void clearVBox(){
        VBoxUser.getChildren().clear();
    }
}
