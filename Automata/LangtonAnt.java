package Automata;

import Generators.Maze;

import java.util.*;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LangtonAnt extends Application {

    class Ant {
        public int x, y, direction, id;
        public boolean right;

        Ant (int x, int y, int dir, int id, boolean right) {
            this.x = x;
            this.y = y;
            this.direction = dir;
            this.id = id;
            this.right = right;
        }
    }

    static GraphicsContext gc;

    static Map<String, Integer> cells = new HashMap<>();
    static ArrayList<Ant> ants = new ArrayList<>();
    // Turns right
    static ArrayList<Integer> antDirections = new ArrayList<>();
    static int length = 0;
    static double offsetX = 0, offsetY = 0;

    static double cellSize = 20;
    static boolean remove = false;
    static double zoomSpeed = 1.02;

    static double lastX = 0;
    static double lastY = 0;

    static boolean settings = false,
            timerStarted = false;

    static int skipSpeed = 1;

    static AnimationTimer t;

    static void nextGeneration() {
        HashSet<String> nextGen = new HashSet();
        HashSet<String> nextCheck = new HashSet();
        ants.forEach(ant -> {
            String key = ant.x + "," + ant.y;
            if (cells.containsKey(key)) {
                //cells.remove(ant.x + "," + ant.y);
                cells.put(key, cells.get(key) + 1);
                if (cells.get(key) == length) {
                    cells.remove(key);
                }
            }
            else {
                cells.put(key, 0);
            }
            ant.direction %= 4;
            if (ant.direction < 0)
                ant.direction += 4;
            switch (ant.direction) {
                case 0:
                    ant.y--;
                    break;
                case 1:
                    ant.x++;
                    break;
                case 2:
                    ant.y++;
                    break;
                case 3:
                    ant.x--;
                    break;
            }
            key = ant.x + "," + ant.y;
            if (cells.containsKey(key)) {
                switch (antDirections.get(cells.get(key))) {
                    case 0:
                        ant.direction++;
                        break;
                    case 1:
                        ant.direction--;
                        break;
                    case 2:
                        // stay same
                        break;
                    case 3:
                        ant.direction += 2;
                        break;
                }
            }
            else {
                ant.direction++;
            }
            /*if (cells.containsKey(ant.x + "," + ant.y)) {
                cells.remove(ant.x + "," + ant.y);
            }
            cells.put(ant.x + "," + ant.y, ant.id % );
            */
        });
    }

    static void render() {
        gc.clearRect(0, 0, 800, 800);
        gc.setFill(Color.WHITE);
        for (int i = -1; i < 800 / cellSize + 1; i++) {
            for (int j = -1; j < 800 / cellSize + 1; j++) {
                if (cells.containsKey((i + (int)offsetX) + "," + (j + (int)offsetY))) {
                    int v = cells.get((i + (int)offsetX) + "," + (j + (int)offsetY));
                    gc.setFill(Color.rgb(50 + 40 / length * v, 60 + 60 / length * v, 100 + v * 10 / length));
                    gc.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
                }
            }
        }
        gc.setFill(Color.INDIANRED);
        ants.forEach(ant -> {
            double bevel = cellSize * 0.1;
            gc.fillRect((ant.x - (int)offsetX) * cellSize + bevel, (ant.y - (int)offsetY) * cellSize + bevel, cellSize - bevel * 2, cellSize - bevel * 2);
        });
        if (settings)
            renderSettings();
    }

    static void renderSettings() {
        length = antDirections.size();
        for (int i = 0; i < length; i++) {
            String t = "";
            switch (antDirections.get(i)) {
                case 0:
                    gc.setFill(Color.rgb(127, 255, 0, 0.6));
                    t = ">";
                    break;
                case 1:
                    t = "<";
                    gc.setFill(Color.rgb(0, 255, 0, 0.6));
                    break;
                case 2:
                    t = "^";
                    gc.setFill(Color.rgb(255, 127, 0, 0.6));
                    break;
                case 3:
                    gc.setFill(Color.rgb(255, 0, 0, 0.6));
                    t = "v";
                    break;
            }
            gc.fillRect(10 + 30 * i,10, 25, 25);
            gc.setTextAlign(TextAlignment.LEFT);
            gc.setTextBaseline(VPos.TOP);
            gc.setFill(Color.BLACK);
            gc.fillText(t, 12 + 30 * i, 12);
        }
    }

    @Override
    public void start(Stage primaryStage) {

        Group root = new Group();
        Scene s = new Scene(root, 800, 800, Color.BLACK);

        final Canvas canvas = new Canvas(800,800);
        canvas.setFocusTraversable(true);
        gc = canvas.getGraphicsContext2D();

        root.getChildren().add(canvas);

        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("Langton's Ant");
        primaryStage.setScene(s);
        primaryStage.setResizable(false);
        primaryStage.show();

        antDirections.clear();
        antDirections.add(1);
        length = antDirections.size();

        ants.add(new Ant(0, 0, 0, 0, false));

        canvas.setOnMouseDragged(e -> {
            if (settings) {

            }
            else {
                offsetX -= (e.getX() - lastX) / cellSize * 1;
                offsetY -= (e.getY() - lastY) / cellSize * 1;
                render();
                lastX = e.getX();
                lastY = e.getY();
            }
        });

        canvas.setOnMousePressed(e -> {
            if (settings) {
                ants.add(new Ant((int) (e.getX() / cellSize) + (int) offsetX, ((int) (e.getY() / cellSize) + (int) offsetY), 0, 0, false));
            }
            else {
                lastX = e.getX();
                lastY = e.getY();
            }
            render();
        });

        canvas.setOnKeyPressed(e -> {
            switch(e.getCode()) {
                case SPACE:
                    nextGeneration();
                    render();
                    break;
                case I:
                    skipSpeed /= 2;
                    if (skipSpeed == 0)
                        skipSpeed++;
                    break;
                case O:
                    skipSpeed *= 2;
                    break;
                case P:
                    for (int i = 0; i < skipSpeed; i++)
                        nextGeneration();
                    render();
                    break;
                case R:
                    if (settings) {
                        antDirections.clear();
                    }
                    cells.clear();
                    cells.clear();
                    offsetX = 0;
                    offsetY = 0;
                    cellSize = 20;
                    ants.clear();
                    settings = false;
                    render();
                    break;
                case E:
                    settings = !settings;
                    render();
                    break;
                case W:
                    if (settings)
                    antDirections.add(2);
                    render();
                    break;
                case S:
                    if (settings)
                    antDirections.add(3);
                    render();
                    break;
                case D:
                    if (settings)
                    antDirections.add(0);
                    render();
                    break;
                case A:
                    if (settings)
                    antDirections.add(1);
                    render();
                    break;
                case T:
                    if (timerStarted)
                        t.stop();
                    else
                        t.start();
                    timerStarted = !timerStarted;
                    break;
            }
        });

        canvas.setOnScroll(e -> {
            cellSize *= (e.getDeltaY() > 0) ? zoomSpeed : 1 / zoomSpeed;
            render();
        });

        render();

        t = new AnimationTimer() {
            @Override
            public void handle(long dt) {
                nextGeneration();
                render();
            }
        };

    }

    public static void main(String[] args) {
        launch(args);
    }

}
