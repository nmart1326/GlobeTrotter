module com.example.globetrotter {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.web;

    requires java.sql;
    requires org.slf4j;

    requires java.net.http;
    requires org.json;
    requires java.dotenv;



    requires com.esri.arcgisruntime;


    opens com.example.globetrotter.main to javafx.graphics, javafx.fxml, javafx.controls;
    exports com.example.globetrotter.main;
    exports com.example.globetrotter.view;
    exports com.example.globetrotter.service;
}