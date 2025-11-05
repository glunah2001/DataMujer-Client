package com.uned.clientedatamujer.controller.view.base;

import com.uned.clientedatamujer.controller.util.SceneManager;
import com.uned.clientedatamujer.controller.util.ThrowingSupplier;
import com.uned.clientedatamujer.controller.util.UIUXFeedbackUtils;
import com.uned.clientedatamujer.dto.ApiError;
import com.uned.clientedatamujer.service.AuthService;
import com.uned.clientedatamujer.service.TokenRefresher;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.util.Duration;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class BaseController {

    public <T> void executeCall(
            ThrowingSupplier<Object> serviceCall,
            Consumer<T> onSuccess,
            Consumer<ApiError> onError,
            String errorTitle
    ){
        UIUXFeedbackUtils.showLoading();
        TokenRefresher.refreshIfNeeded();
        runAsync(() ->{
            try{
                Object response = serviceCall.get();
                if(response instanceof ApiError error){
                    runLater(()->{
                        handleApiError(error, errorTitle);
                        if(error.status() != 401 && error.status() != 403
                                && error.status() != 500 && error.status() != 502){
                            if(onError != null) onError.accept(error);
                            else UIUXFeedbackUtils.hideLoading();
                        }
                    });
                }else{
                    T data = (T) response;
                    runLater(() -> {onSuccess.accept(data);});
                }
            }catch(Exception e){
                e.printStackTrace();
                runLater(UIUXFeedbackUtils::hideLoading);
            }
        });
    }

    public <T> void executeCall(
            ThrowingSupplier<Object> serviceCall,
            Consumer<T> onSuccess,
            String errorTitle
    ){
        executeCall(serviceCall, onSuccess, null, errorTitle);
    }

    public <T> void executeCall(
            ThrowingSupplier<Object> serviceCall,
            Consumer<T> onSuccess
    ){
        executeCall(serviceCall, onSuccess, null, null);
    }

    protected void runAsync(Runnable task){
        new Thread(task).start();
    }

    protected void runLater(Runnable task){
        Platform.runLater(task);
    }

    public void withDelay(double second, Runnable action){
        PauseTransition delay = new PauseTransition(Duration.seconds(second));
        delay.setOnFinished(event -> action.run());
        delay.play();
    }

    protected void handleApiError(ApiError error,
                                  String title) {
        if (error.details() != null) {
            String message = error.details().stream()
                    .map(detail -> "* " + detail)
                    .collect(Collectors.joining("\n"));
            UIUXFeedbackUtils.showErrorDialog(title, message);
            return;
        }

        String message = error.error() + ": " + error.message();
        UIUXFeedbackUtils.showErrorSnackbar(message);

        if (error.status() == 401 || error.status() == 403 ||
                error.status() == 500 || error.status() == 502) {
            withDelay(4, () -> {
                try {
                    UIUXFeedbackUtils.hideLoading();
                    var service = new AuthService();
                    service.logout();
                    SceneManager.toLogIn();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
