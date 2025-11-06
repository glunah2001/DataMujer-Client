package com.uned.clientedatamujer.controller.view.base;

import com.uned.clientedatamujer.controller.util.UIUXFeedbackUtils;
import com.uned.clientedatamujer.controller.view.scene.MainController;
import com.uned.clientedatamujer.service.util.AuthSession;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Objects;

public abstract class BaseSubSceneController {
    protected MainController mainController;
    protected int currentPage;
    protected Button prev;
    protected Button next;
    protected Button print;
    protected VBox vBox;

    public MainController getMainController() {return mainController;}

    protected void setVBox(VBox vBox) {this.vBox = vBox;}
    protected void setPrev(Button prev) {this.prev = prev;}
    protected void setNext(Button next) {this.next = next;}
    protected void setPrint(Button print) {this.print = print;}
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    protected <T> void setCard(String fxml, T content, BaseSubSceneController parent){
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

    protected <T> void setForm(String fxml, BaseSubSceneController parent, T type){
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

    protected void allowPageableButtons(int currentPage, int totalPages){
        this.currentPage = currentPage;
        if(totalPages == 0){
            next.setVisible(false);
            next.setManaged(false);
            prev.setVisible(false);
            prev.setManaged(false);
            return;
        }

        boolean allowNext = currentPage < totalPages-1;
        boolean allowPrev = currentPage > 0;

        next.setVisible(allowNext);
        next.setManaged(allowNext);
        prev.setVisible(allowPrev);
        prev.setManaged(allowPrev);
    }

    protected void allowPrintButtons(boolean allow){
        if(!Objects.equals(AuthSession.getRole(), "ROLE_ADMIN")) return;
        if(allow && vBox.getChildren().isEmpty()){
            print.setVisible(false);
            print.setManaged(false);
            return;
        }
        print.setVisible(allow);
        print.setManaged(allow);
    }

    protected abstract void refreshCurrentPage();
}
