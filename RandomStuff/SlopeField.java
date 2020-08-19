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

public class SlopeField extends Application {

    final int r = 12, steps = 35;
    final double dt = 2;

    double mouseX, mouseY, offsetX = 400, offsetY = 400;
    boolean mouseDown = false, realtime;

    public int fastForward = 1;

    class Point {

        double x, y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    GraphicsContext gc;

    ArrayList<Point> points = new ArrayList<>();

    public void AddPart(double x, double y) {
        points.add(new Point(x, y));
    }

    void DrawPoint(Point p) {
        gc.fillRoundRect(p.x - r / 2 + offsetX, p.y - r / 2 + offsetY, r, r, r, r);
    }

    public void Draw() {

        gc.setStroke(Color.WHITE);

        if (mouseDown) {

        }
        gc.clearRect(0, 0, 800, 800);

        gc.setFill(Color.rgb(255,255,255,1));
        for (Point p : points) {
            DrawPoint(p);
        }
    }

    double function(double x, double y) {
        return -x / y;
    }

    Point slope(Point p) {
        double f = function(p.x, p.y), n = 1;
        if (Double.isFinite(f)) {

        }
        if (f < 0) {
            n = -1;
        }
        double a = Math.atan2(f, n);
        return new Point(Math.cos(a), Math.sin(a));
    }

    @Override
    public void start(Stage primaryStage) {

        Group root = new Group();
        Scene s = new Scene(root, 800, 800, Color.BLACK);

        final Canvas canvas = new Canvas(800, 800);
        canvas.setFocusTraversable(true);
        gc = canvas.getGraphicsContext2D();

        root.getChildren().add(canvas);

        primaryStage.initStyle(StageStyle.UNIFIED);
        primaryStage.setTitle("Particles");
        primaryStage.setScene(s);
        primaryStage.setResizable(false);
        primaryStage.show();

        canvas.setOnMousePressed(e -> {
            mouseDown = true;
            mouseX = e.getX();
            mouseY = e.getY();
            points.add(new Point(mouseX - offsetX, mouseY - offsetY));
        });

        canvas.setOnMouseReleased(e -> {
            mouseDown = false;
        });

        canvas.setOnMouseDragged(e -> {
            if (!e.isShiftDown()) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });

        canvas.setOnKeyPressed(e -> {
            switch(e.getCode()) {
                case R:
                    break;
            }
        });

        new AnimationTimer() {

            @Override
            public void handle(long t) {
            for (Point p : points) {
                Point slp = slope(p);
                p.x += slp.x * dt;
                p.y += slp.y * dt;
            }
            Draw();
            }

        }.start();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
