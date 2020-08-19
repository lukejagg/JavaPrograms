package RandomStuff;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import Generators.Maze;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.paint.*;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

class Cube {

    int sx, sy, sz;

    double x,y,z;

    short blockId;

    boolean down;
    boolean up;
    boolean left;
    boolean right;
    boolean forward;
    boolean back;

    Color c;

    Cube(int x, int y, int z, short id, boolean[] neighbors, int sx, int sy, int sz, Color c) {

        this.x = x;
        this.y = y;
        this.z = z;
        blockId = id;

        down = neighbors[0];
        up = neighbors[1];
        left = neighbors[2];
        right = neighbors[3];
        back = neighbors[4];
        forward = neighbors[5];

        this.sx = sx;
        this.sy = sy;
        this.sz = sz;

        this.c = c;
    }

    void drawLeftRight(boolean b, Color c) {
        if (b) {
            if (left)
                CubePuzzle.drawFace(x, y, z, x, y+sy, z, x, y+sy, z+sz, x, y, z+sz, c, 0.7, 2);
        }
        else {
            if (right)
                CubePuzzle.drawFace(x+sx, y, z, x+sx, y+sy, z, x+sx, y+sy, z+sz, x+sx, y, z+sz, c, 0.7, 3);
        }
    }

    void drawBottomTop(boolean b, Color c) {
        if (b) {
            if (down)
                CubePuzzle.drawFace(x, y, z, x+sx, y, z, x+sx, y, z+sz, x, y, z+sz, c, 0.4, 0);
        }
        else {
            if (up)
                CubePuzzle.drawFace(x, y+sy, z, x+sx, y+sy, z, x+sx, y+sy, z+sz, x, y+sy, z+sz, c, 1, 1);
        }
    }

    void drawBackForward(boolean b, Color c) {
        if (b) {
            if (back)
                CubePuzzle.drawFace(x, y, z, x+sx, y, z, x+sx, y+sy, z, x, y+sy, z, c, 0.8, 4);
        }
        else {
            if (forward)
                CubePuzzle.drawFace(x, y, z+sz, x+sx, y, z+sz, x+sx, y+sy, z+sz, x, y+sy, z+sz, c, 0.6, 5);
        }
    }

    void drawCube() {

//        Color c = COLORS[blockId - 1];

        double d1 = distance(x-sx, y, z), d2 = distance(x, y-sy, z), d3 = distance(x, y, z-sz);
        boolean fx = true, fy = true, fz = true;

        if (d1 > distance(x+sx, y, z)) {
            d1 = distance(x+sx, y, z);
            fx = false;
        }
        if (d2 > distance(x, y+sy, z)) {
            d2 = distance(x, y+sy, z);
            fy = false;
        }
        if (d3 > distance(x, y, z+sz)) {
            d3 = distance(x, y, z+sz);
            fz = false;
        }

        if (d1 < d2) {
            if (d1 < d3) {
                if (d2 < d3) { // 1 < 2 < 3
                    drawBackForward(fz, c);
                    drawBottomTop(false, c);
                    drawLeftRight(fx, c);
                }
                else { // 1 < 2 & 1 < 3 & 3 < 2
                    drawBottomTop(false, c);
                    drawBackForward(fz, c);
                    drawLeftRight(fx, c);
                }
            }
            else { // 3 < 1 < 2
                drawBottomTop(false, c);
                drawLeftRight(fx, c);
                drawBackForward(fz, c);
            }
        }
        else { // 1 > 2
            if (d2 < d3) { // 2 < 3
                if (d1 < d3) { // 2 < 1 < 3
                    drawBackForward(fz, c);
                    drawLeftRight(fx, c);
                    drawBottomTop(false, c);
                }
                else { // 2 < 3 < 1
                    drawLeftRight(fx, c);
                    drawBackForward(fz, c);
                    drawBottomTop(false, c);
                }
            }
            else { // 3 < 2 < 1
                drawLeftRight(fx, c);
                drawBottomTop(false, c);
                drawBackForward(fz, c);
            }
        }
    }

    static double distance(double x, double y, double z) {
        x -= CubePuzzle.playerX;
        z -= CubePuzzle.playerZ;
        return x*x + y*y + z*z;
    }

}

public class CubePuzzle extends Application {

    // VIEW SETTINGS
    static final int WIDTH = 1200;
    static final int HEIGHT = 800;

    static final double FOV = 56;
    static final double VIEW_FACTOR = 360 / Math.tan(Math.toRadians(FOV / 2));

    // UI / SOUND
    static GraphicsContext gc;

    // Player
    static double playerX = 4, playerY = 8, playerZ = 0;
    static int blockX = 0, blockZ = 0;
    static double renderX = 0, renderZ = 0;
    static double cameraY = 0, cameraX = -2.7;
    static Cube cube, beam;
    static int time = 2;
    static boolean dead = false, win = false;
    static int level = 0;

    /*
    1: block
    2: spawn
    3: end
    4: 2 health block
    10+x (odd): activator
    10+x (even): activated (ID: Activator ID+1)
     */
    static int[][][] blocks = {
            {
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 1, 1, 2, 0, 0, 0, 0},
                    {0, 1, 1, 1, 0, 0, 0, 0},
                    {0, 1, 3, 1, 0, 0, 0, 0},
                    {0, 1, 1, 1, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
            },
            {
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 2, 0, 0, 0},
                    {0, 0, 0, 0, 1, 0, 0, 0},
                    {0, 0, 0, 0, 1, 0, 0, 0},
                    {0, 0, 0, 0, 3, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
            },
            {
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 2, 0, 0, 0},
                    {0, 0, 0, 1, 1, 0, 0, 0},
                    {0, 0, 0, 1, 1, 0, 0, 0},
                    {0, 0, 0, 0, 3, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
            },
            {
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 1, 1, 2, 0, 0, 0},
                    {0, 0, 1, 1, 1, 0, 0, 0},
                    {0, 0, 1, 1, 1, 0, 0, 0},
                    {0, 0, 0, 0, 3, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
            },
            {
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 2, 1, 1, 0},
                    {0, 0, 0, 1, 1, 1, 1, 0},
                    {0, 0, 0, 1, 1, 1, 1, 0},
                    {0, 0, 0, 0, 3, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
            },
            {
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 1, 1, 0, 0, 0},
                    {0, 0, 2, 4, 1, 0, 0, 0},
                    {0, 0, 0, 1, 1, 0, 0, 0},
                    {0, 0, 0, 0, 3, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
            },
            {
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 2, 0, 0, 0, 0},
                    {0, 0, 0, 4, 0, 0, 0, 0},
                    {0, 0, 0, 4, 0, 0, 0, 0},
                    {0, 0, 0, 3, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
            },
            {
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 2, 0, 0, 0, 0},
                    {0, 0, 0, 4, 0, 0, 0, 0},
                    {0, 0, 0, 4, 0, 0, 0, 0},
                    {0, 0, 0, 4, 4, 3, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
            },
            {
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 2, 4, 0, 0, 0},
                    {0, 0, 0, 4, 4, 0, 0, 0},
                    {0, 0, 0, 4, 4, 0, 0, 0},
                    {0, 0, 0, 4, 4, 0, 0, 0},
                    {0, 0, 0, 3, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
            },
            {
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 2, 0, 0, 0, 0},
                    {0, 0, 0, 1, 1, 0, 0, 0},
                    {0, 1, 1, 1, 1, 0, 0, 0},
                    {0, 1, 1, 4, 1, 1, 3, 0},
                    {0, 0, 1, 4, 0, 0, 0, 0},
                    {0, 0, 1, 1, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
            },
            {
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 1, 1, 2, 0, 0, 0},
                    {0, 0, 1, 4, 4, 1, 1, 0},
                    {0, 0, 1, 4, 4, 3, 1, 0},
                    {0, 0, 0, 0, 1, 1, 1, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
            },
            {
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 2, 0, 0, 0, 0},
                    {0, 0, 0, 1, 0, 0, 0, 0},
                    {0, 3, 1, 4, 1, 0, 0, 0},
                    {0, 0, 0, 1, 1, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
            },
            {
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
            },
    };

    static int[][] map = {};

    static Cube[][] cubes;

    static double lookSpeed = 1.6;

    static int facing = 0;

   // static ArrayList<Cube> cubes = new ArrayList<Cube>();

    static double xToScreen(double x) {
        return x + WIDTH / 2.0;
    }

    static double yToScreen(double y) {
        return y + HEIGHT / 2.0;
    }

    static double screenToX(double x) {
        return x - WIDTH / 2.0;
    }

    static double screenToY(double y) {
        return y - HEIGHT / 2.0;
    }

    static double sin(double t) {
        return Math.sin(t);
    }

    static double cos(double t) {
        return Math.cos(t);
    }

    static double tan(double t) {
        return Math.tan(t);
    }

    static double sqrt(double n) {
        return Math.sqrt(n);
    }

    static double sign(double n) {
        if(n>=0)
            return 1;
        return -1;
    }

    static double[] rotatePoint(double x, double y, double z) {
        double c1 = cos(cameraY);
        double s1 = sin(cameraY);
        double c2 = cos(cameraX);
        double s2 = sin(cameraX);
        double pX = x*c1 - z*s1;
        double pY = s1*c2*x + c1*c2*z + s2*y;
        double pZ = -s1*s2*x - c1*s2*z + c2*y;
        return new double[] { pX, pY, pZ }; }

    static void drawFace(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4, Color color, double shade, int face) {

        if (facing == face)
            return;

        double[] p1 = rotatePoint(x1 - playerX, y1 - playerY, z1 - playerZ);
        if (p1[2] < 0)
            return;
        double[] p2 = rotatePoint(x2 - playerX, y2 - playerY, z2 - playerZ);
        if (p2[2] < 0)
            return;
        double[] p3 = rotatePoint(x3 - playerX, y3 - playerY, z3 - playerZ);
        if (p3[2] < 0)
            return;
        double[] p4 = rotatePoint(x4 - playerX, y4 - playerY, z4 - playerZ);
        if (p4[2] < 0)
            return;


//		if (p1[2] > 0  && p2[2] > 0 && p3[2] > 0 && p4[2] > 0) {

        double px = xToScreen(p1[0]*VIEW_FACTOR/p1[2]);
        double py = yToScreen(p1[1]*VIEW_FACTOR/p1[2]);
        double qx = xToScreen(p2[0]*VIEW_FACTOR/p2[2]);
        double qy = yToScreen(p2[1]*VIEW_FACTOR/p2[2]);
        double rx = xToScreen(p3[0]*VIEW_FACTOR/p3[2]);
        double ry = yToScreen(p3[1]*VIEW_FACTOR/p3[2]);
        double sx = xToScreen(p4[0]*VIEW_FACTOR/p4[2]);
        double sy = yToScreen(p4[1]*VIEW_FACTOR/p4[2]);

        color = Color.hsb(color.getHue(), color.getSaturation(), color.getBrightness() * shade, color.getOpacity());
        gc.setFill(color);

        gc.fillPolygon(new double[] {px, qx, rx, sx}, new double[] {py, qy, ry, sy}, 4);

//		}
    }

    public static double unsquaredMagnitude(double x, double y, double z) {
        return x*x + y*y + z*z;
    }

    static void checkPosition() {
        if (map[blockX][blockZ] == 0)
            dead = true;
        int count = 0;
        for (int x = 0; x < map.length; x++) {
            for (int z = 0; z < map[0].length; z++) {
                if (map[x][z] != 0)
                    count++;
            }
        }
        if (count == 1 && map[blockX][blockZ] == 3) {
            win = true;
            beam = (new Cube(blockX, 1, blockZ, (short) 1, new boolean[] {true,true,true,true,true,true}, 1, 5, 1, new Color(1,1,0, 0)));
        }
    }

    static void dropBlock() {
        if (map[blockX][blockZ] != 0) {
            if (map[blockX][blockZ] == 4) {
                map[blockX][blockZ] = 1;
                //cubes[blockX][blockZ].c = new Color(180/255f, 120/255f, 120/255f, 1);
            }
            else if (map[blockX][blockZ] == 3) {
                generateCubes();
            }
            else
                map[blockX][blockZ] = 0;
        }
    }

    @Override
    public void start(Stage primaryStage) {

        Group root = new Group();
        Scene s = new Scene(root, WIDTH, HEIGHT, Color.BLACK);

        final Canvas canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();

        root.getChildren().add(canvas);

        primaryStage.initStyle(StageStyle.UNIFIED);
        primaryStage.setTitle("C-Cubed");
        primaryStage.setScene(s);
        primaryStage.setResizable(false);
        primaryStage.show();

        canvas.setFocusTraversable(true);

        canvas.setOnMouseMoved(e -> {

//    		Robot r;
//			try {
//				r = new Robot();
//
//	            cameraX = Math.max(-Math.PI/2, Math.min(cameraX, Math.PI/2));
//			} catch (AWTException e1) {
//				e1.printStackTrace();
//			}

//            cameraY = 8 * screenToX(e.getX()) / WIDTH;
//            cameraX = -8 * screenToY(e.getY()) / HEIGHT;
//
//            System.out.println(cameraY + " " + cameraX);


        });

        canvas.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case W:
                    if (!dead && !win && blockZ < map[0].length - 1) {
                        dropBlock();
                        blockZ++;
                        checkPosition();
                    }
                    break;
                case A:
                    if (!dead && !win && blockX > 0) {
                        dropBlock();
                        blockX--;
                        checkPosition();
                    }
                    break;
                case S:
                    if (!dead && !win && blockZ > 0) {
                        dropBlock();
                        blockZ--;
                        checkPosition();
                    }
                    break;
                case D:
                    if (!dead && !win && blockX < map[0].length - 1) {
                        dropBlock();
                        blockX++;
                        checkPosition();
                    }
                    break;
                case R:
                    generateCubes();
                    break;

                default:
                    break;
            }
        });

        canvas.setOnKeyReleased(e -> {
            switch (e.getCode()) {
                case W:
                    break;
                case A:
                    break;
                case S:
                    break;
                case D:
                    break;
                default:
                    break;
            }
        });

        generateCubes();

        AnimationTimer timer = new AnimationTimer() {

            long lt = 0;

            @Override
            public void handle(long dt) {

                if (lt > 0) {

                    double deltaTime = (dt - lt) / 1000000000.0;

                    double c1 = cos(cameraY);
                    double s1 = sin(cameraY);
                    double c2 = cos(cameraX);
                    double s2 = sin(cameraX);
                    double[] normal = new double[] { s1*s2, -c2, c1*s2 };
//    	    		System.out.println(normal[0] + " " + normal[1] + " " + normal[2]);
                    double[] distanceFromFaces = new double[] {unsquaredMagnitude(normal[0], normal[1]-1, normal[2]),
                            unsquaredMagnitude(normal[0], normal[1]+1, normal[2]),
                            unsquaredMagnitude(normal[0]-1, normal[1], normal[2]),
                            unsquaredMagnitude(normal[0]+1, normal[1], normal[2]),
                            unsquaredMagnitude(normal[0], normal[1], normal[2]-1),
                            unsquaredMagnitude(normal[0], normal[1], normal[2]+1),
                    };
                    double d = distanceFromFaces[0];
                    int index = 0;
                    for (int f = 0; f < 6; f++)
                        if (distanceFromFaces[f] < d)
                            d = distanceFromFaces[index = f];

                    for (int x = 0; x < map.length; x++) {
                        for (int z = 0; z < map[0].length; z++) {
                           if (map[x][z] == 0 && cubes[x][z] != null) {
                               cubes[x][z].y -= 1.6 * (cubes[x][z].y * -deltaTime * 4 + deltaTime);
                               if (cubes[x][z].y < -4)
                                   cubes[x][z] = null;
                               else
                                   cubes[x][z].c = new Color(cubes[x][z].c.getRed(), cubes[x][z].c.getGreen(), cubes[x][z].c.getBlue(), 1 + cubes[x][z].y / 4.5);
                           }
                           else if (map[x][z] == 1 && cubes[x][z] != null) {
                               cubes[x][z].c = new Color(cubes[x][z].c.getRed() + (120/255f - cubes[x][z].c.getRed()) * 0.2, cubes[x][z].c.getGreen() + (120/255f - cubes[x][z].c.getGreen()) * 0.2, cubes[x][z].c.getBlue() + (120/255f - cubes[x][z].c.getBlue()) * 0.2, 1);
                           }

                        }
                    }

                    facing = index;
                    renderCubes();

                    if (dead && !win) {
                        cube.y -= 1.6 * ((cube.y - 1) * -deltaTime * 4 + deltaTime);
                        if (cube.y < -3)
                            generateCubes();
                        else
                            cube.c = new Color(cube.c.getRed(), cube.c.getGreen(), cube.c.getBlue(), Math.max(1 + (cube.y - 1) / 4.5, 0));
                    }

                    renderX = renderX + (blockX - renderX) * 0.3;
                    renderZ = renderZ + (blockZ - renderZ) * 0.3;
                    cube.x = renderX;
                    cube.z = renderZ;
                    if (win) {
                        if (Math.abs(renderX - blockX) < 0.05 && Math.abs(renderZ - blockZ) < 0.05) {
                            cube.y += deltaTime * 2;
                            if (cube.y > 3.5) {
                                level++;
                                generateCubes();
                            }
                            else
                                cube.c = new Color(cube.c.getRed(), cube.c.getGreen(), cube.c.getBlue(), Math.max(1 - (cube.y - 1) / 2, 0));
                        }
                    }
                    cube.drawCube();
                    if (beam != null) {
                        beam.c = new Color(1,1,0, Math.min(beam.c.getOpacity() + (0.25 - beam.c.getOpacity()) * 0.005 + deltaTime * 0.15, 0.25));
                        beam.drawCube();
                    }
                }

                lt = dt;

            }

        };

        timer.start();
    }

    public static void generateCubes() {
        if (level >= blocks.length)
            level = blocks.length - 1;
        dead = false;
        win = false;
        cubes = new Cube[blocks[level].length][blocks[level][0].length];
        playerX = blocks[level].length / 2f;
        int startX = 0, startZ = 0;
        map = new int[blocks[level].length][blocks[level][0].length];
        for (int x = 0; x < blocks[level].length; x++) {
            for (int z = 0; z < blocks[level][0].length; z++) {
                map[x][z] = blocks[level][x][z];
                Color c = new Color(120/255f, 120/255f, 120/255f, 1);
                switch (map[x][z]) {
                    case 3:
                        c = new Color(120/255f, 240/255f, 120/255f, 1);
                        break;
                    case 4:
                        c = new Color(240/255f, 120/255f, 120/255f, 1);
                        break;
                }
                if (blocks[level][x][z] != 0)
                    cubes[x][z] = (new Cube(x, 0, z, (short) 1, new boolean[] {true,true,true,true,true,true}, 1, 1, 1, c));
                if (blocks[level][x][z] == 2) {
                    startX = x;
                    startZ = z;
                }

            }
        }

        blockX = startX;
        blockZ = startZ;
        renderX = blockX;
        renderZ = blockZ;
        cube = new Cube(startX, 1, startZ, (short) 1, new boolean[] {true,true,true,true,true,true}, 1, 1, 1, Color.rgb(200,120,120));
        beam = null;
        /*cubes.sort(new Comparator<Cube>() {
            @Override
            public int compare(Cube o1, Cube o2) {
                double dx2 = o1.x - playerX;
                double dy2 = o1.y - playerY;
                double dz2 = o1.z - playerZ;
                double dx = o2.x - playerX;
                double dy = o2.y - playerY;
                double dz = o2.z - playerZ;
                double d1 = dx*dx+dy*dy+dz*dz;
                double d2 = dx2*dx2+dy2*dy2+dz2*dz2;
                if (d1 < d2)
                    return -1;
                if (d1 > d2)
                    return 1;
                return 0;
            }
        });
        */
    }

    public static void renderCubes() {

        gc.clearRect(0, 0, WIDTH, HEIGHT);
        /*cubes.forEach(c -> {
            c.drawCube();
        });*/

        for (int x = map.length - 1; x >= map.length / 2; x--) {
            for (int z = map[0].length - 1; z > -1; z--) {
                if (cubes[x][z] != null)
                    cubes[x][z].drawCube();

            }
        }

        for (int x = 0; x <= map.length / 2 - 1; x++) {
            for (int z = map[0].length - 1; z > -1; z--) {
                if (cubes[x][z] != null)
                    cubes[x][z].drawCube();

            }
        }

    }

    public static void main(String[] args) {
        launch(args);
    }

}
