package RandomStuff;

import Generators.Maze;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

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

public class CellularAutomata extends Application {

    static GraphicsContext gc;

    static Set<String> cells = new HashSet(), checkedCells = new HashSet(), save = new HashSet();
    static double offsetX = 0, offsetY = 0;

    static double cellSize = 20;
    static boolean remove = false;
    static double zoomSpeed = 1.02;

    static double lastX = 0;
    static double lastY = 0;

    static boolean settings = false, changingDead = false,
    timerStarted = false,
    //Moore = neighbors adjacent and diagonal.
    //False = Von Neumann = adjacent only
    moore = true,
    triangle = false;
    //TODO: Make hexagon. sin(60*n), cos(60*n), nE[0,6)

    static int skipSpeed = 1;

    static boolean[]
    deadN = new boolean[] {
            false,
            false,
            false,
            true,
            false,
            false,
            false,
            false,
            false,
    },
    aliveN = new boolean[] {
            false,
            false,
            true,
            true,
            false,
            false,
            false,
            false,
            false,
    };

    static AnimationTimer t;

    static void nextGeneration() {
        HashSet<String> nextGen = new HashSet();
        HashSet<String> nextCheck = new HashSet();
        checkedCells.forEach(ps -> {
            String[] psa = ps.split(",");
            int[] p = {Integer.parseInt(psa[0]), Integer.parseInt(psa[1])};
            int n = 0;
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i == 0 && j == 0)
                        continue;
                    if (!moore) {
                        if (Math.abs(i) + Math.abs(j) == 2)
                            continue;
                        if (triangle) {
                            if ((p[0] + i) % 2 == 0) {
                                if ((p[1] + j) % 2 == 0) {
                                    if (i == 0 && j == -1)
                                        continue;
                                } else {
                                    if (i == 0 && j == 1)
                                        continue;
                                }
                            } else {
                                if ((p[1] + j) % 2 == 1) {
                                    if (i == 0 && j == -1)
                                        continue;
                                } else {
                                    if (i == 0 && j == 1)
                                        continue;
                                }
                            }
                        }
                    }
                    if (cells.contains((p[0] + i) + "," + (p[1] + j)))
                        n++;
                }
            }
            boolean alive = cells.contains(p[0] + "," + p[1]); // ? cells.get(p[0] + "," + p[1]) : false;
            if (alive)
                alive = aliveN[n];
            else
                alive = deadN[n];
            if (alive) {
                nextGen.add(p[0] + "," + p[1]);
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        if (!moore)
                            if (Math.abs(i) + Math.abs(j) == 2)
                                continue;
                        nextCheck.add((p[0] + i) + "," + (p[1] + j));
                    }
                }
            }
            else {
                nextGen.remove(p[0] + "," + p[1]);
            }
        });
        cells = nextGen;
        checkedCells = nextCheck;
    }

    static void render() {
        gc.clearRect(0, 0, 800, 800);
        gc.setFill(Color.WHITE);
        double[] x = new double[3], y = new double[3];
        for (int i = -1; i < 800 / cellSize + 1; i++) {
            for (int j = -1; j < 800 / cellSize + 1; j++) {
                if (cells.contains((i + (int)offsetX) + "," + (j + (int)offsetY))) {
                    if (!triangle)
                        gc.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
                    else {
                        if ((i + (int) offsetX) % 2 == 0) {
                            if ((j + (int) offsetY) % 2 == 0) {
                                x[0] = i * cellSize - 0.5 * cellSize;
                                x[1] = i * cellSize + cellSize * 1.5;
                                x[2] = i * cellSize + cellSize / 2;
                                y[0] = j * cellSize;
                                y[1] = j * cellSize;
                                y[2] = j * cellSize + cellSize;
                            } else {
                                x[0] = i * cellSize - 0.5 * cellSize;
                                x[1] = i * cellSize + cellSize * 1.5;
                                x[2] = i * cellSize + cellSize / 2;
                                y[0] = j * cellSize + cellSize;
                                y[1] = j * cellSize + cellSize;
                                y[2] = j * cellSize;
                            }
                        } else {
                            if ((j + (int) offsetY) % 2 == 1) {
                                x[0] = i * cellSize - 0.5 * cellSize;
                                x[1] = i * cellSize + cellSize * 1.5;
                                x[2] = i * cellSize + cellSize / 2;
                                y[0] = j * cellSize;
                                y[1] = j * cellSize;
                                y[2] = j * cellSize + cellSize;
                            } else {
                                x[0] = i * cellSize - 0.5 * cellSize;
                                x[1] = i * cellSize + cellSize * 1.5;
                                x[2] = i * cellSize + cellSize / 2;
                                y[0] = j * cellSize + cellSize;
                                y[1] = j * cellSize + cellSize;
                                y[2] = j * cellSize;
                            }
                        }
                        gc.fillPolygon(x, y, 3);
                    }
                }
            }
        }
        if (settings)
            renderSettings();
    }

    static void addPoint(int x, int y) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                checkedCells.add((x + i) + "," + (y + j));
            }
        }
        if (cells.contains(x + "," + y))
            cells.remove(x + "," + y);
        else
            cells.add(x + "," + y);
    }

    static void addPoint(int x, int y, boolean b) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (!moore)
                    if (Math.abs(i) + Math.abs(j) == 2)
                        continue;
                checkedCells.add((x + i) + "," + (y + j));
            }
        }
        if (cells.contains(x + "," + y)) {
            if (!b)
                cells.remove(x + "," + y);
        }
        else if (b)
            cells.add(x + "," + y);
    }

    static void renderSettings() {
        gc.setFill(Color.rgb(200, 200, 200, 0.6));
        gc.fillRect(0, 0, 800, 200);
        if (changingDead)
            gc.fillRect(5, 5, 790, 90);
        else
            gc.fillRect(5, 105, 790, 90);
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setTextBaseline(VPos.BOTTOM);
        for (int i = 0; i <= 8; i++) {
            if (deadN[i])
                gc.setFill(Color.rgb(0, 255, 0, 0.8));
            else
                gc.setFill(Color.rgb(255, 0, 0, 0.8));
            gc.fillRect(10 + 87.5 * i,10, 80, 80);

            if (aliveN[i])
                gc.setFill(Color.rgb(0, 255, 0, 0.8));
            else
                gc.setFill(Color.rgb(255, 0, 0, 0.8));
            gc.fillRect(10 + 87.5 * i,110, 80, 80);

            gc.setFill(Color.BLACK);
            gc.fillText(i + "", 12 + 87.5 * i,90);
            gc.fillText(i + "", 12 + 87.5 * i,190);
        }
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setTextBaseline(VPos.TOP);
        gc.fillText("BIRTH", 12, 10);
        gc.fillText("DIE", 12, 110);
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
        primaryStage.setTitle("Cellular Tomato");
        primaryStage.setScene(s);
        primaryStage.setResizable(false);
        primaryStage.show();

        addPoint(4, 4);
        addPoint(4, 5);

        canvas.setOnMouseDragged(e -> {
            if (settings) {

            }
            else {
                if (!e.isShiftDown()) {
                    addPoint(((int) (e.getX() / cellSize) + (int) offsetX), ((int) (e.getY() / cellSize) + (int) offsetY), !remove);
                    render();
                } else {
                    offsetX -= (e.getX() - lastX) / cellSize * 1;
                    offsetY -= (e.getY() - lastY) / cellSize * 1;
                    render();
                }
                lastX = e.getX();
                lastY = e.getY();
            }
        });

        canvas.setOnMousePressed(e -> {
            if (settings) {
                if (e.getY() < 90) {
                    if (e.getX() > 10 && e.getX() < 790)
                        deadN[(int)((e.getX() - 10) / 87.5)] = !deadN[(int)((e.getX() - 10) / 87.5)];
                }
                else if (e.getY() < 190) {
                    if (e.getX() > 10 && e.getX() < 790)
                        aliveN[(int)((e.getX() - 10) / 87.5)] = !aliveN[(int)((e.getX() - 10) / 87.5)];
                }
            }
            else {
                lastX = e.getX();
                lastY = e.getY();
                remove = false;
                int px = (int) (e.getX() / cellSize) + (int) offsetX, py = (int) (e.getY() / cellSize) + (int) offsetY;
                if (cells.contains(px + "," + py)) {
                    remove = true;
                }
                if (e.isControlDown()) {
                    addPoint(px, py);
                    addPoint(px + 2, py);
                    addPoint(px + 2, py + 1);
                    addPoint(px + 4, py + 2);
                    addPoint(px + 4, py + 3);
                    addPoint(px + 4, py + 4);
                    addPoint(px + 6, py + 3);
                    addPoint(px + 6, py + 4);
                    addPoint(px + 6, py + 5);
                    addPoint(px + 7, py + 4);
                }
                else if (e.isAltDown()) {
                    for (int i = 0; i < 16; i++) {
                        for (int j = 0; j < 16; j++) {
                            addPoint(px + i, py + j);
                        }
                    }
                }
                else if (!e.isShiftDown()) {
                    addPoint(px, py);
                }
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
                    skipSpeed--;
               case O:
                   skipSpeed++;
               case P:
                   for (int i = 0; i < skipSpeed; i++)
                       nextGeneration();
                   render();
                   break;
               case R:
                   checkedCells.clear();
                   cells.clear();
                   offsetX = 0;
                   offsetY = 0;
                   cellSize = 20;
                   render();
                   break;
               case S:
                   settings = !settings;
                   render();
                   break;
               case L:
                   cells = new HashSet(save);
                   cells.forEach(c -> {
                       String[] psa = c.split(",");
                       int[] p = {Integer.parseInt(psa[0]), Integer.parseInt(psa[1])};
                       for (int i = -1; i <= 1; i++) {
                           for (int j = -1; j <= 1; j++) {
                               checkedCells.add((p[0] + i) + "," + (p[1] + j));
                           }
                       }
                   });
                   render();
                   break;
               case Q:
                   save = new HashSet(cells);
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
