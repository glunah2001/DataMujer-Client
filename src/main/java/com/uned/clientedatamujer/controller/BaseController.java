package com.uned.clientedatamujer.controller;

import com.jfoenix.controls.JFXSnackbar;
import com.uned.clientedatamujer.dto.ApiError;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class BaseController {

    protected void runAsync(Runnable task){
        new Thread(task).start();
    }

    protected void runLater(Runnable task){
        Platform.runLater(task);
    }

    protected void withDelay(double second, Runnable action){
        PauseTransition delay = new PauseTransition(Duration.seconds(second));
        delay.setOnFinished(event -> action.run());
        delay.play();
    }

    protected void showLoading(StackPane rootPane){
        UIUXFeedbackUtils.showLoadingOverlay(rootPane);
    }

    protected void hideLoading(StackPane rootPane){
        UIUXFeedbackUtils.hideLoadingOverlay(rootPane);
    }

    protected void showErrorSnackBar(String message, JFXSnackbar snackbar){
        UIUXFeedbackUtils.errorSnackbar(message, snackbar);
    }

    protected void showSuccessSnackBar(String message, JFXSnackbar snackbar){
        UIUXFeedbackUtils.successSnackbar(message, snackbar);
    }

    protected void showErrorDialog(StackPane rootPane, String title, String message){
        UIUXFeedbackUtils.showErrorDialog(rootPane, title, message);
    }

    protected void handleApiError(StackPane rootPane,
                                  JFXSnackbar snackbar,
                                  ApiError error,
                                  String title){
        if(error.details() != null){
            String message = error.details().stream()
                    .map(detail -> "* " + detail)
                    .collect(Collectors.joining("\n"));
            showErrorDialog(rootPane, title, message);
            return;
        }

        /*switch (error.error().toUpperCase()){
            case "NOT FOUND", "CONFLICT", "BAD REQUEST", "UNEXPECTED", "SERVICE UNAVAILABLE" -> {*/
                String message = error.error() + ": " + error.message();
                showErrorSnackBar(message, snackbar);
            /*}
            case "UNAUTHORIZED", "FORBIDDEN" -> {}
        }*/
    }
}
