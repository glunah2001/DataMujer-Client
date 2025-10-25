package com.uned.clientedatamujer.controller.util;

import com.jfoenix.controls.JFXToggleButton;
import com.uned.clientedatamujer.enums.Classification;
import com.uned.clientedatamujer.enums.Country;
import com.uned.clientedatamujer.enums.Method;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.function.UnaryOperator;

public class ComponentInitializer {

    public static void initializeCountry(ComboBox<Country> comboCountry){
        comboCountry.getItems().addAll(Arrays.asList(Country.values()));
        comboCountry.setConverter(new StringConverter<>() {
            @Override
            public String toString(Country country) {
                if (country == null) return "";
                String formatted = country.name().toUpperCase().replace("_", " ");
                return Character.toUpperCase(formatted.charAt(0)) + formatted.substring(1);
            }

            @Override
            public Country fromString(String string) {
                if (string == null || string.isEmpty()) return null;
                return Arrays.stream(Country.values())
                        .filter(c -> c.name().replace("_", " ").equalsIgnoreCase(string))
                        .findFirst()
                        .orElse(null);
            }
        });
        comboCountry.setValue(Country.COSTA_RICA);
    }

    public static void initializeMethod(ComboBox<Method> comboMethod) {
        comboMethod.getItems().addAll(Arrays.asList(Method.values()));
        comboMethod.setConverter(new StringConverter<>() {
            @Override
            public String toString(Method method) {
                if (method == null) return "";
                String formatted = method.name().toLowerCase().replace("_", " ");
                return Character.toUpperCase(formatted.charAt(0)) + formatted.substring(1);
            }

            @Override
            public Method fromString(String string) {
                if (string == null || string.isEmpty()) return null;
                return Arrays.stream(Method.values())
                        .filter(m -> m.name().replace("_", " ").equalsIgnoreCase(string))
                        .findFirst()
                        .orElse(null);
            }
        });
        comboMethod.setValue(Method.EFECTIVO);
    }

    public static void initializeClassification(ComboBox<Classification> comboClassification) {
        comboClassification.getItems().addAll(Arrays.asList(Classification.values()));
        comboClassification.setConverter(new StringConverter<>() {
            @Override
            public String toString(Classification classification) {
                if (classification == null) return "";
                String formatted = classification.name().toLowerCase().replace("_", " ");
                return Character.toUpperCase(formatted.charAt(0)) + formatted.substring(1);
            }

            @Override
            public Classification fromString(String string) {
                if (string == null || string.isEmpty()) return null;
                return Arrays.stream(Classification.values())
                        .filter(c -> c.name().replace("_", " ").equalsIgnoreCase(string))
                        .findFirst()
                        .orElse(null);
            }
        });
        comboClassification.setValue(Classification.MENSUALIDAD);
    }

    public static void initializeToggle(JFXToggleButton toggle, String off, String on){
        toggle.setText(off);
        toggle.selectedProperty().addListener((
                observable,
                oldValue,
                newValue) -> {
            if (newValue) {
                toggle.setText(on);
            } else {
                toggle.setText(off);
            }
        });
    }

    public static void initializePhone(TextField txtPhone){
        txtPhone.textProperty().addListener((obs,
                                             oldText,
                                             newText) -> {
            if (!newText.matches("[+\\d\\s]*")) {
                txtPhone.setText(newText.replaceAll("[^+\\d\\s]", ""));
                return;
            }

            if (newText.chars().filter(ch -> ch == '+').count() > 1) {
                txtPhone.setText(oldText);
                return;
            }

            if (newText.length() > 1 && newText.charAt(0) != '+') {
                txtPhone.setText("+" + newText.replaceAll("\\+", ""));
                return;
            }

            if (newText.length() > 17) {
                txtPhone.setText(oldText);
                return;
            }

            if (!newText.isEmpty() && !newText.matches("^\\+[1-9]\\d{0,2}\\s\\d{0,14}$")) {
                if (!newText.matches("^\\+[1-9]?\\d{0,2}\\s?\\d{0,14}$")) {
                    txtPhone.setText(oldText);
                }
            }
        });
    }

    public static void initializePhysicalCedula(TextField txtCedula,
                                                JFXToggleButton toggle){
        initializeCedula(txtCedula, true, toggle);
    }

    public static void initializeLegalCedula(TextField txtCedula){
        initializeCedula(txtCedula, false, null);
    }

    private static void initializeCedula(TextField txtCedula, boolean isPhysicalIssue, JFXToggleButton toggle){
        txtCedula.textProperty().addListener((obs,
                                              oldText,
                                              newText) -> {
            if (!newText.matches("\\d*")) {
                txtCedula.setText(newText.replaceAll("[^\\d]", ""));
                return;
            }

            int maxLength = isPhysicalIssue ? (toggle.isSelected() ? 12 : 9) : 10;

            if (newText.length() > maxLength) {
                txtCedula.setText(oldText);
            }
        });
    }

    public static void initializeTotalAmount(TextField txtTotalAmount) {
        DecimalFormat format = new DecimalFormat("#.##");
        format.setParseBigDecimal(true);

        txtTotalAmount.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d{0,2})?")) {
                txtTotalAmount.setText(oldValue);
            }
        });

        // Cuando pierde el foco, formatea el valor
        txtTotalAmount.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) { // perdió el foco
                try {
                    BigDecimal value = new BigDecimal(txtTotalAmount.getText());
                    if (value.compareTo(BigDecimal.ZERO) <= 0) {
                        txtTotalAmount.setStyle("-fx-border-color: red;");
                    } else {
                        txtTotalAmount.setStyle(null);
                        txtTotalAmount.setText(format.format(value));
                    }
                } catch (NumberFormatException e) {
                    txtTotalAmount.setStyle("-fx-border-color: red;");
                }
            }
        });

        txtTotalAmount.setText("0.00");
    }

    public static void initializeSpinnerHours(Spinner<Integer> spinner){initializeSpinner(spinner, 23);}
    public static void initializeSpinnerMinutes(Spinner<Integer> spinner){initializeSpinner(spinner, 59);}

    private static void initializeSpinner(Spinner<Integer> spinner, int max){
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, max, 0);
        valueFactory.setWrapAround(true);
        spinner.setValueFactory(valueFactory);
    }

    public static void configureLongOnlyTextField(TextField txtID) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.isEmpty()) {
                return change;
            }

            if (!newText.matches("\\d+")) {
                return null;
            }

            try {
                long value = Long.parseLong(newText);
            } catch (NumberFormatException e) {
                return null;
            }

            return change;
        };

        TextFormatter<Long> textFormatter = new TextFormatter<>(filter);
        txtID.setTextFormatter(textFormatter);
    }
}
