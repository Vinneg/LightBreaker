module vinneg.lightbreaker {

    requires javafx.controls;

    requires javafx.fxml;
    requires java.desktop;

    opens vinneg.lightbreaker to javafx.fxml;

    exports vinneg.lightbreaker;
}
