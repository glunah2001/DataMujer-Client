package com.uned.clientedatamujer.controller.view;

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

        if(index == 0){
            scene = "/com/uned/clientedatamujer/physical-person-view.fxml";
        }else if( index == 1){
            scene = "/com/uned/clientedatamujer/legal-person-view.fxml";
        }else{ return; }

        SceneManager.changeScene(
                scene, 1080, 720, false
        );
    }

}
