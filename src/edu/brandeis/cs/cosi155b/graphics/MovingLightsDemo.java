package edu.brandeis.cs.cosi155b.graphics;

import edu.brandeis.cs.cosi155b.scene.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kahliloppenheimer on 9/4/15.
 */
public class MovingLightsDemo {

    private static Scene3D sampleScene = new Scene3D(
            Color.BLACK,
            new Color((float) .05, (float) .05, (float) .05),
            new Sphere3D(new Point3D(2, -.5, -5), .5, null, new Material(Color.RED, 0)),
            new Sphere3D(new Point3D(-1, .5, -2), .5, null, new Material(Color.YELLOW, 0)),
            new Sphere3D(new Point3D(-.5, -1.5, -2), .5, null, new Material(Color.PINK, 0)),
            new Sphere3D(new Point3D(-1, 3, -4), .5, null, new Material(Color.CYAN, 0)));
    private static final int FRAMES_TO_RENDER = 60;

    public static void main(String[] args) throws InterruptedException {
        List<SimpleFrame3D> toDisplay = new ArrayList<>();
        double radians = 0;
        SimpleFrame3D frame = new SimpleFrame3D(new Point3D(-1, -1, -1), 2, 2, 400, 400);
        Camera3D camera = new Camera3D(new Point3D(0, 0, 0), new Point3D(0, 0, -1));
        List<Light3D> lights = new ArrayList<>();
        lights.add(new Light3D(new Point3D(-2, -2.5, 1), Color.YELLOW));
        // lights.add(new Light3D(new Point3D(-1, -1, -1), .2));
        RayTracer rt = new RayTracer(frame, camera, sampleScene, lights);
        SimpleFrame3D rendered = rt.render();
        toDisplay.add(rendered);

        for(int i = 0; i < FRAMES_TO_RENDER; ++i) {
            System.out.println((i + 1) + " / " + FRAMES_TO_RENDER + " rendered");
            radians += 2 * Math.PI / FRAMES_TO_RENDER;

            Sphere3D sphere = (Sphere3D) sampleScene.remove(0);
            Sphere3D sphere2 = (Sphere3D) sampleScene.remove(0);
            Sphere3D sphere3 = (Sphere3D) sampleScene.remove(0);
            Sphere3D sphere4 = (Sphere3D) sampleScene.remove(0);
            sampleScene.add(0, sphere.translate(0, .3 * Math.cos(2 * radians) - sphere.getCenter().getY(),
                                                   .3 * Math.sin(2 * radians) - sphere.getCenter().getZ() - 3));
            sampleScene.add(1, sphere2.translate(.5 * Math.cos(.5 * radians) - sphere2.getCenter().getX() - 1,
                                                 .5 * Math.sin(.5 * radians) - sphere2.getCenter().getY(), 0));
            sampleScene.add(2, sphere3.translate(0, .02, .02));
            sampleScene.add(3, sphere4.translate(.6 * Math.sin(4 * radians) - sphere4.getCenter().getX(), -.1,
                                                 .6 * Math.cos(4 * radians) - sphere4.getCenter().getZ() - 5));
            /*
            sampleScene.add(2, new Sphere3D(new Point3D(sphere3.getCenter().getX() + Math.cos(radians), sphere3.getCenter().getY() + Math.sin(radius), 0),
                    sphere3.getRadius(),
                    sphere3.getInsideMaterial(),kjj
                    sphere3.getOutsideMaterial()));
                    */

            /*
            lights.remove(0);
            lights.add(new Light3D(new Point3D(-1, radius * Math.sin(.25 * radians), radius * Math.cos(.25 * radians)), 1));
            */

            rendered = rt.render();
            toDisplay.add(rendered);
        }

        Canvas3D canvas = display(toDisplay.remove(0));
        while(true) {
            System.out.println("Starting scene!");
            for (SimpleFrame3D f : toDisplay) {
                display(f, canvas);
            }
        }
    }

    private static Canvas3D display(SimpleFrame3D rendered) throws InterruptedException {
        Canvas3D canvas = new MyCanvas3D(rendered.getWidthPx(), rendered.getHeightPx());
        SwingUtilities.invokeLater(() -> createAndShowGUI((MyCanvas3D) canvas));
        Thread.sleep(1000);
        display(rendered, canvas);
        return canvas;
    }

    private static void display(SimpleFrame3D rendered, Canvas3D canvas) throws InterruptedException {
        for(int i = 0; i < rendered.getWidthPx(); ++i) {
            for(int j = 0; j < rendered.getHeightPx(); ++j) {
                canvas.drawPixel(i, j, rendered.getPixel(i, j).getColor());
            }
        }
        SwingUtilities.invokeLater( () -> canvas.refresh() );
        Thread.sleep(10);
    }

    /*
     * here we create a window, add the canvas,
     * set the window size and make it visible!
     */
    private static void createAndShowGUI(MyCanvas3D canvas) {

        JFrame f = new JFrame("PA01 Demo");

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(canvas);
        System.out.println("Width = " + canvas.getWidth() + "\theight = " + canvas.getHeight());
        f.setSize(canvas.getWidth(), canvas.getHeight());
        f.setVisible(true);
    }
}
