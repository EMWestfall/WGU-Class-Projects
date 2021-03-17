module scheduler {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;
    requires mysql.connector.java;
    opens scheduler to javafx.fxml;
    exports scheduler;
    opens login to javafx.fxml;
    exports login;
    opens alerts to javafx.fxml;
    exports alerts;
    opens appointment to javafx.fxml;
    exports appointment;
    opens customer to javafx.fxml;
    exports customer;
    opens data to javafx.fxml;
    exports data;
    opens mainmenu to javafx.fxml;
    exports mainmenu;
    opens scenecontroller to javafx.fxml;
    exports scenecontroller;
    opens tester to javafx.fxml;
    exports tester;
    opens user to javafx.fxml;
    exports user;
    opens reports to javafx.fxml;
    exports reports;
}