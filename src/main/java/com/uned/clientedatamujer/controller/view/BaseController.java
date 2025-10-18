package com.uned.clientedatamujer.controller.view;

import com.jfoenix.controls.JFXSnackbar;
import com.uned.clientedatamujer.controller.util.ThrowingSupplier;
import com.uned.clientedatamujer.controller.util.UIUXFeedbackUtils;
import com.uned.clientedatamujer.dto.ApiError;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class BaseController {

    private StackPane rootPane;
    private JFXSnackbar snackBarInfo;

    protected <T> void executeCall(
            ThrowingSupplier<Object> serviceCall,
            Consumer<T> onSuccess,
            Consumer<ApiError> onError,
            String errorTitle
    ){
        showLoading();
        runAsync(() ->{
            try{
                Object response = serviceCall.get();
                if(response instanceof ApiError error){
                    runLater(()->{
                        handleApiError(error, errorTitle);
                        if(onError != null)onError.accept(error);
                        else hideLoading();
                    });
                }else{
                    T data = (T) response;
                    runLater(() -> {
                        hideLoading();
                        onSuccess.accept(data);
                    });
                }
            }catch(Exception e){
                e.printStackTrace();
                runLater(this::hideLoading);
            }
        });
    }

    protected <T> void executeCall(
            ThrowingSupplier<Object> serviceCall,
            Consumer<T> onSuccess,
            String errorTitle
    ){
        executeCall(serviceCall, onSuccess, null, errorTitle);
    }

    public void setRootPane(StackPane rootPane) {this.rootPane = rootPane;}

    public void setSnackBarInfo(JFXSnackbar snackBarInfo) {this.snackBarInfo = snackBarInfo;}

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

    protected void showLoading(){
        UIUXFeedbackUtils.showLoadingOverlay(rootPane);
    }

    protected void hideLoading(){
        UIUXFeedbackUtils.hideLoadingOverlay(rootPane);
    }

    protected void showErrorSnackBar(String message){
        UIUXFeedbackUtils.errorSnackbar(message, snackBarInfo);
    }

    protected void showSuccessSnackBar(String message){
        UIUXFeedbackUtils.successSnackbar(message, snackBarInfo);
    }

    protected void showErrorDialog(StackPane rootPane, String title, String message){
        UIUXFeedbackUtils.showErrorDialog(rootPane, title, message);
    }

    protected void handleApiError(ApiError error,
                                  String title) {
        if (error.details() != null) {
            String message = error.details().stream()
                    .map(detail -> "* " + detail)
                    .collect(Collectors.joining("\n"));
            showErrorDialog(rootPane, title, message);
            return;
        }

        String message = error.error() + ": " + error.message();
        showErrorSnackBar(message);
    }
}
