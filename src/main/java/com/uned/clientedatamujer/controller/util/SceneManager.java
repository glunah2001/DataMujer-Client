package com.uned.clientedatamujer.controller.util;

import com.uned.clientedatamujer.controller.view.base.BaseSubSceneController;
import com.uned.clientedatamujer.controller.view.scene.MainController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SceneManager {

    private static Stage stage;

    public static void setStage(Stage st){
        stage = st;
        stage.getIcons().add(new Image(Objects.requireNonNull(
                SceneManager.class.getResourceAsStream("/com/uned/clientedatamujer/images/DataMujer.jpg"))
        ));
        stage.setTitle("Cliente Data Mujer");
    }

    public static void changeScene(String fxml, int width, int height, boolean isResizable) throws IOException{
        FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxml));
        Scene scene = new Scene(loader.load(), width, height);
        stage.setResizable(isResizable);
        stage.setScene(scene);
        stage.show();
    }

    public static void toLogIn() throws IOException {
        changeScene("/com/uned/clientedatamujer/views/scene/login-view.fxml", 1080, 720, false);
    }

    public static void toMyProfile(int width, int height) throws IOException {
        changeScene("/com/uned/clientedatamujer/views/scene/my-profile-view.fxml", width, height, true);
    }

    public static void toMainView(int width, int height) throws IOException{
        changeScene("/com/uned/clientedatamujer/views/scene/main-view.fxml", width, height, true);
    }

    public static void loadSubScene(StackPane subScenePane,
                                    String fxml,
                                    MainController mainController) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxml));
        Parent root = loader.load();

        subScenePane.getChildren().clear();

        BaseSubSceneController controller = loader.getController();
        controller.setMainController(mainController);

        subScenePane.getChildren().add(root);
    }

}
