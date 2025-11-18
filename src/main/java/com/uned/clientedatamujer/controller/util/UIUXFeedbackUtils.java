package com.uned.clientedatamujer.controller.util;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXSnackbar;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.Optional;

public class UIUXFeedbackUtils {

    private static StackPane rootPane;
    private static JFXSnackbar snackbar;

    public static StackPane getRootPane() {return rootPane;}

    public static JFXSnackbar getSnackbar() {return snackbar;}

    public static void setRootPane(StackPane rootPane) {UIUXFeedbackUtils.rootPane = rootPane;}

    public static void setSnackbar(JFXSnackbar snackbar) {UIUXFeedbackUtils.snackbar = snackbar;}

    public static void showErrorSnackbar(String message){
        showSnackbar(message, "#D32F2F", "white");
    }

    public static void showSuccessSnackbar(String message){
        showSnackbar(message, "#48B458", "black");
    }

    public static void showLoading(){
        Optional<Node> existingOverlay = rootPane.getChildren().stream()
                .filter(node -> "overlay-pane".equals(node.getId()))
                .findFirst();

        if (existingOverlay.isPresent()) {
            return;
        }

        StackPane overlay = new StackPane();
        overlay.setId("overlay-pane");
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6);");

        ProgressIndicator spinner = new ProgressIndicator();
        spinner.setMaxSize(80, 80);

        overlay.getChildren().add(spinner);
        StackPane.setAlignment(spinner, Pos.CENTER);

        overlay.setPickOnBounds(true);

        Platform.runLater(() -> rootPane.getChildren().add(overlay));
    }

    public static void hideLoading(){
        Platform.runLater(() ->
                rootPane.getChildren().removeIf(node -> "overlay-pane".equals(node.getId()))
        );
    }

    public static void showErrorDialog(String title, String message) {
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

    public static void showTwoStepTermsDialog(
            String title1, String content1,
            String title2, String content2,
            Runnable onAcceptBoth
    ) {
        showTermsDialog(title1, content1, () -> {
            // Segundo diálogo si se aceptó el primero
            showTermsDialog(title2, content2, onAcceptBoth);
        });
    }

    private static void showTermsDialog(String title, String content, Runnable onAccept) {

        VBox container = new VBox(20);
        container.setPadding(new Insets(20));
        container.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setMaxWidth(Double.MAX_VALUE);

        Label text = new Label(content);
        text.setWrapText(true);
        text.setStyle("-fx-font-size: 14px;");

        ScrollPane scroll = new ScrollPane(text);
        scroll.setFitToWidth(true);
        scroll.setPrefHeight(400);

        JFXButton btnAccept = new JFXButton("Aceptar");
        btnAccept.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 8 20; -fx-background-radius: 6px;");

        JFXButton btnCancel = new JFXButton("Cancelar");
        btnCancel.setStyle("-fx-background-color: #D32F2F; -fx-text-fill: white; -fx-padding: 8 20; -fx-background-radius: 6px;");

        HBox buttonBox = new HBox(15, btnAccept, btnCancel);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        container.getChildren().addAll(titleLabel, scroll, buttonBox);

        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setBody(container);

        // El rootPane ya está configurado en initialize()
        JFXDialog dialog = new JFXDialog(rootPane, layout, JFXDialog.DialogTransition.CENTER);
        dialog.setOverlayClose(false); // NO cerrar clickeando afuera

        btnAccept.setOnAction(e -> {
            dialog.close();
            if (onAccept != null) onAccept.run();
        });

        btnCancel.setOnAction(e -> dialog.close());

        dialog.show();
    }

    private static void showSnackbar(String message, String BgColor, String txtColor){
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
}
