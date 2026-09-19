package vinneg.lightbreaker;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Controller {

    @FXML
    private ToggleButton startToggle;
    @FXML
    private ToggleButton aimToggle;

    private Stage aimStage;
    private double dragOffsetX;
    private double dragOffsetY;

    @FXML
    protected void onStartToggleAction() {
        if (startToggle.isSelected()) {
            startToggle.setText("STOP");
        } else {
            startToggle.setText("START");
        }
    }

    @FXML
    protected void onAimToggleAction() {
        if (aimToggle.isSelected()) {
            showAim();
        } else {
            hideAim();
        }
    }

    private void showAim() {
        if (aimStage == null) {
            Pane root = new Pane();
            root.setStyle("-fx-border-color: black; -fx-border-width: 1;");
            Circle dot1 = new Circle(13, 13, 3, Color.BLACK);
            Circle dot2 = new Circle(14, 41, 3, Color.BLACK);
            root.getChildren().addAll(dot1, dot2);

            aimStage = new Stage(StageStyle.UNDECORATED);
            aimStage.setTitle("Aim");
            aimStage.setScene(new Scene(root, 27, 54));
            aimStage.setAlwaysOnTop(true);
            aimStage.setOpacity(0.3);

            root.setOnMousePressed(e -> {
                dragOffsetX = e.getScreenX() - aimStage.getX();
                dragOffsetY = e.getScreenY() - aimStage.getY();
            });
            root.setOnMouseDragged(e -> {
                aimStage.setX(e.getScreenX() - dragOffsetX);
                aimStage.setY(e.getScreenY() - dragOffsetY);
            });

            aimStage.setOnHidden(e -> aimToggle.setSelected(false));
        }

        aimStage.show();
    }

    private void hideAim() {
        if (aimStage != null) {
            aimStage.hide();
        }
    }

}
