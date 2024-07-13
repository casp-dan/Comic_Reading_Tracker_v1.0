module app {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.google.gson;
    requires java.sql;
    requires javafx.graphics;
    requires javafx.base;

    opens app to javafx.fxml;
    exports app;
    exports model;
    exports app.controllers;
    opens app.controllers to javafx.fxml;
}