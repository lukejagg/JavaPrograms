package MazeGen;

import Generators.Maze;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Maze4D extends Application {

    GraphicsContext gc;

    static Color primaryColor = Color.rgb(255, 0, 0);
    static Color secondaryColor = Color.rgb(50, 50, 255);

    static int cellSize = 150;

    static int width = 3, height = 3, depth = 3, time = 1;

    static int currentZ = 1, currentW = 1;

    @Override
    public void start(Stage primaryStage) {

        boolean[][][][] maze = Maze.recursiveBacktracking(width, height, depth, time, 0, 0, 0, 0);

        width = width * 2 + 1;
        height = height * 2 + 1;
        depth = depth * 2 + 1;
        time = time * 2 + 1;

        Group root = new Group();
        Scene s = new Scene(root, width * cellSize, height * cellSize, Color.WHITE);

        final Canvas canvas = new Canvas(width * cellSize, height * cellSize);
        canvas.setFocusTraversable(true);
        gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);

        root.getChildren().add(canvas);

        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("Maze");
        primaryStage.setScene(s);
        primaryStage.setResizable(false);
        primaryStage.show();

        canvas.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case W:
                    currentZ++;
                    if (currentZ >= depth - 1)
                        currentZ = depth - 2;
                    break;
                case S:
                    currentZ--;
                    if (currentZ < 1)
                        currentZ = 1;
                    break;
                case I:
                    currentW++;
                    if (currentW >= time - 1)
                        currentW = time - 2;
                    break;
                case K:
                    currentW--;
                    if (currentW < 1)
                        currentW = 1;
                    break;
            }
        });

        new AnimationTimer() {

            @Override
            public void handle(long dt) {

                System.out.println(currentZ + " " + currentW);

                gc.clearRect(0, 0, width * cellSize, height * cellSize);

                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < width; y++) {
                        if (!maze[x][y][currentZ][currentW])
                            gc.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                    }
                }

            }

        }.start();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
