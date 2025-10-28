package com.uned.clientedatamujer.controller.view;

import com.uned.clientedatamujer.controller.util.ComponentInitializer;
import com.uned.clientedatamujer.dto.SimplePage;
import com.uned.clientedatamujer.dto.response.LegalPersonDTO;
import com.uned.clientedatamujer.dto.response.PhysicalPersonDTO;
import com.uned.clientedatamujer.dto.response.ProfileDTO;
import com.uned.clientedatamujer.service.AuthSession;
import com.uned.clientedatamujer.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.List;

public class AdminController extends BaseSubSceneController{

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
    private int currentPage;

    @FXML
    private void initialize(){
        ComponentInitializer.initializeParam(txtParam, comboBoxParamType);
        currentPage = 0;
    }

    @FXML
    private void searchUser(ActionEvent event) {
        if(txtParam.getText().isEmpty()) return;
        int index = comboBoxParamType.getSelectionModel().getSelectedIndex();
        String param = txtParam.getText();
        switch (index){
            case 0 -> searchUserByUsername(param);
            case 1 -> searchUserByNationalId(param);
            case 2 -> searchUserByLegalId(param);
            case 3 -> searchUserByName(param);
            case 4 -> searchUserByBusiness(param);
            case 5 -> searchUserBySurnames(param);
        }
    }

    private void searchUserBySurnames(String param) {
        mainController.executeCall(
                () -> service.getUserBySurname(AuthSession.getAccessToken(), currentPage, param),
                (SimplePage<PhysicalPersonDTO> simplePage) -> {
                    clearVBox();
                    allowPageableButtons(simplePage.currentPage(), simplePage.totalPages());
                    List<PhysicalPersonDTO> dto = simplePage.content();
                    dto.forEach(person -> {
                        setCard(
                                "/com/uned/clientedatamujer/user-container.fxml",
                                VBoxUser,
                                person,
                                this
                        );
                    });
                    mainController.hideLoading();
                }
        );
    }

    private void searchUserByBusiness(String param) {
        mainController.executeCall(
                () -> service.getUserByBusiness(AuthSession.getAccessToken(), currentPage, param),
                (SimplePage<LegalPersonDTO> simplePage) -> {
                    clearVBox();
                    allowPageableButtons(simplePage.currentPage(), simplePage.totalPages());
                    List<LegalPersonDTO> dto = simplePage.content();
                    dto.forEach(person -> {
                        setCard(
                                "/com/uned/clientedatamujer/user-container.fxml",
                                VBoxUser,
                                person,
                                this
                        );
                    });
                    mainController.hideLoading();
                }
        );
    }

    private void searchUserByName(String param) {
        mainController.executeCall(
                () -> service.getUserByName(AuthSession.getAccessToken(), currentPage, param),
                (SimplePage<PhysicalPersonDTO> simplePage) -> {
                    clearVBox();
                    allowPageableButtons(simplePage.currentPage(), simplePage.totalPages());
                    List<PhysicalPersonDTO> dto = simplePage.content();
                    dto.forEach(person -> {
                        setCard(
                                "/com/uned/clientedatamujer/user-container.fxml",
                                VBoxUser,
                                person,
                                this
                        );
                    });
                    mainController.hideLoading();
                }
        );
    }

    private void searchUserByLegalId(String param) {
        clearVBox();
        mainController.executeCall(
                () -> service.getUserByLegalId(AuthSession.getAccessToken(), param),
                (LegalPersonDTO dto) -> {
                    allowPageableButtons(0, 0);
                    setCard(
                            "/com/uned/clientedatamujer/user-container.fxml",
                            VBoxUser,
                            dto,
                            this
                    );
                    mainController.hideLoading();
                }
        );
    }

    private void searchUserByNationalId(String param) {
        clearVBox();
        mainController.executeCall(
                () -> service.getUserByNationalId(AuthSession.getAccessToken(), param),
                (PhysicalPersonDTO dto) -> {
                    allowPageableButtons(0, 0);
                    setCard(
                            "/com/uned/clientedatamujer/user-container.fxml",
                            VBoxUser,
                            dto,
                            this
                    );
                    mainController.hideLoading();
                }
        );
    }

    private void searchUserByUsername(String param) {
        clearVBox();
        mainController.executeCall(
                () -> service.getUserByUsername(AuthSession.getAccessToken(), param),
                (ProfileDTO dto) -> {
                    allowPageableButtons(0, 0);
                    setCard(
                            "/com/uned/clientedatamujer/user-container.fxml",
                            VBoxUser,
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

    @FXML
    private void showPrevious(ActionEvent event) {
        currentPage--;
        //getPageData(currentPage);
    }

    @FXML
    private void showNext(ActionEvent event) {
        currentPage++;
        //getPageData(currentPage);
    }

    public void refreshCurrentPage() {
        if(txtParam.getText().isEmpty()){
            clearVBox();
            return;
        }

    }

    private void clearVBox(){
        VBoxUser.getChildren().clear();
    }
}
