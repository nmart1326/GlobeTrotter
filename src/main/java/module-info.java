module com.example.globetrotter {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.globetrotter to javafx.fxml;
    exports com.example.globetrotter;
}