package Automata;

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

//

public class Colony extends Application {

    class Cell {
        public int value;

        Cell (int value) {
            this.value = value;
        }
    }

    static GraphicsContext gc;

    static Map<String, Cell> cells = new HashMap<>();

    static double offsetX = 0, offsetY = 0;
    static double cellSize = 20;
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
        for(Map.Entry<String, Cell> entry : cells.entrySet()) {
            String key = entry.getKey();
            Cell value = entry.getValue();

            String[] psa = key.split(",");
            int x = Integer.parseInt(psa[0]), y = Integer.parseInt(psa[1]);
        }
    }

    static void render() {
        gc.clearRect(0, 0, 800, 800);
        gc.setFill(Color.WHITE);
        for (int i = -1; i < 800 / cellSize + 1; i++) {
            for (int j = -1; j < 800 / cellSize + 1; j++) {
                if (cells.containsKey((i + (int)offsetX) + "," + (j + (int)offsetY))) {
                    int v = cells.get((i + (int)offsetX) + "," + (j + (int)offsetY)).value;
                    gc.setFill(Color.rgb(v, v / 4, 0));
                    gc.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
                }
            }
        }
        gc.setFill(Color.INDIANRED);

        if (settings)
            renderSettings();
    }

    static void renderSettings() {

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
        primaryStage.setTitle("Name");
        primaryStage.setScene(s);
        primaryStage.setResizable(false);
        primaryStage.show();

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
                //(int) (e.getX() / cellSize) + (int) offsetX, ((int) (e.getY() / cellSize) + (int) offsetY)
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
                    cells.clear();
                    offsetX = 0;
                    offsetY = 0;
                    cellSize = 20;
                    settings = false;
                    render();
                    break;
                case E:
                    settings = !settings;
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
