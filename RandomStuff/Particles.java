package RandomStuff;

import Generators.Maze;

import java.util.Random;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.xml.soap.Text;

import javafx.geometry.VPos;
import javafx.scene.image.WritableImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Particles extends Application {

    /*
    Keys:
        r = reset
        f = freeze
     */

    // Diameter
    final int r = 12, steps = 35;
    final double dt = 0.005;
    final double g = 1, bounce = 0.8;
    final double launchConstant = 0.4;

    int placeType = 0;
    double startX, startY, mouseX, mouseY, lastX, lastY;
    boolean mouseDown = false;

    int previewSteps = 35, previewDistance = 1;

    public int fastForward = 1;

    class Particle {

        double x, y, vx, vy;

        public Particle(double x, double y, double vx, double vy) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
        }

        public Particle (Particle obj) {
            this.x = obj.x;
            this.y = obj.y;
            this.vx = obj.vx;
            this.vy = obj.vy;
        }

    }

    class Bounce {
        Particle p1, p2;
        double vx1, vy1, m1, m2;
    }

    GraphicsContext gc;

    ArrayList<Particle> parts = new ArrayList<>();
    ArrayList<Bounce> bounces = new ArrayList<Bounce>();
    ArrayList<Particle> previewParts = new ArrayList<>(), previewPartsClone = new ArrayList<>();
    ArrayList<Particle> perm = new ArrayList<>();

    double returnX, returnY;

    Particle part;

    public void AddPart(double x, double y, double vx, double vy) {
        parts.add(new Particle(x, y, vx, vy));
    }

    void CloneParts(ArrayList<Particle> p1, ArrayList<Particle> p2) {
        p1.clear();
        for (Particle p : p2) {
            p1.add(new Particle(p.x, p.y, p.vx, p.vy));
        }
    }

    public void Draw() {

        gc.setStroke(Color.WHITE);

        if (mouseDown) {
            /*
            gc.setStroke(Color.WHITE);
            gc.strokeLine(startX, startY, startX * 2 - mouseX, startY * 2 - mouseY);

            gc.setFill(Color.rgb(255,255,255,0.4));
            gc.fillRoundRect(startX - r / 2, startY - r / 2, r, r, r, r);

            double x = startX, y = startY, vx = (startX - mouseX) * launchConstant, vy = (startY - mouseY) * launchConstant, lastX = startX, lastY = startY;
            for (int i = 0; i < previewDistance; i++) {
                for (int j = 0; j < previewSteps; j++) {
                    GetVelocity(x, y, vx, vy);
                    vx = returnX;
                    vy = returnY;
                    x += vx * dt;
                    y += vy * dt;
                }
                gc.strokeLine(lastX, lastY, x, y);
                lastX = x;
                lastY = y;
            }
            */

                //CloneParts(previewPartsClone, parts);
                //part = new Particle(startX, startY, (startX - mouseX) * launchConstant, (startY - mouseY) * launchConstant);
                //previewPartsClone.add(part);
            //CloneParts(previewParts, parts);
            //part = new Particle(startX, startY, (startX - mouseX) * launchConstant, (startY - mouseY) * launchConstant);
            //previewParts.add(part);

            for (int s = 0; s < previewDistance; s++) {
                for (int i = 0; i < previewSteps * fastForward; i++) {
                    Physics(previewParts);
                }
                part = previewParts.get(previewParts.size() - 1);
                gc.strokeLine(lastX, lastY, part.x, part.y);
                lastX = part.x;
                lastY = part.y;
                for (Particle p : previewParts) {
                    gc.setFill(Color.rgb(255, 255,255,.2));
                    gc.fillRoundRect(p.x - r / 4, p.y - r / 4, r / 2, r / 2, r, r);
                }
                Particle p = previewParts.get(previewParts.size() - 1);
                gc.setFill(Color.rgb(100, 255,100,.4));
                gc.fillRoundRect(p.x - r / 4, p.y - r / 4, r / 2, r / 2, r, r);
            }
        }
        else {
            gc.clearRect(0, 0, 800, 800);
        }

        gc.setFill(Color.rgb(255,255,255,1));
        for (Particle p : parts) {
            gc.fillRoundRect(p.x - r / 2, p.y - r / 2, r, r, r, r);
        }
    }

    void GetVelocity(double px, double py, double vx, double vy, ArrayList<Particle> partz, int id) {
        double dx1 = px - 400, dy1 = py - 400;
        double m1 = Math.sqrt(dx1 * dx1 + dy1 * dy1);
        if (m1 > 0) {
            dx1 /= m1;
            dy1 /= m1;

            double c = g / m1 * m1 * 2 * dt;

            vx -= c * dx1;
            vy -= c * dy1;
        }

        //int i = 0;
        for (Particle x : partz) {
            //if (ind > i) {
                double dx = px - x.x, dy = py - x.y;
                double m = Math.sqrt(dx * dx + dy * dy);
                if (m > 0.001f) {
                    dx /= m;
                    dy /= m;

                    double c = g / m * m * dt;

                    vx -= c * dx;
                    vy -= c * dy;

                    if (m < r) {
                        //double vm = Math.sqrt(vx * vx + vy * vy);
                        //vx = vm * dx * bounce;
                        //vy = vm * dy * bounce;
                        /*
                        double vx1 = x.vx, vy1 = x.vy;
                        x.vx = vx;
                        x.vy = vy;
                        vx = vx1;
                        vy = vy1;
                        break;
                        */

                        boolean none = true;
                        for (int l = 0; l < bounces.size(); l++) {
                            if (bounces.get(l).p1.equals(x)) {
                                none = false;
                                break;
                            }
                        }

                        if (none) {
                            Bounce bounce = new Bounce();
                            bounce.p1 = partz.get(id);
                            bounce.p2 = x;
                            bounce.vx1 = dx;
                            bounce.vy1 = dy;
                            bounce.m1 = m;
                            bounces.add(bounce);
                        }
                    }
                }
            //}
            //i++;
        }
        returnX = vx;
        returnY = vy;
    }

    public void Physics(ArrayList<Particle> partz) {

        bounces.clear();
        // Initial physics (gravity)
        int idk = 0;
        for (Particle p : partz) {
            GetVelocity(p.x, p.y, p.vx, p.vy, partz, idk++);
            p.vx = returnX;
            p.vy = returnY;
        }

        for (Bounce b : bounces) {
            /*
            double vx1 = bounces.get(i).vx, vy1 = bounces.get(i).vy;
            bounces.get(i).vx = bounces.get(i + 1).vx;
            bounces.get(i).vy = bounces.get(i + 1).vy;
            bounces.get(i + 1).vx = vx1;
            bounces.get(i + 1).vy = vy1;
            break;
            */
            b.m2 = Math.sqrt(b.p2.vx * b.p2.vx + b.p2.vy * b.p2.vy) * bounce;
            b.m1 *= bounce;
            b.p1.vx = b.m2 * b.vx1;
            b.p1.vy = b.m2 * b.vy1;
            b.p2.vx = -b.m1 * b.vx1;
            b.p2.vy = -b.m1 * b.vy1;
        }

        // After physics (prevent collisions)
        for (Particle p : partz) {
            p.x += p.vx * dt;
            p.y += p.vy * dt;


            for (Particle x : partz) {
                double dx = p.x - x.x, dy = p.y - x.y;
                double m = Math.sqrt(dx * dx + dy * dy);
                if (m > 0) {
                    dx /= m;
                    dy /= m;

                    if (m < r) {
                        p.x += dx * (r - m);
                        p.y += dy * (r - m);
                    }
                }
            }

        }
    }

    /*
    public void Physics() {

        // Initial physics (gravity)
        for (Particle p : parts) {
            double dx1 = p.x - 400, dy1 = p.y - 400;
            double m1 = Math.sqrt(dx1 * dx1 + dy1 * dy1);
            if (m1 > 0) {
                dx1 /= m1;
                dy1 /= m1;

                double c = g / m1 * m1 * 2 * dt;

                p.vx -= c * dx1;
                p.vy -= c * dy1;
            }

            for (Particle x : parts) {
                double dx = p.x - x.x, dy = p.y - x.y;
                double m = Math.sqrt(dx * dx + dy * dy);
                if (m > 0) {
                    dx /= m;
                    dy /= m;

                    double c = g / m * m * dt;

                    p.vx -= c * dx;
                    p.vy -= c * dy;

                    if (m < r) {
                        double vm = Math.sqrt(p.vx * p.vx + p.vy * p.vy);
                        p.vx = vm * dx * bounce;
                        p.vy = vm * dy * bounce;
                    }
                }
            }
        }

        // After physics (prevent collisions)
        for (Particle p : parts) {
            p.x += p.vx * dt;
            p.y += p.vy * dt;


            for (Particle x : parts) {
                double dx = p.x - x.x, dy = p.y - x.y;
                double m = Math.sqrt(dx * dx + dy * dy);
                if (m > 0) {
                    dx /= m;
                    dy /= m;

                    if (m < r) {
                        p.x += dx * (r - m);
                        p.y += dy * (r - m);
                    }
                }
            }

        }
    }
    */

    @Override
    public void start(Stage primaryStage) {

        Group root = new Group();
        Scene s = new Scene(root, 800, 800, Color.BLACK);

        final Canvas canvas = new Canvas(800, 800);
        canvas.setFocusTraversable(true);
        gc = canvas.getGraphicsContext2D();

        root.getChildren().add(canvas);

        primaryStage.initStyle(StageStyle.UNIFIED);
        primaryStage.setTitle("Particles");
        primaryStage.setScene(s);
        primaryStage.setResizable(false);
        primaryStage.show();

        canvas.setOnMousePressed(e -> {
            mouseDown = true;
            startX = e.getX();
            startY = e.getY();
            mouseX = e.getX();
            mouseY = e.getY();
            CloneParts(previewParts, parts);
            part = new Particle(startX, startY, (startX - mouseX) * launchConstant, (startY - mouseY) * launchConstant);
            previewParts.add(part);
            gc.clearRect(0, 0, 800, 800);
            lastX = startX;
            lastY = startY;
        });

        canvas.setOnMouseReleased(e -> {
            mouseDown = false;
            AddPart(startX, startY, (startX - mouseX) * launchConstant, (startY - mouseY) * launchConstant);
        });

        canvas.setOnMouseDragged(e -> {
            if (!e.isShiftDown()) {
                mouseX = e.getX();
                mouseY = e.getY();
                CloneParts(previewParts, parts);
                part = new Particle(startX, startY, (startX - mouseX) * launchConstant, (startY - mouseY) * launchConstant);
                previewParts.add(part);
                gc.clearRect(0, 0, 800, 800);
                lastX = startX;
                lastY = startY;
            }
        });

        canvas.setOnKeyPressed(e -> {
            switch(e.getCode()) {
                case R:
                    parts.clear();
                    break;
                case O:
                    fastForward *= 2;
                    break;
                case I:
                    fastForward /= 2;
                    if (fastForward < 1) {
                        fastForward = 1;
                    }
                    break;
            }
        });

        new AnimationTimer() {

            @Override
            public void handle(long dt) {
                if (!mouseDown) {
                    for (int i = 0; i < steps * fastForward; i++) {
                        bounces.clear();
                        Physics(parts);
                    }
                }
                Draw();
            }

        }.start();

        Draw();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
