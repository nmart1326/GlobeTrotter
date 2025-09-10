module org.example.gy {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.gy to javafx.fxml;
    exports org.example.gy;
}