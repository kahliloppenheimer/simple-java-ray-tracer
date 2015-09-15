package edu.brandeis.cs.cosi155b.graphics;

import edu.brandeis.cs.cosi155b.scene.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kahliloppenheimer on 9/15/15.
 */
public class Pa1 {
    private static final int ANTI_ALIASING = 500;
    private static final int NUM_THREADS = 4;

    public static void main(String[] args) throws InterruptedException {

        Camera3D camera = new Camera3D(new Vector(0, 0, 0));
        SimpleFrame3D frame = new SimpleFrame3D(new Vector(-.5, -.5, -1), 1, 1, 400, 400);

        // Objects in scene
        List<Object3D> objects = new ArrayList<>();
        objects.add(new Sphere3D(new Vector(1, 1, -5), 1, null, new Material(Color.WHITE, 100, .2)));
        objects.add(new Plane3D(new Vector(0, -1, 0), new Vector(0, 1, 0), new Material(Color.WHITE, 1, .2)));
        objects.add(new Plane3D(new Vector(1.5, 0, 0), new Vector(-1, 0, 0), new Material(Color.WHITE, 1, .2)));

        // Lights in scene
        List<Light3D> lights = new ArrayList<>();
        lights.add(new Light3D(new Vector(-2, 1, 0), Color.WHITE));

        // Whole scene
        Scene3D scene = new Scene3D(objects, lights, Color.BLUE, new Color((float) .075, (float) .075, (float) .075));

        RayTracer rt = new RayTracer(frame, camera, scene);

        long start = System.currentTimeMillis();
        SimpleFrame3D rendered = rt.render(false, ANTI_ALIASING, NUM_THREADS);
        long end = System.currentTimeMillis();

        System.out.println("Rendering took " + (end - start) + " ms");

        MyCanvas3D canvas = new MyCanvas3D(rendered.getWidthPx(), rendered.getHeightPx());
        SwingUtilities.invokeLater(() -> canvas.createAndShowGUI());
        Thread.sleep(500);

        start = System.currentTimeMillis();
        canvas.paintFrame(rendered);
        end = System.currentTimeMillis();

        System.out.println("Painting took " + (end - start) + " ms");

        SwingUtilities.invokeLater(() -> canvas.refresh());
    }
}
