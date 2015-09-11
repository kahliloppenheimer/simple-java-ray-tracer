package edu.brandeis.cs.cosi155b.graphics;
import edu.brandeis.cs.cosi155b.scene.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kahliloppenheimer on 9/5/15.
 */
public class SingleFrameDemo {

    private static final int ANTI_ALIASING = 8;

    public static void main(String[] args) throws InterruptedException {

        Camera3D camera = new Camera3D(new Point3D(0, 0, 0));
        SimpleFrame3D frame = new SimpleFrame3D(new Point3D(-.5, -.5, -1), 1, 1, 800, 800);

        // Objects in scene
        List<Object3D> objects = new ArrayList<Object3D>();
        objects.add(new Sphere3D(new Point3D(1, 1, -5), 1, null, new Material(Color.red, 50, .25)));
        objects.add(new Plane3D(new Point3D(0, -1, 0), new Point3D(0, 1, 0), new Material(Color.GREEN, 1, .25)));
        objects.add(new Sphere3D(new Point3D(-3, 0, -8), 1, null, new Material(Color.pink, 10, .4)));

        // Lights in scene
        List<Light3D> lights = new ArrayList<>();
        lights.add(new Light3D(new Point3D(2, 1, 0), Color.WHITE));

        // Whole scene
        Scene3D scene = new Scene3D(objects, lights, Color.BLACK, new Color((float) .05, (float) .05, (float) .05));

        RayTracer rt = new RayTracer(frame, camera, scene);
        SimpleFrame3D rendered = rt.render(ANTI_ALIASING);
        MyCanvas3D canvas = new MyCanvas3D(rendered.getWidthPx(), rendered.getHeightPx());
        SwingUtilities.invokeLater(() -> canvas.createAndShowGUI());
        Thread.sleep(500);
        canvas.paintFrame(rendered);
        SwingUtilities.invokeLater(() -> canvas.refresh());
        /*
        SimpleFrame3D frame = new SimpleFrame3D(new Point3D(-1, -1, -1), 2, 2, 400, 400);
        Camera3D camera = new Camera3D(new Point3D(0, 0, 0));
        List<Light3D> lights = new ArrayList<>();
//        lights.add(new Light3D(new Point3D(-2, -2.5, 1), Color.YELLOW));
        lights.add(new Light3D(new Point3D(2, 2.5, -3), Color.WHITE));
//         lights.add(new Light3D(new Point3D(-1, -1, -1), .2));
        RayTracer rt = new RayTracer(frame, camera, sampleScene, lights);
        MovingLightsDemo.display(rt.render(64));
        */
    }
}
