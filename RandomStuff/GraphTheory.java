package RandomStuff;

import Generators.Maze;

import java.util.Random;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.xml.soap.Text;

import javafx.geometry.VPos;
import javafx.scene.image.WritableImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GraphTheory extends Application {

    GraphicsContext gc;
    static Random r = new Random();
    static int move = -1;
    static int size = 25;
    static int cir = 3;

    class Point {
        double x, y;
        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    ArrayList<Point> points = new ArrayList<Point>();

    public boolean makeConnection(int num1, int num2) {
        int n = 1;
        while (n*n <= num1 + num2) {
            if (n*n == num1 + num2) {
                return true;
            }
            n++;
        }
        return false;
    }

    public void draw() {
        gc.clearRect(0, 0, 1200, 800);
        int c = 0;
        int[] connections = new int[points.size()];
        for (int i = 0; i < points.size(); i++) {
            for (int j = 0; j < i; j++) {
                int n1 = i + 1, n2 = j+1;
                if (makeConnection(n1, n2)) {
                    gc.setStroke(Color.hsb(c, 1, 1));
                    c += 6;
                    connections[i]++;
                    connections[j]++;
                    gc.strokeLine(points.get(i).x, points.get(i).y, points.get(j).x, points.get(j).y);
                }
            }
        }
        for (int i = 0; i < points.size(); i++) {
            gc.setFill(Color.WHITE);
            gc.fillRoundRect(points.get(i).x - size - cir, points.get(i).y - size - cir, 2 * size + cir * 2, 2 * size + cir * 2, 2 * size + cir * 2, 2 * size + cir * 2);
            gc.setFill(Color.BLACK);
            gc.fillRoundRect(points.get(i).x - size, points.get(i).y - size, 2 * size, 2 * size, 2 * size, 2 * size);
            if (connections[i] == 0)
                gc.setFill(Color.RED);
            else if (connections[i] == 1)
                gc.setFill(Color.YELLOW);
            else
                gc.setFill(Color.WHITE);
            gc.fillText(i + 1 + "", points.get(i).x, points.get(i).y);
        }
    }

    public int getConnections(int index) {
        int c = 0;
        for (int i = 0; i < points.size(); i++) {
            for (int j = 0; j < points.size(); j++) {
                if (i == j)
                    continue;
                int n1 = i + 1, n2 = j+1;
                if (makeConnection(n1, n2)) {
                    c++;
                }
            }
        }
        return c;
    }

    public void findPath(int startIndex) {
        gc.clearRect(0, 0, 1200, 800);
        int c = 0;
        int[] connections = new int[points.size()];
        for (int i = 0; i < points.size(); i++) {
            for (int j = 0; j < i; j++) {
                int n1 = i + 1, n2 = j+1;
                if (makeConnection(n1, n2)) {
                    gc.setStroke(Color.hsb(c, 1, 1));
                    c += 6;
                    connections[i]++;
                    connections[j]++;
                    gc.strokeLine(points.get(i).x, points.get(i).y, points.get(j).x, points.get(j).y);
                }
            }
        }

        /**
         * The coolest path finding thing I made using recursion!
         */

        ArrayList<Integer> rPath = new ArrayList<Integer>();
        ArrayList<Integer[]> paths = new ArrayList<Integer[]>();
        while (true) {
            if (rPath.size() > 0) {

            }
            else {
                int n = getConnections(startIndex);
                if (n > 0) {
//                    rPath.add()
                }
            }
            break;
        }


        for (int i = 0; i < points.size(); i++) {
            gc.setFill(Color.WHITE);
            gc.fillRoundRect(points.get(i).x - size - cir, points.get(i).y - size - cir, 2 * size + cir * 2, 2 * size + cir * 2, 2 * size + cir * 2, 2 * size + cir * 2);
            gc.setFill(Color.BLACK);
            gc.fillRoundRect(points.get(i).x - size, points.get(i).y - size, 2 * size, 2 * size, 2 * size, 2 * size);
            if (connections[i] == 0)
                gc.setFill(Color.RED);
            else if (connections[i] == 1)
                gc.setFill(Color.YELLOW);
            else
                gc.setFill(Color.WHITE);
            gc.fillText(i + 1 + "", points.get(i).x, points.get(i).y);
        }
    }

    @Override
    public void start(Stage primaryStage) {

        Group root = new Group();
        Scene s = new Scene(root, 1200, 800, Color.BLACK);

        final Canvas canvas = new Canvas(1200, 800);
        canvas.setFocusTraversable(true);
        gc = canvas.getGraphicsContext2D();
        gc.setTextBaseline(VPos.CENTER);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(new Font(size));

        root.getChildren().add(canvas);

        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("Graph Theory");
        primaryStage.setScene(s);
        primaryStage.setResizable(false);
        primaryStage.show();

        canvas.setOnMousePressed(e -> {
            if (e.isShiftDown()) {
                points.add(new Point(e.getX(), e.getY()));
            }
            else if (e.isControlDown()) {
                int move = -1;
                double closest = size;
                for (int i = 0; i < points.size(); i++) {
                    if (Math.hypot(points.get(i).x - e.getX(), points.get(i).y - e.getY()) < closest) {
                        move = i;
                        closest = Math.hypot(points.get(i).x - e.getX(), points.get(i).y - e.getY());
                    }
                }
                if (move > -1) {
                    points.remove(move);
                }
            }
            else if (e.isAltDown()) {
                int move = -1;
                double closest = size;
                for (int i = 0; i < points.size(); i++) {
                    if (Math.hypot(points.get(i).x - e.getX(), points.get(i).y - e.getY()) < closest) {
                        move = i;
                        closest = Math.hypot(points.get(i).x - e.getX(), points.get(i).y - e.getY());
                    }
                }
                if (move > -1) {
                    findPath(move);
                }
            }
            else {
                move = -1;
                double closest = size;
                for (int i = 0; i < points.size(); i++) {
                    if (Math.hypot(points.get(i).x - e.getX(), points.get(i).y - e.getY()) < closest) {
                        move = i;
                        closest = Math.hypot(points.get(i).x - e.getX(), points.get(i).y - e.getY());
                    }
                }
            }
            draw();
        });

        canvas.setOnMouseReleased(e -> {
            move = -1;
        });

        canvas.setOnMouseDragged(e -> {
            if (move > -1) {
                points.get(move).x = e.getX();
                points.get(move).y = e.getY();
            }
            draw();
        });

        canvas.setOnKeyPressed(e -> {
            switch(e.getCode()) {
                case SPACE:
                    points.clear();
                    draw();
                    break;
                case R:
                    for (Point p : points) {
                        p.x = r.nextDouble() * (1200 - size * 2) + size;
                        p.y = r.nextDouble() * (800 - size * 2) + size;
                    }
                    draw();
                    break;
            }
        });

    }

    public static void main(String[] args) {
        launch(args);
    }

}
