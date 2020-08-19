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

public class CustomSearchAutomata extends Application {

    static GraphicsContext gc;

    static Set<String> cells = new HashSet(), checkedCells = new HashSet(), save = new HashSet();
    static double offsetX = 0, offsetY = 0;

    static double cellSize = 20;
    static boolean remove = false;
    static double zoomSpeed = 1.02;

    static double lastX = 0;
    static double lastY = 0;

    static boolean settings = false, changingDead = false,
            moore = false;

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
    }, aliveN = new boolean[] {
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

    static int[] searchX = new int[] {
        -1, 0, 1, -1, 1, -1, 0, 1
    };
    static int[] searchY = new int[] {
        -1, -1, -1, 0, 0, 1, 1, 1
    };

    static void nextGeneration() {
        HashSet<String> nextGen = new HashSet();
        HashSet<String> nextCheck = new HashSet();
        checkedCells.forEach(ps -> {
            String[] psa = ps.split(",");
            int[] p = {Integer.parseInt(psa[0]), Integer.parseInt(psa[1])};
            int n = 0;
            for (int i = 0; i < searchX.length; i++) {
                if (cells.contains((p[0] + searchX[i]) + "," + (p[1] + searchY[i])))
                    n++;
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
                nextCheck.add(p[0] + "," + p[1]);
                for (int i = 0; i < searchX.length; i++) {
                    nextCheck.add((p[0] + searchX[i]) + "," + (p[1] + searchY[i]));
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
        for (int i = -1; i < 800 / cellSize + 1; i++) {
            for (int j = -1; j < 800 / cellSize + 1; j++) {
                if (cells.contains((i + (int)offsetX) + "," + (j + (int)offsetY))) {
                    gc.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
                }
            }
        }
        if (settings)
            renderSettings();
    }

    static void addPoint(int x, int y) {
        checkedCells.add(x + "," + y);
        for (int i = 0; i < searchX.length; i++) {
            checkedCells.add((x + searchX[i]) + "," + (y + searchY[i]));
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
        gc.fillText("DEAD", 12, 10);
        gc.fillText("ALIVE", 12, 110);
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
//               case D:
//                   changingDead = true;
//                   render();
//                   break;
//               case A:
//                   changingDead = false;
//                   render();
//                   break;
//               case DIGIT0:
//                   if (changingDead)
//                       deadN[0] = !deadN[0];
//                   else
//                       aliveN[0] = !aliveN[0];
//                   render();
//                   break;
//               case DIGIT1:
//                   if (changingDead)
//                       deadN[1] = !deadN[1];
//                   else
//                       aliveN[1] = !aliveN[1];
//                   render();
//                   break;
//               case DIGIT2:
//                   if (changingDead)
//                       deadN[2] = !deadN[2];
//                   else
//                       aliveN[2] = !aliveN[2];
//                   render();
//                   break;
//               case DIGIT3:
//                   if (changingDead)
//                       deadN[3] = !deadN[3];
//                   else
//                       aliveN[3] = !aliveN[3];
//                   render();
//                   break;
//               case DIGIT4:
//                   if (changingDead)
//                       deadN[4] = !deadN[4];
//                   else
//                       aliveN[4] = !aliveN[4];
//                   render();
//                   break;
//               case DIGIT5:
//                   if (changingDead)
//                       deadN[5] = !deadN[5];
//                   else
//                       aliveN[5] = !aliveN[5];
//                   render();
//                   break;
//               case DIGIT6:
//                   if (changingDead)
//                       deadN[6] = !deadN[6];
//                   else
//                       aliveN[6] = !aliveN[6];
//                   render();
//                   break;
//               case DIGIT7:
//                   if (changingDead)
//                       deadN[7] = !deadN[7];
//                   else
//                       aliveN[7] = !aliveN[7];
//                   render();
//                   break;
//               case DIGIT8:
//                   if (changingDead)
//                       deadN[8] = !deadN[8];
//                   else
//                       aliveN[8] = !aliveN[8];
//                   render();
//                   break;
            }
        });

        canvas.setOnScroll(e -> {
            cellSize *= (e.getDeltaY() > 0) ? zoomSpeed : 1 / zoomSpeed;
//            offsetX += (e.getX() - 400) * cellSize / 800 * (e.getDeltaY() > 0 ? 1 : -1);
            render();
        });

        render();

//        AnimationTimer timer = new AnimationTimer();

    }

    public static void main(String[] args) {
        searchX = new int[] {-1, -2, 0, 0, 1, 2, 0, 0};
        searchY = new int[] {0, 0, -1, -2, 0, 0, 1, 2};
        aliveN = new boolean[searchX.length];
        deadN = new boolean[searchX.length];
        aliveN[2] = true;
        aliveN[3] = true;
        deadN[3] = true;
        launch(args);
    }

}
