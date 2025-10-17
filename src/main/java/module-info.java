module com.uned.clientedatamujer {
    requires javafx.fxml;
    requires jakarta.validation;
    requires java.net.http;
    requires com.jfoenix;
    requires javafx.controls;

    requires com.auth0.jwt;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.annotation;

    opens com.uned.clientedatamujer to javafx.fxml;
    exports com.uned.clientedatamujer;
    exports com.uned.clientedatamujer.controller;
    exports com.uned.clientedatamujer.enums;
    exports com.uned.clientedatamujer.dto to com.fasterxml.jackson.databind;

    opens com.uned.clientedatamujer.controller to javafx.fxml;
    opens com.uned.clientedatamujer.dto.authentication to com.fasterxml.jackson.databind;
    opens com.uned.clientedatamujer.dto.request to com.fasterxml.jackson.databind;
    opens com.uned.clientedatamujer.dto.response to com.fasterxml.jackson.databind;
    opens com.uned.clientedatamujer.dto.token to com.fasterxml.jackson.databind;
}