module Main {
    requires java.sql;
    requires java.logging;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires javafx.base;
    requires javafx.graphics;
    requires java.desktop;
    opens Main to javafx.fxml;
    exports Main;
    opens Controller to javafx.fxml;
    exports Controller;
    opens model to javafx.base;
    exports model;
}
