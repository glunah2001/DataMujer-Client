package com.uned.clientedatamujer.controller.view.form;

import com.uned.clientedatamujer.controller.view.base.BaseFormController;
import com.uned.clientedatamujer.controller.view.base.BaseSubSceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class UserFormController implements BaseFormController<String> {
    @FXML
    private TextField txtUsername;
    private Node root;

    @Override
    public void setParentController(BaseSubSceneController parent) {}

    @Override
    public void setRoot(HBox node) {
        root = node;
    }

    @Override
    public String sendData() {
        return txtUsername.getText().trim();
    }

    public void deleteForm(ActionEvent event) {
        if (root != null && root.getParent() instanceof VBox vbox) {
            vbox.getChildren().remove(root);
        }
    }
}
