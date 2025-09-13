module com.example.globetrotter {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens com.example.globetrotter.main to javafx.graphics, javafx.fxml;
    exports com.example.globetrotter.main;
}