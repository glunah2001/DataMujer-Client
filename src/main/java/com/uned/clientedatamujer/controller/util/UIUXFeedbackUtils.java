package com.uned.clientedatamujer.controller.util;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXSnackbar;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class UIUXFeedbackUtils {

    private static StackPane overlayPane;

    private static void showSnackbar(String message, JFXSnackbar snackbar, String BgColor, String txtColor){
        Label text = new Label(message);
        text.setStyle("-fx-text-fill: "+txtColor+"; -fx-font-size: 14px;");

        StackPane container = new StackPane(text);
        container.setStyle("-fx-background-color: "+BgColor+"; "
                + "-fx-padding: 12px 24px; "
                + "-fx-background-radius: 8px;");

        snackbar.enqueue(
                new JFXSnackbar.SnackbarEvent(
                        container,
                        javafx.util.Duration.seconds(3)
                )
        );
    }

    public static void errorSnackbar(String message, JFXSnackbar snackbar){
        showSnackbar(message, snackbar, "#D32F2F", "white");
    }

    public static void successSnackbar(String message, JFXSnackbar snackbar){
        showSnackbar(message, snackbar, "#48B458", "black");
    }

    public static void showLoadingOverlay(StackPane rootPane){
        if(overlayPane != null && rootPane.getChildren().contains(overlayPane))
            return;

        overlayPane = new StackPane();
        overlayPane.setStyle("fx-background-color: rgba(0, 0, 0, 0.6);");

        ProgressIndicator spinner = new ProgressIndicator();
        spinner.setMaxSize(80,80);

        overlayPane.getChildren().add(spinner);
        StackPane.setAlignment(spinner, Pos.CENTER);

        overlayPane.setPickOnBounds(true);

        Platform.runLater(() -> rootPane.getChildren().add(overlayPane));
    }

    public static void hideLoadingOverlay(StackPane rootPane){
        if(overlayPane == null) return;
        Platform.runLater(() -> rootPane.getChildren().remove(overlayPane));
    }

    public static void showErrorDialog(StackPane rootPane, String title, String message) {
        VBox content = new VBox(15);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setPadding(new Insets(20));

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #D32F2F;");

        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333;");

        // Botón de cerrar
        JFXButton closeButton = new JFXButton("Cerrar");
        closeButton.setStyle(
                "-fx-background-color: #D32F2F; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 6px; " +
                        "-fx-padding: 6 18 6 18;"
        );

        HBox buttonContainer = new HBox(closeButton);
        buttonContainer.setAlignment(Pos.CENTER_RIGHT);

        content.getChildren().addAll(titleLabel, messageLabel, buttonContainer);

        // Crear el layout del diálogo
        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setBody(content);

        // Crear el diálogo
        JFXDialog dialog = new JFXDialog(rootPane, layout, JFXDialog.DialogTransition.CENTER);

        // Cerrar el diálogo al presionar el botón
        closeButton.setOnAction(event -> dialog.close());

        dialog.show();
    }

}
