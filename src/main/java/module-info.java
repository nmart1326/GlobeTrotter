module org.example.gy {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.gy to javafx.fxml;
    exports org.example.gy;
}