package com.uned.clientedatamujer.controller.view;

import com.jfoenix.controls.JFXSnackbar;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public abstract class BaseSubSceneController {

    protected MainController mainController;
    protected StackPane rootPane;
    protected JFXSnackbar snackBarInfo;

    public void setMainController(MainController mainController) {this.mainController = mainController;}

    public void setRootPane(StackPane rootPane) {
        this.rootPane = rootPane;
    }

    public void setSnackBarInfo(JFXSnackbar snackBarInfo) {
        this.snackBarInfo = snackBarInfo;
    }

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
            mainController.showSuccessSnackBar("Corrupción en la ruta de recursos.");
        }
    }
}
