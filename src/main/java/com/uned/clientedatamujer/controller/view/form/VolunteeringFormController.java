package com.uned.clientedatamujer.controller.view.form;

import com.uned.clientedatamujer.controller.util.ComponentInitializer;
import com.uned.clientedatamujer.controller.view.base.BaseFormController;
import com.uned.clientedatamujer.controller.view.base.BaseSubSceneController;
import com.uned.clientedatamujer.controller.view.subscene.NewVolunteeringController;
import com.uned.clientedatamujer.dto.request.BaseVolunteeringRegisterDTO;
import com.uned.clientedatamujer.dto.request.VolunteeringRegisterDTO;
import com.uned.clientedatamujer.service.util.DataUtilities;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

public class VolunteeringFormController implements BaseFormController<VolunteeringRegisterDTO> {

    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtRole;
    @FXML
    private DatePicker dtpStartDate;
    @FXML
    private Spinner<Integer> spinnerStartHour;
    @FXML
    private Spinner<Integer> spinnerStartMinutes;
    @FXML
    private DatePicker dtpEndDate;
    @FXML
    private Spinner<Integer> spinnerEndHour;
    @FXML
    private Spinner<Integer> spinnerEndMinutes;
    @FXML
    private Button btnDelete;

    private BaseSubSceneController parent;
    private Node root;

    @FXML
    private void initialize(){
        ComponentInitializer.initializeSpinnerHours(spinnerStartHour);
        ComponentInitializer.initializeSpinnerHours(spinnerEndHour);
        ComponentInitializer.initializeSpinnerMinutes(spinnerStartMinutes);
        ComponentInitializer.initializeSpinnerMinutes(spinnerEndMinutes);
        Platform.runLater(() -> {
            if (parent instanceof NewVolunteeringController nvc) {
                txtUsername.setVisible(nvc.isToggleActive());
                txtUsername.setManaged(nvc.isToggleActive());
                btnDelete.setVisible(nvc.isToggleActive());
                btnDelete.setManaged(nvc.isToggleActive());

                dtpStartDate.setValue(LocalDate.from(DataUtilities.getLastActivityDTO().startDate()));
                dtpEndDate.setValue(LocalDate.from(DataUtilities.getLastActivityDTO().endDate()));
                spinnerStartHour.getValueFactory().setValue(
                        DataUtilities.getLastActivityDTO().startDate().getHour()
                );
                spinnerStartMinutes.getValueFactory().setValue(
                        DataUtilities.getLastActivityDTO().startDate().getMinute()
                );
                spinnerEndHour.getValueFactory().setValue(
                        DataUtilities.getLastActivityDTO().endDate().getHour()
                );
                spinnerEndMinutes.getValueFactory().setValue(
                        DataUtilities.getLastActivityDTO().endDate().getMinute()
                );
            }
        });
    }

    @Override
    public void setParentController(BaseSubSceneController parent) {
        this.parent = parent;
    }

    @Override
    public void setRoot(HBox node) {
        root = node;
    }

    @Override
    public VolunteeringRegisterDTO sendData() {
        var startDate = dtpStartDate.getValue();
        var endDate = dtpEndDate.getValue();
        if(verifyDates(startDate) || verifyDates(endDate)) return null;
        var startHour = LocalTime.of(spinnerStartHour.getValue(), spinnerStartMinutes.getValue());
        var endHour = LocalTime.of(spinnerEndHour.getValue(), spinnerEndMinutes.getValue());

        var startShift = LocalDateTime.of(startDate, startHour);
        var endShift = LocalDateTime.of(endDate, endHour);

        var dto = new BaseVolunteeringRegisterDTO(
                DataUtilities.getLastActivityDTO().id(),
                startShift,
                endShift,
                txtRole.getText().trim()
        );
        return new VolunteeringRegisterDTO(dto, txtUsername.getText());
    }

    private boolean verifyDates(LocalDate date){return date == null;}

    public void deleteForm(ActionEvent event) {
        if (root != null && root.getParent() instanceof VBox vbox) {
            vbox.getChildren().remove(root);
        }
    }
}
