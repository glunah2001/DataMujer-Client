package com.uned.clientedatamujer.controller.view.scene;

import com.uned.clientedatamujer.controller.util.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import java.io.IOException;

public class TypePersonController {

    @FXML
    private ComboBox<String> comboBoxPersonType;

    @FXML
    private void ToLoginView(ActionEvent event) throws IOException {
        SceneManager.toLogIn();
    }

    @FXML
    private void ToRegisterView(ActionEvent event) throws IOException{
        String scene;
        int index = comboBoxPersonType.getSelectionModel().getSelectedIndex();
        switch (index) {
            case 0 -> scene = "/com/uned/clientedatamujer/views/scene/physical-person-view.fxml";
            case 1 -> scene = "/com/uned/clientedatamujer/views/scene/legal-person-view.fxml";
            default -> {
                return;
            }
        }

        SceneManager.changeScene(
                scene, 1080, 720, false
        );
    }

}
