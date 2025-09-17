module com.example.globetrotter {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires com.gluonhq.maps; // Gluon map module

    opens com.example.globetrotter.main to javafx.graphics, javafx.fxml, javafx.controls;
    exports com.example.globetrotter.main;
}