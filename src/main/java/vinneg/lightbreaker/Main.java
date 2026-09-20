package vinneg.lightbreaker;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.security.NoSuchAlgorithmException;

import static java.awt.event.KeyEvent.VK_ALT;
import static java.awt.event.KeyEvent.VK_TAB;

public class Main extends Application {

    private double dragOffsetX;
    private double dragOffsetY;
    private Robot robot;

    @Override
    public void start(Stage main) throws AWTException {
        Robot robot = new Robot();

        Pane aimRoot = new Pane();
        aimRoot.setStyle("-fx-border-color: black; -fx-border-width: 1;");
        Circle dot1 = new Circle(13, 13, 3, javafx.scene.paint.Color.BLACK);
        Circle dot2 = new Circle(14, 41, 3, Color.BLACK);
        aimRoot.getChildren().addAll(dot1, dot2);

        Stage slave = new Stage(StageStyle.UNDECORATED);
        slave.setTitle("Aim");
        slave.setScene(new Scene(aimRoot, 27, 54));
        slave.setAlwaysOnTop(true);
        slave.setX(400);
        slave.setY(120);
        slave.setOpacity(0.3);

        aimRoot.setOnMousePressed(e -> {
            dragOffsetX = e.getScreenX() - slave.getX();
            dragOffsetY = e.getScreenY() - slave.getY();
        });
        aimRoot.setOnMouseDragged(e -> {
            slave.setX(e.getScreenX() - dragOffsetX);
            slave.setY(e.getScreenY() - dragOffsetY);
        });

        VBox root = new VBox(10);

        javafx.scene.control.Label title = new Label("Li-Br");
        title.setPrefSize(80, 20);

        ToggleButton start = new ToggleButton("START");
        start.setPrefSize(80, 40);
        start.setOnAction(_ -> {
            if (start.isSelected()) {
                start.setText("STOP");

                int hx = (int) slave.getX() + 13;
                int hy = (int) slave.getY() + 13;
                int cx = (int) slave.getX() + 13;
                int cy = (int) slave.getY() + 41;

                try {
                    Worker.start(hx, hy, cx, cy);
                } catch (NoSuchAlgorithmException ignore) {
                }

                robot.keyPress(VK_ALT);
                robot.keyPress(VK_TAB);
                robot.keyRelease(VK_TAB);
                robot.keyRelease(VK_ALT);
            } else {
                start.setText("START");

                Worker.stop();

                robot.keyPress(VK_ALT);
                robot.keyPress(VK_TAB);
                robot.keyRelease(VK_TAB);
                robot.keyRelease(VK_ALT);
            }
        });

        ToggleButton aim = new ToggleButton("AIM");
        aim.setPrefSize(80, 20);
        aim.setOnAction(_ -> {
            if (aim.isSelected()) {
                slave.show();
            } else {
                slave.hide();
            }
        });

        Button close = new Button("close");
        close.setPrefSize(80, 20);
        close.setOnAction(_ -> {
            slave.close();
            main.close();
            Worker.stop();
        });

        slave.setOnHidden(_ -> aim.setSelected(false));

        root.getChildren().addAll(title, start, aim, close);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        main.initStyle(StageStyle.UNDECORATED);
        main.setAlwaysOnTop(true);
        main.setResizable(false);
        main.setX(440);
        main.setY(90);
        main.setScene(scene);

        main.setOnCloseRequest(_ -> {
            slave.close();
            Worker.stop();
        });

        main.show();
    }

    static void main(String[] args) {
        launch(args);
    }

}
