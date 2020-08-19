package MazeGen;

import java.util.ArrayList;
import java.util.Comparator;

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

    double sx, sy, sz;

    double x;
    double y;
    double z;

    short blockId;

    boolean down;
    boolean up;
    boolean left;
    boolean right;
    boolean forward;
    boolean back;

    Color c;

    Cube(double x, double y, double z, short id, boolean[] neighbors, double sx, double sy, double sz, Color c) {

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
                Maze3D_3D.drawFace(x, y, z, x, y+sy, z, x, y+sy, z+sz, x, y, z+sz, c, 0.9, 2);
        }
        else {
            if (right)
                Maze3D_3D.drawFace(x+sx, y, z, x+sx, y+sy, z, x+sx, y+sy, z+sz, x+sx, y, z+sz, c, 0.5, 3);
        }
    }

    void drawBottomTop(boolean b, Color c) {
        if (b) {
            if (down)
                Maze3D_3D.drawFace(x, y, z, x+sx, y, z, x+sx, y, z+sz, x, y, z+sz, c, 0.4, 0);
        }
        else {
            if (up)
                Maze3D_3D.drawFace(x, y+sy, z, x+sx, y+sy, z, x+sx, y+sy, z+sz, x, y+sy, z+sz, c, 0.8, 1);
        }
    }

    void drawBackForward(boolean b, Color c) {
        if (b) {
            if (back)
                Maze3D_3D.drawFace(x, y, z, x+sx, y, z, x+sx, y+sy, z, x, y+sy, z, c, 0.9, 4);
        }
        else {
            if (forward)
                Maze3D_3D.drawFace(x, y, z+sz, x+sx, y, z+sz, x+sx, y+sy, z+sz, x, y+sy, z+sz, c, 0.5, 5);
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
                    drawBottomTop(fy, c);
                    drawLeftRight(fx, c);
                }
                else { // 1 < 2 & 1 < 3 & 3 < 2
                    drawBottomTop(fy, c);
                    drawBackForward(fz, c);
                    drawLeftRight(fx, c);
                }
            }
            else { // 3 < 1 < 2
                drawBottomTop(fy, c);
                drawLeftRight(fx, c);
                drawBackForward(fz, c);
            }
        }
        else { // 1 > 2
            if (d2 < d3) { // 2 < 3
                if (d1 < d3) { // 2 < 1 < 3
                    drawBackForward(fz, c);
                    drawLeftRight(fx, c);
                    drawBottomTop(fy, c);
                }
                else { // 2 < 3 < 1
                    drawLeftRight(fx, c);
                    drawBackForward(fz, c);
                    drawBottomTop(fy, c);
                }
            }
            else { // 3 < 2 < 1
                drawLeftRight(fx, c);
                drawBottomTop(fy, c);
                drawBackForward(fz, c);
            }
        }
    }

    static double distance(double x, double y, double z) {
        x -= Maze3D_3D.playerX;
        y -= Maze3D_3D.playerY;
        z -= Maze3D_3D.playerZ;
        return x*x + y*y + z*z;
    }

}

public class Maze3D_3D extends Application {

    // VIEW SETTINGS
    static final int WIDTH = 1200;
    static final int HEIGHT = 800;

    static final double FOV = 70;
    static final double VIEW_FACTOR = 360 / Math.tan(Math.toRadians(FOV / 2));

    final static boolean LOOK_INVERSION = false;

    // UI / SOUND
    static GraphicsContext gc;

    // World Generator Settings
    static double seed = 1.0412;

    static double caveScale = 0.104;
    static double caveThreshold = -0.4;

    // Keys
    static boolean kW = false;
    static boolean kA = false;
    static boolean kS = false;
    static boolean kD = false;
    static boolean kE = false;
    static boolean kQ = false;
    static boolean kI = false;
    static boolean kJ = false;
    static boolean kK = false;
    static boolean kL = false;
    static boolean kSPACE = false;

    // Player
    static double playerX = 0;
    static double playerY = 0;
    static double playerZ = 0;

    static double cameraX = -90;
    static double cameraY = 0;

    static int currentW = 1;
    static int width = 3;
    static int height = 3;
    static int depth = 3;
    static int time = 2;

    static boolean[][][][] maze;

    static double lookSpeed = 1.6;

    static int facing = 0;

    static int currentX = 1, currentY = 1, currentZ = 1, currentT = 1;
    static int lX = 3, lY = 3, lZ = 3, lW = 3;

//	static double lastFPS = 0;

    static ArrayList<Cube> cubes = new ArrayList<Cube>();

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

        //Color c = Color.hsb(color.getHue(), color.getSaturation(), color.getBrightness() * shade, 1);
        gc.setFill(color);

        gc.fillPolygon(new double[] {px, qx, rx, sx}, new double[] {py, qy, ry, sy}, 4);

//		}
    }

    public static double unsquaredMagnitude(double x, double y, double z) {
        return x*x + y*y + z*z;
    }

    @Override
    public void start(Stage primaryStage) {

        Group root = new Group();
        Scene s = new Scene(root, WIDTH, HEIGHT, Color.BLACK);

        final Canvas canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();

        root.getChildren().add(canvas);

        primaryStage.initStyle(StageStyle.UNIFIED);
        primaryStage.setTitle("Limecraft");
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

            cameraY = 8 * screenToX(e.getX()) / WIDTH;
            cameraX = -8 * screenToY(e.getY()) / HEIGHT;

        });

        canvas.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case W:
                    kW = true;
                    break;
                case A:
                    kA = true;
                    break;
                case S:
                    kS = true;
                    break;
                case D:
                    kD = true;
                    break;
                case E:
                    kE = true;
                    break;
                case Q:
                    kQ = true;
                    break;
                case SPACE:
                    kSPACE = true;
                    break;
//                case I:
//                    kI = true;
//                    break;
//                case J:
//                    kJ = true;
//                    break;
//                case K:
//                    kK = true;
//                    break;
//                case L:
//                    kL = true;
//                    break;
//                case O:
//                    currentW++;
//                    if (currentW >= time - 1)
//                        currentW = time - 2;
//                    else
//                        generateCubes();
//                    break;
//                case U:
//                    currentW--;
//                    if (currentW < 1)
//                        currentW = 1;
//                    else
//                        generateCubes();
//                    break;

                case I:
                    if (maze[currentX][currentY][currentZ + 1][currentT])
                        currentZ += 2;
                    generateCubes();
                    break;
                case K:
                    if (maze[currentX][currentY][currentZ - 1][currentT])
                        currentZ -= 2;
                    generateCubes();
                    break;
                case J:
                    if (maze[currentX-1][currentY][currentZ][currentT])
                        currentX -= 2;
                    generateCubes();
                    break;
                case L:
                    if (maze[currentX+1][currentY][currentZ][currentT])
                        currentX += 2;
                    generateCubes();
                    break;
                case U:
                    if (maze[currentX][currentY-1][currentZ][currentT])
                        currentY -= 2;
                    generateCubes();
                    break;
                case O:
                    if (maze[currentX][currentY+1][currentZ][currentT])
                        currentY += 2;
                    generateCubes();
                    break;
                case SEMICOLON:
                    if (maze[currentX][currentY][currentZ][currentT-1])
                        currentT -= 2;
                    generateCubes();
                    break;
                case P:
                    if (maze[currentX][currentY][currentZ][currentT+1])
                        currentT += 2;
                    generateCubes();
                    break;
                case F:
                    currentW--;
                    if (currentW < 1)
                        currentW = 1;
                    else
                        generateCubes();
                    break;
                case R:
                    currentW++;
                    if (currentW >= time - 1)
                        currentW = time - 2;
                    else
                        generateCubes();
                    break;

                default:
                    break;
            }
        });

        canvas.setOnKeyReleased(e -> {
            switch (e.getCode()) {
                case W:
                    kW = false;
                    break;
                case A:
                    kA = false;
                    break;
                case S:
                    kS = false;
                    break;
                case D:
                    kD = false;
                    break;
                case E:
                    kE = false;
                    break;
                case Q:
                    kQ = false;
                    break;
                case SPACE:
                    kSPACE = false;
                    break;
                case I:
                    kI = false;
                    break;
                case J:
                    kJ = false;
                    break;
                case K:
                    kK = false;
                    break;
                case L:
                    kL = false;
                    break;
                default:
                    break;
            }
        });

        AnimationTimer timer = new AnimationTimer() {

            long lt = 0;

            @Override
            public void handle(long dt) {

                if (lt > 0) {

                    double deltaTime = (dt - lt) / 1000000000.0;

                    double mx = 0;
                    double my = 0;
                    double mz = 0;

                    if (kW) {
                        mz += cos(cameraY);
                        mx += sin(cameraY);
                    }
                    else if (kS) {
                        mz -= cos(cameraY);
                        mx -= sin(cameraY);
                    }
                    if (kD) {
                        mx += cos(cameraY);
                        mz -= sin(cameraY);
                    }
                    else if (kA) {
                        mx -= cos(cameraY);
                        mz += sin(cameraY);
                    }
                    if (kE) {
                        my += 1;
                    }
                    else if (kQ) {
                        my -= 1;
                    }
                    boolean changed = false;
                    double h = Math.hypot(mx, mz);
                    if (h > 0.01) {
                        mx /= h;
                        mz /= h;
                        playerX += mx * deltaTime * 6;
                        playerZ += mz * deltaTime * 6;
                        changed = true;
                    }
                    if (Math.abs(my) > 0.01) {
                        playerY += my * deltaTime * 6;
                        changed = true;
                    }

                    if (kI)
                        cameraX += deltaTime * lookSpeed;
                    if (kK)
                        cameraX -= deltaTime * lookSpeed;
                    if (kJ)
                        cameraY -= deltaTime * lookSpeed;
                    if (kL)
                        cameraY += deltaTime * lookSpeed;


                    if (changed) {
                        cubes.sort(new Comparator<Cube>() {
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
                    }

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
                    facing = index;
                    renderCubes();
//    	    		lastFPS = lastFPS + 0.25 * (1.0 / deltaTime - lastFPS);
//    	    		gc.fillText("FPS " + Math.round(lastFPS), 20, 20);
                }

                lt = dt;

            }

        };

        timer.start();

        start();
    }

    public static void start() {

        boolean[][][][] m = Maze.recursiveBacktracking(width, height, depth, time, 0, 0, 0, 0);
        width = width * 2 + 1;
        height = height * 2 + 1;
        depth = depth * 2 + 1;
        time = time * 2 + 1;

//        boolean[][][] b = new boolean[width][height][depth];
//
//        for (int i = 1; i < width; i++) {
//            for (int j = 1; j < height; j++) {
//                for (int k = 1; k < depth; k++) {
//                    if (maze[i][j][k]) {
//                        b[i][j][k] = true;
//                    }
//                }
//            }
//        }
        maze = m;
        generateCubes();
    }

    public static void generateCubes() {
        cubes.clear();
        if (currentX == lX && currentY == lY && currentZ == lZ && currentT == lW) {
            maze = Maze.recursiveBacktracking((width-1)/2, (height-1)/2, (depth-1)/2, (time-1)/2, 0, 0, 0, 0);
            currentT = 1;
            currentX = 1;
            currentY = 1;
            currentZ = 1;
        }
        boolean[][][][] b = maze;
        if (currentW == currentT) {
            boolean[] xD = {true, true, true, true, true, true};
            cubes.add(new Cube(currentX * 3 - 0.5, currentY * 3 - 0.5, currentZ * 3 - 0.5, (short) 1, xD, 3, 3, 3, Color.rgb(0, 255, 0, 0.5)));
        }
        if (currentW == lW) {
            boolean[] xD = {true, true, true, true, true, true};
            cubes.add(new Cube(lX * 3 - 0.5, lY * 3 - 0.5, lZ * 3 - 0.5, (short) 1, xD, 3, 3, 3, Color.rgb(0, 0, 255, 0.5)));
        }
        for (int x = 1; x < b.length - 1; x++) {
            for (int y = 1; y < b[0].length - 1; y++) {
                for (int z = 1; z < b[0][0].length - 1; z++) {
                    if (b[x][y][z][currentW]) {
                        boolean[] n = new boolean[6];
                        if (x%2==1 && y%2==1 && z%2==1) {
                            n[0] = true;
                            n[1] = true;
                            n[2] = true;
                            n[3] = true;
                            n[4] = true;
                            n[5] = true;
                            cubes.add(new Cube(x * 3, y * 3, z * 3, (short) 1, n, 2, 2, 2, Color.rgb(255,0,0)));
                            if (maze[x][y][z][currentW + 1]) {
                                cubes.add(new Cube(x * 3 + 2, y * 3 + 2, z * 3 + 2, (short) 1, n, 1, 1, 1, Color.rgb(255,0,255)));
                            }
                            if (maze[x][y][z][currentW - 1]) {
                                cubes.add(new Cube(x * 3 - 1, y * 3 - 1, z * 3 - 1, (short) 1, n, 1, 1, 1, Color.rgb(255,0,255)));
                            }
                        } else {
                            boolean fx = true;
                            boolean fy = true;
                            boolean fz = true;
                            if (!b[x-1][y][z][currentW]) {
                                n[2] = true;
                                fx = false;
                            }
                            if (!b[x+1][y][z][currentW]) {
                                n[3] = true;
                                fx = false;
                            }
                            if (!b[x][y-1][z][currentW]) {
                                n[0] = true;
                                fy = false;
                            }
                            if (!b[x][y+1][z][currentW]) {
                                n[1] = true;
                                fy = false;
                            }
                            if (!b[x][y][z-1][currentW]) {
                                n[4] = true;
                                fz = false;
                            }
                            if (!b[x][y][z+1][currentW]) {
                                n[5] = true;
                                fz = false;
                            }
                            cubes.add(new Cube(x * 3 - (fx ? 1 : -0.5), y * 3 - (fy ? 1 : -0.5), z * 3 - (fz ? 1 : -0.5), (short) 2, n, fx ? 4 : 1, fy ? 4 : 1, fz ? 4 : 1, Color.rgb(150,0,0)));
                        }
                    }
                    // TODO: only render shown sides
//                    boolean[] n = new boolean[6];
//                    for (int l = 0; l < 6; l++) {
//                        if (n[l]) {
//                            cubes.add(new Cube(x, y, z,(short)1, n));
//                            break;
//                        }
//                    }
                }
            }
        }
        cubes.sort(new Comparator<Cube>() {
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
    }

    public static void renderCubes() {

        gc.clearRect(0, 0, WIDTH, HEIGHT);
        cubes.forEach(c -> {
            c.drawCube();
        });

    }

    public static void main(String[] args) {
        launch(args);
    }

}
