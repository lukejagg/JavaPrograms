package RandomStuff;

import Generators.Maze;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import javafx.scene.image.WritableImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SortingAlgorithms extends Application {

    GraphicsContext gc;

    static int amount = 50;

    @Override
    public void start(Stage primaryStage) {

        Group root = new Group();
        Scene s = new Scene(root, 800, 800, Color.BLACK);

        final Canvas canvas = new Canvas(800, 800);
        canvas.setFocusTraversable(true);
        gc = canvas.getGraphicsContext2D();

        root.getChildren().add(canvas);

        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("Sort");
        primaryStage.setScene(s);
        primaryStage.setResizable(false);
        primaryStage.show();

        ArrayList<Integer> list = new ArrayList<Integer>();

        for (int i = 0; i < amount; i++)
            list.add(i);

        Random r = new Random();

        for (int i = amount - 1; i >= 0; i--) {
            int n = r.nextInt(amount);
            int in = list.get(i);
            list.set(i, list.get(n));
            list.set(n, in);
        }

        new AnimationTimer() {

            int i = 0;
            int j = 0;

            @Override
            public void handle(long dt) {

                gc.clearRect(0, 0, 800, 800);

                for (int x = 0; x < 1; x++){
                    if (list.get(i) > list.get(++i)) {
                        int lol = list.get(i);
                        list.set(i, list.get(i - 1));
                        list.set(i - 1, lol);
                    }
                    if (i >= amount - 1 - j) {
                        i = 0;
                        j++;
                        if (j >= amount) {
                            this.stop();
                            break;
                        }
                    }
                }

                for (int i = 0; i < amount; i++) {
                    gc.setFill(Color.hsb(list.get(i) * 360.0 / amount,1, 1));
                    double[] x = {400, 400 + Math.cos(Math.toRadians(i * 360.0 / amount)) * 400, 400 + Math.cos(Math.toRadians((i + 1) * 360.0 / amount)) * 400}, y = {400, 400 + Math.sin(Math.toRadians(i * 360.0 / amount)) * 400, 400 + Math.sin(Math.toRadians((i + 1) * 360.0 / amount)) * 400};
                    gc.fillPolygon(x, y, 3);
                }
            }

        }.start();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
