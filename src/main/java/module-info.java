module com.uned.clientedatamujer {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.uned.clientedatamujer to javafx.fxml;
    exports com.uned.clientedatamujer;
    exports com.uned.clientedatamujer.controller;
    opens com.uned.clientedatamujer.controller to javafx.fxml;
}