package RandomStuff;

import java.util.Random;
import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Tetride extends Application {

    GraphicsContext gc;

    static double frameTime = 1;
    static int width = 7, height = 16, cellSize = 50;
    static ArrayList<BlockData> blocks = new ArrayList<>();
    static Block[] BLOCKS = {
            new Block(new Point[] {new Point(0,0), new Point(1,0), new Point(1,1), new Point(0,1)}),
    };

    static class BlockData {
        Block block;
        Color color;
        int x, y;

        public BlockData(Block b, Color c, int x, int y) {
            block = b;
            color = c;
            this.x = x;
            this.y = y;
        }
    }

    static class Block {
        Point[] points;

        public Block(Point[] p) {
            points = p;
        }

//        public Block clone() {
//            Point[] ps = new Point[points.length];
//            for (int i = 0; i < points.length; i++) {
//                ps[i] = points[i].clone();
//            }
//            return new Block(ps, color);
//        }
    }

    static class Point {
        int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

//        public boolean equals(Point p) {
//            return (p.x == x && p.y == y);
//        }

//        public Point clone() {
//            return new Point(x, y);
//        }
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

        blocks.add(new BlockData(BLOCKS[0], Color.RED, 0, 0));

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

            long lt = 0;
            double lastFrame = 0;
            double vx = 0, vy = 0;

            @Override
            public void handle(long t) {

                if (lt > 0) {

                    double delta = (t - lt) / 1000000000f;
                    lastFrame += delta;

                    if (lastFrame >= frameTime) {
                        lastFrame -= frameTime;

                        gc.clearRect(0,0, width * cellSize, height * cellSize);

                        for (BlockData b : blocks) {
                            gc.setFill(b.color);
                            for (Point p : b.block.points) {
                                gc.fillRect((p.x + b.x) * cellSize, height * cellSize - (p.y + b.y + 1) * cellSize, cellSize, cellSize);
                            }
                        }

                    }

                }

                lt = t;

            }

        }.start();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
