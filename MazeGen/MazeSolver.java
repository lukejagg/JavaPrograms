package MazeGen;

import Generators.Maze;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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

public class MazeSolver extends Application {

    GraphicsContext gc;

    static Color primaryColor = Color.rgb(255, 0, 0);
    static Color secondaryColor = Color.rgb(50, 50, 255);

    static boolean perPixel = true;
    static int width = 1000;
    static int height = 1000;
    static int cellSize = 1;
    static double wallWidth = 1;
    static double solutionWidth = 0;

//    static ArrayList<Character> dir = new ArrayList<Character>();

    @Override
    public void start(Stage primaryStage) {

        boolean[][] maze = Maze.recursiveBacktracking(width, height, 0, 0);

        if (perPixel) {
            width = width * 2 + 1;
            height = height * 2 + 1;
        }

        boolean[][] cells = new boolean[height][width];

        Group root = new Group();
        Scene s = new Scene(root, width * cellSize, height * cellSize, Color.BLACK);

        final Canvas canvas = new Canvas(width * cellSize, height * cellSize);
        canvas.setFocusTraversable(true);
        gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);

        root.getChildren().add(canvas);

        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("Maze");
        primaryStage.setScene(s);
        primaryStage.setResizable(false);
        primaryStage.show();

        if (perPixel)
            canvas.setOnKeyPressed(e -> {
                switch (e.getCode()) {
                    case SPACE:
                        WritableImage wim = new WritableImage(width * cellSize, height * cellSize);
                        canvas.snapshot(null, wim);

                        File file = new File("maze_solution2.png");

                        try {
                            ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
                            System.exit(0);
                        } catch (IOException exc) {
                            exc.printStackTrace();
                        }
                        break;
                }
            });

        if (perPixel) {
            for (int i = 0; i < maze.length; i++) {
                for (int j = 0; j < maze[0].length; j++) {
                    gc.setFill(Color.WHITE);
                    if (maze[i][j]) {
                        gc.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
                    }
                }
            }
        } else {
            for (int i = 1; i < maze.length - 1; i++) {
                for (int j = 1; j < maze[0].length - 1; j++) {
                    gc.setFill(Color.WHITE);
                    if (i % 2 == 1 && j % 2 == 1) {
                        gc.fillRect((i - 1) / 2 * cellSize + wallWidth, (j - 1) / 2 * cellSize + wallWidth, cellSize - wallWidth * 2, cellSize - wallWidth * 2);
                        if (maze[i + 1][j]) {
                            gc.fillRect((i - 1) / 2 * cellSize + wallWidth, (j - 1) / 2 * cellSize + wallWidth, cellSize, cellSize - wallWidth * 2);
                        }
                        if (maze[i][j + 1]) {
                            gc.fillRect((i - 1) / 2 * cellSize + wallWidth, (j - 1) / 2 * cellSize + wallWidth, cellSize - wallWidth * 2, cellSize);
                        }
                    }

                }
            }
        }

        if (perPixel) {
            new AnimationTimer() {

                ArrayList<Character> dir = new ArrayList<Character>();
                int currentX = 1;
                int currentY = 1;
                ArrayList<Character> choices = new ArrayList<Character>();
                int time = 0;

                @Override
                public void handle(long dt) {

                    if (time++ % 1 != 0)
                        return;

                    for (int n = 0; n < 50; n++) {

                        choices.clear();
                        if (currentX + 1 < width && maze[currentX + 1][currentY] == true && (dir.size() == 0 || dir.get(dir.size() - 1) != 'a') && cells[(currentX + 1) / 2][(currentY - 1) / 2] == false) {
                            choices.add('d');
                        } else if (currentY + 1 < height && maze[currentX][currentY + 1] == true && (dir.size() == 0 || dir.get(dir.size() - 1) != 's') && cells[(currentX - 1) / 2][(currentY + 1) / 2] == false) {
                            choices.add('w');
                        } else if (currentX - 1 > 0 && maze[currentX - 1][currentY] == true && (dir.size() == 0 || dir.get(dir.size() - 1) != 'd') && cells[(currentX - 3) / 2][(currentY - 1) / 2] == false) {
                            choices.add('a');
                        } else if (currentY - 1 > 0 && maze[currentX][currentY - 1] == true && (dir.size() == 0 || dir.get(dir.size() - 1) != 'w') && cells[(currentX - 1) / 2][(currentY - 3) / 2] == false) {
                            choices.add('s');
                        }
                        int cx = currentX * cellSize;
                        int cy = currentY * cellSize;
                        if (choices.size() > 0) {
                            gc.setFill(primaryColor);
                            gc.fillRect(cx, cy, cellSize, cellSize);
                            switch (choices.get(0)) {
                                case 'd':
                                    gc.fillRect(cx + cellSize, cy, cellSize, cellSize);
                                    currentX += 2;
                                    break;
                                case 'a':
                                    gc.fillRect(cx - cellSize, cy, cellSize, cellSize);
                                    currentX -= 2;
                                    break;
                                case 'w':
                                    gc.fillRect(cx, cy + cellSize, cellSize, cellSize);
                                    currentY += 2;
                                    break;
                                case 's':
                                    gc.fillRect(cx, cy - cellSize, cellSize, cellSize);
                                    currentY -= 2;
                                    break;
                            }
                            dir.add(choices.get(0));
                        } else {
                            gc.setFill(secondaryColor);
                            gc.fillRect(cx, cy, cellSize, cellSize);
                            cells[(currentX - 1) / 2][(currentY - 1) / 2] = true;
                            switch (dir.get(dir.size() - 1)) {
                                case 'd':
                                    gc.fillRect(cx - cellSize, cy, cellSize, cellSize);
                                    currentX -= 2;
                                    break;
                                case 'a':
                                    gc.fillRect(cx + cellSize, cy, cellSize, cellSize);
                                    currentX += 2;
                                    break;
                                case 'w':
                                    gc.fillRect(cx, cy - cellSize, cellSize, cellSize);
                                    currentY -= 2;
                                    break;
                                case 's':
                                    gc.fillRect(cx, cy + cellSize, cellSize, cellSize);
                                    currentY += 2;
                                    break;
                            }
                            dir.remove(dir.size() - 1);
                        }
                        if (dir.size() == 0 || (currentX == width-2 && currentY == height-2)) {
                            gc.fillRect(currentX * cellSize, currentY * cellSize, cellSize, cellSize);
                            this.stop();
                            return;
                        }

                    }
                }

            }.start();
        } else {
            new AnimationTimer() {

                ArrayList<Character> dir = new ArrayList<Character>();
                int currentX = 1;
                int currentY = 1;
                ArrayList<Character> choices = new ArrayList<Character>();
                int time = 0;
                int totalTimes = 0;

                @Override
                public void handle(long dt) {

                    if (time++ % 1 != 0)
                        return;

                    for (int n = 0; n < 10000; n++) {

                        choices.clear();
                        if (currentX + 1 < width * 2 + 1 && maze[currentX + 1][currentY] == true && (dir.size() == 0 || dir.get(dir.size() - 1) != 'a') && cells[(currentX + 1) / 2][(currentY - 1) / 2] == false) {
                            choices.add('d');
                        } else if (currentY + 1 < height * 2 + 1 && maze[currentX][currentY + 1] == true && (dir.size() == 0 || dir.get(dir.size() - 1) != 's') && cells[(currentX - 1) / 2][(currentY + 1) / 2] == false) {
                            choices.add('w');
                        } else if (currentX - 1 > 0 && maze[currentX - 1][currentY] == true && (dir.size() == 0 || dir.get(dir.size() - 1) != 'd') && cells[(currentX - 3) / 2][(currentY - 1) / 2] == false) {
                            choices.add('a');
                        } else if (currentY - 1 > 0 && maze[currentX][currentY - 1] == true && (dir.size() == 0 || dir.get(dir.size() - 1) != 'w') && cells[(currentX - 1) / 2][(currentY - 3) / 2] == false) {
                            choices.add('s');
                        }
                        int cx = (currentX - 1) / 2 * cellSize;
                        int cy = (currentY - 1) / 2 * cellSize;
                        if (choices.size() > 0) {
                            gc.setFill(primaryColor);
                            gc.fillRect(cx + wallWidth + solutionWidth, cy + wallWidth + solutionWidth, cellSize - wallWidth * 2 - solutionWidth * 2, cellSize - wallWidth * 2 - solutionWidth * 2);
                            switch (choices.get(0)) {
                                case 'd':
                                    gc.fillRect(cx + cellSize - wallWidth - solutionWidth, cy + wallWidth + solutionWidth, 2 * solutionWidth + 2 * wallWidth, cellSize - wallWidth * 2 - solutionWidth * 2);
                                    currentX += 2;
                                    break;
                                case 'a':
                                    gc.fillRect(cx - wallWidth - solutionWidth, cy + wallWidth + solutionWidth, 2 * solutionWidth + 2 * wallWidth, cellSize - wallWidth * 2 - solutionWidth * 2);
                                    currentX -= 2;
                                    break;
                                case 'w':
                                    gc.fillRect(cx + wallWidth + solutionWidth, cy + cellSize - wallWidth - solutionWidth, cellSize - wallWidth * 2 - solutionWidth * 2, 2 * solutionWidth + 2 * wallWidth);
                                    currentY += 2;
                                    break;
                                case 's':
                                    gc.fillRect(cx + wallWidth + solutionWidth, cy - wallWidth - solutionWidth, cellSize - wallWidth * 2 - solutionWidth * 2, 2 * solutionWidth + 2 * wallWidth);
                                    currentY -= 2;
                                    break;
                            }
                            dir.add(choices.get(0));
                        } else {
                            if (dir.size() > 0) {
                                gc.setFill(secondaryColor);
                                gc.fillRect(cx + wallWidth + solutionWidth, cy + wallWidth + solutionWidth, cellSize - wallWidth * 2 - solutionWidth * 2, cellSize - wallWidth * 2 - solutionWidth * 2);
                                cells[(currentX - 1) / 2][(currentY - 1) / 2] = true;
                                switch (dir.get(dir.size() - 1)) {
                                    case 'd':
                                        gc.fillRect(cx - wallWidth - solutionWidth, cy + wallWidth + solutionWidth, 2 * solutionWidth + 2 * wallWidth, cellSize - wallWidth * 2 - solutionWidth * 2);
                                        currentX -= 2;
                                        break;
                                    case 'a':
                                        gc.fillRect(cx + cellSize - wallWidth - solutionWidth, cy + wallWidth + solutionWidth, 2 * solutionWidth + 2 * wallWidth, cellSize - wallWidth * 2 - solutionWidth * 2);
                                        currentX += 2;
                                        break;
                                    case 'w':
                                        gc.fillRect(cx + wallWidth + solutionWidth, cy - wallWidth - solutionWidth, cellSize - wallWidth * 2 - solutionWidth * 2, 2 * solutionWidth + 2 * wallWidth);
                                        currentY -= 2;
                                        break;
                                    case 's':
                                        gc.fillRect(cx + wallWidth + solutionWidth, cy + cellSize - wallWidth - solutionWidth, cellSize - wallWidth * 2 - solutionWidth * 2, 2 * solutionWidth + 2 * wallWidth);
                                        currentY += 2;
                                        break;
                                }
                                dir.remove(dir.size() - 1);
                            }
                            else if (++totalTimes > 4) {
                                this.stop();
                                return;
                            }
                        }

                        if (currentX == width * 2 - 1 && currentY == height * 2 - 1) {
                            this.stop();
                            return;
                        }

                    }
                }

            }.start();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }

}
