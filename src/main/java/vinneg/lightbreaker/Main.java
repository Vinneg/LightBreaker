package vinneg.lightbreaker;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.security.NoSuchAlgorithmException;

import static java.awt.event.KeyEvent.VK_ALT;
import static java.awt.event.KeyEvent.VK_TAB;

public class Main extends Application {

    private double dragOffsetX;
    private double dragOffsetY;
    private Robot robot;

    @Override
    public void start(Stage mainStage) throws AWTException {
        Robot robot = new Robot();

        // Заголовок окна
        mainStage.setAlwaysOnTop(true);
        mainStage.setTitle("Li-Br");
        mainStage.initStyle(StageStyle.UTILITY);
        mainStage.setX(440);
        mainStage.setY(90);

        // Создаём контейнер для элементов (вертикальная компоновка)
        VBox root = new VBox(10); // 10 — отступ между элементами
        root.setStyle("-fx-padding: 5; -fx-background-color: #f0f0f0;");

        Pane aimRoot = new Pane();
        aimRoot.setStyle("-fx-border-color: black; -fx-border-width: 1;");
        Circle dot1 = new Circle(13, 13, 3, javafx.scene.paint.Color.BLACK);
        Circle dot2 = new Circle(14, 41, 3, Color.BLACK);
        aimRoot.getChildren().addAll(dot1, dot2);

        // Дополнительное окно (создаём заранее, но не показываем)
        Stage aimStage = new Stage(StageStyle.UNDECORATED);
        aimStage.setTitle("Aim");
        aimStage.setScene(new Scene(aimRoot, 27, 54));
        aimStage.setAlwaysOnTop(true);
        aimStage.setX(400);
        aimStage.setY(120);
        aimStage.setOpacity(0.3);

        aimRoot.setOnMousePressed(e -> {
            dragOffsetX = e.getScreenX() - aimStage.getX();
            dragOffsetY = e.getScreenY() - aimStage.getY();
        });
        aimRoot.setOnMouseDragged(e -> {
            aimStage.setX(e.getScreenX() - dragOffsetX);
            aimStage.setY(e.getScreenY() - dragOffsetY);
        });

        ToggleButton startButton = new ToggleButton("START");
        startButton.setPrefSize(60, 40);
        startButton.setOnAction(event -> {
            if (startButton.isSelected()) {
                // При первом нажатии — открываем окно
                startButton.setText("STOP");

                int hx = (int) aimStage.getX() + 13;
                int hy = (int) aimStage.getY() + 13;
                int cx = (int) aimStage.getX() + 13;
                int cy = (int) aimStage.getY() + 41;

                System.out.println("health " + hx + "-" + hy + " cast " + cx + "-" + cy);

                try {
                    Worker.start(hx, hy, cx, cy);
                } catch (NoSuchAlgorithmException ignore) {
                }

                robot.keyPress(VK_ALT);
                robot.keyPress(VK_TAB);
                robot.keyRelease(VK_TAB);
                robot.keyRelease(VK_ALT);
            } else {
                startButton.setText("START");

                Worker.stop();

                robot.keyPress(VK_ALT);
                robot.keyPress(VK_TAB);
                robot.keyRelease(VK_TAB);
                robot.keyRelease(VK_ALT);
            }
        });

        mainStage.setOnCloseRequest(e -> {
            aimStage.close();
            Worker.stop();
        });

        // Создаём toggle‑кнопку для управления дополнительным окном
        ToggleButton aimButton = new ToggleButton("AIM");
        aimButton.setPrefSize(60, 30);
        aimButton.setOnAction(event -> {
            if (aimButton.isSelected()) {
                aimStage.show();
            } else {
                aimStage.hide();
            }
        });

        aimStage.setOnHidden(e -> aimButton.setSelected(false));

        // Добавляем кнопки в контейнер
        root.getChildren().addAll(startButton, aimButton);

        // Создаём сцену с контейнером
        Scene scene = new Scene(root);

        // Устанавливаем сцену в окно
        mainStage.setScene(scene);

        // Показываем окно
        mainStage.show();
    }

    static void main(String[] args) {
        launch(args);
    }

}
