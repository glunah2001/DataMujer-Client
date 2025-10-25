package com.uned.clientedatamujer.controller.view;

import javafx.event.ActionEvent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class PaymentUpdateController extends BaseSubSceneController{


    public DatePicker dtpStartDate;
    public Spinner<Integer> spinnerPayMinutes;
    public Spinner<Integer> spinnerPayHour;
    public TextField txtId;
    public TextArea txtDescription;

    public void update(ActionEvent event) {
    }
}
