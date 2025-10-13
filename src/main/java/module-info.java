module com.uned.clientedatamujer {
    requires javafx.fxml;
    requires jakarta.validation;
    requires java.net.http;
    requires com.jfoenix;
    requires javafx.controls;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.databind;


    opens com.uned.clientedatamujer to javafx.fxml;
    exports com.uned.clientedatamujer;
    exports com.uned.clientedatamujer.controller;
    exports com.uned.clientedatamujer.dto to com.fasterxml.jackson.databind;

    opens com.uned.clientedatamujer.controller to javafx.fxml;
    opens com.uned.clientedatamujer.dto.authentication to com.fasterxml.jackson.databind;
    opens com.uned.clientedatamujer.dto.request to com.fasterxml.jackson.databind;
    opens com.uned.clientedatamujer.dto.response to com.fasterxml.jackson.databind;
    opens com.uned.clientedatamujer.dto.token to com.fasterxml.jackson.databind;
}