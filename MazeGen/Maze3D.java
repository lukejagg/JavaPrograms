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

public class Maze3D extends Application {

    GraphicsContext gc;

    static Color primaryColor = Color.rgb(255, 0, 0);
    static Color secondaryColor = Color.rgb(50, 50, 255);

    static int cellSize = 40;

    static int width = 3, height = 3, depth = 3;

    static int currentZ = 1;

    @Override
    public void start(Stage primaryStage) {

        boolean[][][] maze = Maze.recursiveBacktracking(width, height, depth, 0, 0, 0);

        width = width * 2 + 1;
        height = height * 2 + 1;
        depth = depth * 2 + 1;

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
            }
        });

        new AnimationTimer() {

            @Override
            public void handle(long dt) {

                gc.clearRect(0, 0, width * cellSize, height * cellSize);

                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < width; y++) {
                        if (!maze[x][y][currentZ])
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
