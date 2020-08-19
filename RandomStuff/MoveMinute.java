package RandomStuff;

import java.awt.*;

public class MoveMinute {

    public static void main(String[] args) throws Exception {
        try {
            Robot robot = new Robot();
            while (true) {
                Point p = MouseInfo.getPointerInfo().getLocation();
                robot.mouseMove(p.x, p.y + 1);
                robot.delay(60000);
                p = MouseInfo.getPointerInfo().getLocation();
                robot.mouseMove(p.x, p.y - 1);
                robot.delay(60000);
            }
        }
        catch (Exception e) {

        }
    }

}
