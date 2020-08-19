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

public class CircleOverlays extends Application {

    class Circle {

        double x, y, r;

        public Circle(double x, double y, double r) {
            this.x = x;
            this.y = y;
            this.r = r;
        }

        public boolean inCircle (int x, int y) {
            if (Math.hypot(x-this.x, y-this.y) <= r)
                return true;
            return false;
        }

    }

    GraphicsContext gc;

    ArrayList<Circle> circles = new ArrayList<Circle>();

    public void makeCircle(double x, double y, double r) {
        circles.add(new Circle(x, y, r));
    }

    public void draw() {
        gc.clearRect(0, 0, 800, 800);
        gc.setFill(Color.rgb(1,1,1,0.5));
        for (int x = 0; x < 800; x++) {
            for (int y = 0; y < 800; y++) {
                for (Circle c : circles) {
                    if (c.inCircle(x, y))
                        gc.fillRect(x, y, 1, 1);
                }
            }
        }
    }

    @Override
    public void start(Stage primaryStage) {

        for (int i = 0; i < 3; i++)
            makeCircle(Math.cos(i * Math.PI * 2 / 3) * 100 + 400, Math.sin(i * Math.PI * 2 / 3) * 100 + 400, 200);

        Group root = new Group();
        Scene s = new Scene(root, 800, 800, Color.WHITE);

        final Canvas canvas = new Canvas(800, 800);
        canvas.setFocusTraversable(true);
        gc = canvas.getGraphicsContext2D();

        root.getChildren().add(canvas);

        primaryStage.initStyle(StageStyle.UNIFIED);
        primaryStage.setTitle("Circles");
        primaryStage.setScene(s);
        primaryStage.setResizable(false);
        primaryStage.show();

        canvas.setOnMousePressed(e -> {

        });

        canvas.setOnMouseReleased(e -> {

        });

        canvas.setOnMouseDragged(e -> {

        });

        canvas.setOnKeyPressed(e -> {
            switch(e.getCode()) {
                case SPACE:
                    draw();
                    break;
            }
        });

        draw();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
