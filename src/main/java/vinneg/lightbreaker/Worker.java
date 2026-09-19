package vinneg.lightbreaker;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static java.awt.event.KeyEvent.VK_ESCAPE;

public class Worker implements Runnable {

    private static Thread main;

    private final Robot robot;
    private final Point hp;
    private final Point cp;

    public Worker(int hx, int hy, int cx, int cy) throws AWTException {
        robot = new Robot();
        hp = new Point(hx, hy);
        cp = new Point(cx, cy);
    }

    @Override
    public void run() {
        long t = 0;

        while (!Thread.currentThread().isInterrupted()) {
//            robot.delay(5);

            Color hc = robot.getPixelColor(hp.x, hp.y);
            Color cc = robot.getPixelColor(cp.x, cp.y);

            if (hc.getRed() < 200 && cc.getRed() > 200) {
                System.out.println(LocalDateTime.now() + ", health = " + hc.getRed() + ", cast = " + cc.getRed());

                robot.keyPress(VK_ESCAPE);
                robot.delay(ThreadLocalRandom.current().nextInt(2, 5));
                robot.keyRelease(VK_ESCAPE);
            }
        }
    }

    public static void start(int hx, int hy, int cx, int cy) throws NoSuchAlgorithmException {
        try {
            if (main == null) {
                Worker worker = new Worker(hx, hy, cx, cy);

                main = new Thread(worker, UUID.randomUUID().toString());
            }

            main.start();
        } catch (Exception e) {
            System.out.println("Fatal error: " + e);
        }
    }

    public static void stop() {
        if (main == null) {
            return;
        }

        try {
            main.interrupt();
            main.join(5 * 1_000); // ждём до 5 сек
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        } finally {
            main = null;
        }
    }

}
