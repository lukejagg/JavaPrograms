package RandomStuff;

import Generators.Maze;

import java.util.Random;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.sun.xml.internal.rngom.parse.xml.DtdContext;
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

enum lol {
    x,
    y,
}

public class Snake extends Application {



    GraphicsContext gc;

    static double delta = 0.1;
    static int cellSize = 30;
    static int width = 15, height = 15;
    static Random rnd = new Random();
    static boolean AI = false;

    class Point {
        int x, y;
        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    ArrayList<Point> snake = new ArrayList<Point>();
    int pX = 0, pY = 0, fX = 0, fY = 0;
    char dir = 'd';
    int score = 0;
    ArrayList<Point> possible = new ArrayList<Point>();
    boolean[][] possibleT = new boolean[width][height];
    boolean speed = false;

    @Override
    public void start(Stage primaryStage) {

        Group root = new Group();
        Scene s = new Scene(root, width * cellSize, height * cellSize, Color.BLACK);

        final Canvas canvas = new Canvas(width * cellSize, height * cellSize);
        canvas.setFocusTraversable(true);
        gc = canvas.getGraphicsContext2D();

        root.getChildren().add(canvas);

        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("Snake");
        primaryStage.setScene(s);
        primaryStage.setResizable(false);
        primaryStage.show();

        canvas.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case W:
                    if (dir != 's')
                        dir = 'w';
                    break;
                case S:
                    if (dir != 'w')
                        dir = 's';
                    break;
                case D:
                    if (dir != 'a')
                        dir = 'd';
                    break;
                case A:
                    if (dir != 'd')
                        dir = 'a';
                    break;
                case SPACE:
                    speed = !speed;
                    break;
            }
        });

        new AnimationTimer() {

            long t = 0;
            double ct = 0;

            @Override
            public void handle(long dt) {

                if (t > 0) {

                    ct += (dt - t) / 1000000000.0;

                    if (ct > delta) {

                        for (int lol = 0; lol < (speed ? width * height : 1); lol ++) {

                            ct -= delta;

                            if (lol == 0)
                                gc.clearRect(0, 0, width * cellSize, height * cellSize);

                            int nX = pX;
                            int nY = pY;

                            switch (dir) {
                                case 'w':
                                    nY--;
                                    break;
                                case 's':
                                    nY++;
                                    break;
                                case 'd':
                                    nX++;
                                    break;
                                case 'a':
                                    nX--;
                                    break;

                            }

                            if (nX >= width)
                                nX = 0;
                            if (nY >= height)
                                nY = 0;
                            if (nX < 0)
                                nX = width - 1;
                            if (nY < 0)
                                nY = height - 1;

                            Point lp = (snake.size() > 0 ? new Point(snake.get(snake.size() - 1).x, snake.get(snake.size() - 1).y) : new Point(pX, pY));

                            gc.setFill(Color.GREEN);

                            int px = pX, py = pY;
                            for (int i = 0; i < snake.size(); i++) {
                                Point p = snake.get(i);
                                int sx = p.x, sy = p.y;
                                p.x = px;
                                p.y = py;
                                px = sx;
                                py = sy;
                                if (lol == 0)
                                    gc.fillRect(p.x * cellSize, p.y * cellSize, cellSize, cellSize);
                                if (nX == p.x && nY == p.y)
                                    snake.clear();
                            }

                            if (nX == fX && nY == fY) {
                                score++;
                                snake.add(lp);
                                possible.clear();
                                for (int i = 0; i < width; i++)
                                    for (int j = 0; j < height; j++)
                                        possibleT[i][j] = true;
                                for (Point p : snake)
                                    possibleT[p.x][p.y] = false;
                                possibleT[nX][nY] = false;
                                for (int i = 0; i < width; i++)
                                    for (int j = 0; j < height; j++)
                                        if (possibleT[i][j])
                                            possible.add(new Point(i, j));
                                if (possible.size() > 0) {
                                    Point np = possible.get(rnd.nextInt(possible.size()));
                                    fX = np.x;
                                    fY = np.y;
                                } else {
                                    snake.clear();
                                }
                            } /*else {
                            if (score++ < 10)
                                snake.add(lp);
                        }*/

                            pX = nX;
                            pY = nY;

                            if (lol == 0) {
                                gc.fillRect(pX * cellSize, pY * cellSize, cellSize, cellSize);

                                gc.setFill(Color.RED);
                                gc.fillRect(fX * cellSize, fY * cellSize, cellSize, cellSize);
                            }

                            if (AI) {
                                if (nX == 0) {
                                    if (nY == 0)
                                        dir = 'd';
                                    else if (nY == height - 1)
                                        dir = 'w';
                                } else if (nX == width - 1) {
                                    if (nY == 0)
                                        dir = 's';
                                    else if (nY == height - 1)
                                        dir = 'a';
                                } else if (nX == width - 2) {
                                    if (nY == 1)
                                        dir = 'a';
                                    else if (nY == height - 1)
                                        dir = 'w';
                                } else if (nX == width - 3 && nY > 0) {
                                    if (nY == height - 1)
                                        dir = 'a';
                                    else if (nY % 2 == 0)
                                        dir = 's';
                                    else
                                        dir = 'a';
                                } else if (nX == 1 && nY < height - 1 && nY > 0) {
                                    if (nY % 2 == 0)
                                        dir = 'd';
                                    else
                                        dir = 's';
                                }
                            }
                        }
                    }

                }

                t = dt;

            }

        }.start();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
