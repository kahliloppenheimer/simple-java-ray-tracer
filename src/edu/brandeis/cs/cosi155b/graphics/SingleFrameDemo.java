package edu.brandeis.cs.cosi155b.graphics;
import edu.brandeis.cs.cosi155b.scene.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kahliloppenheimer on 9/5/15.
 */
public class SingleFrameDemo {

    private static final int ANTI_ALIASING = 1;
    private static final int NUM_THREADS = 4;

    public static void main(String[] args) throws InterruptedException {

        Camera3D camera = new Camera3D(new Vector(0, 0, 0));
        SimpleFrame3D frame = new SimpleFrame3D(new Vector(-.5, -.5, -1), 1, 1, 800, 800);

        // Objects in scene
        List<Object3D> objects = new ArrayList<Object3D>();
        Sphere3D sphere1 = new Sphere3D(new Vector(1, 1, -5), 1, null, new Material(Color.red, 10, 1))
                .transform(LinearTransformation.translate(-1, -1, 0));
        objects.add(new Plane3D(new Vector(0, -1, 0), new Vector(0, 1, 0), new Material(Color.GREEN, 1, .25)));
        objects.add(new Sphere3D(new Vector(-3, 0, -8), 1, null, new Material(Color.pink, 10, .7)));
        objects.add(sphere1);

        // Lights in scene
        List<Light3D> lights = new ArrayList<>();
        lights.add(new Light3D(new Vector(3, 3, 0), new Color(115, 115, 115)));
        lights.add(new Light3D(new Vector(-6, 5, 0), new Color(200, 200, 200)));

        // Whole scene
        Scene3D scene = new Scene3D(objects, lights, Color.BLACK, new Color((float) .075, (float) .075, (float) .075));

        RayTracer rt = new RayTracer(frame, camera, scene);

        long start = System.currentTimeMillis();
        SimpleFrame3D rendered = rt.render(ANTI_ALIASING, NUM_THREADS);
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
