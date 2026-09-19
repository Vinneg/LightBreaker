package vinneg.lightbreaker;

import java.awt.*;

public class Worker implements Runnable {

    private final Robot robot;
    private final Point health;
    private final Point cast;

    public Worker(int hx, int hy, int cx, int cy) throws AWTException {
        robot = new Robot();
        health = new Point(hx, hy);
        cast = new Point(cx, cy);
    }

    @Override
    public void run() {

    }

}
