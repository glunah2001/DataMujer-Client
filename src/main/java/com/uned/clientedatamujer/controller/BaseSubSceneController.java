package com.uned.clientedatamujer.controller;

import com.jfoenix.controls.JFXSnackbar;
import javafx.scene.layout.StackPane;

public abstract class BaseSubSceneController {

    protected MainController mainController;
    protected StackPane rootPane;
    protected JFXSnackbar snackBarInfo;

    protected void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setRootPane(StackPane rootPane) {
        this.rootPane = rootPane;
    }

    public void setSnackBarInfo(JFXSnackbar snackBarInfo) {
        this.snackBarInfo = snackBarInfo;
    }
}
