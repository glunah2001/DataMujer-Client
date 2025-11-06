package com.uned.clientedatamujer.controller.view.base;

import com.jfoenix.controls.JFXSnackbar;
import com.uned.clientedatamujer.controller.util.UIUXFeedbackUtils;
import com.uned.clientedatamujer.controller.view.scene.MainController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public abstract class BaseSubSceneController {

    protected MainController mainController;

    public MainController getMainController() {return mainController;}

    public void setMainController(MainController mainController) {this.mainController = mainController;}

    public <T> void setCard(String fxml, VBox vBox, T content, BaseSubSceneController parent){
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(fxml)
            );
            HBox item = loader.load();
            BaseCardController<T> controller = loader.getController();

            controller.setParentController(parent);
            controller.setData(content);

            item.maxWidthProperty().bind(vBox.widthProperty());

            vBox.getChildren().add(item);
        } catch (IOException e) {
            UIUXFeedbackUtils.showSuccessSnackbar("Corrupción en la ruta de recursos.");
        }
    }

    public <T> void setForm(String fxml, VBox vBox, BaseSubSceneController parent, T type){
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(fxml)
            );
            HBox item = loader.load();
            BaseFormController<T> controller = loader.getController();

            controller.setParentController(parent);
            controller.setRoot(item);

            item.setUserData(controller);

            item.maxWidthProperty().bind(vBox.widthProperty());

            vBox.getChildren().add(item);
        } catch (IOException e) {
            UIUXFeedbackUtils.showSuccessSnackbar("Corrupción en la ruta de recursos.");
        }
    }
}
