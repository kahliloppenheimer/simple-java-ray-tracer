package edu.brandeis.cs.cosi155b.graphics;

import edu.brandeis.cs.cosi155b.scene.*;
import javafx.scene.Camera;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kahliloppenheimer on 9/5/15.
 */
public class SingleFrameDemo {

    private static Scene3D sampleScene = new Scene3D(
            Color.BLACK,
            new Color((float) .05, (float) .05, (float) .05),
            new Sphere3D(new Point3D(2, -.5, -5), .5, null, new Material(Color.RED, 0, 1.0)),
            new Sphere3D(new Point3D(-1, .5, -2), .5, null, new Material(Color.YELLOW, 0, 1.0)),
            new Sphere3D(new Point3D(-.5, -1.5, -2), .5, null, new Material(Color.PINK, 0, 1.0)),
            new Sphere3D(new Point3D(-1, 3, -4), .5, null, new Material(Color.CYAN, 0, 1.0)));

    public static void main(String[] args) throws InterruptedException {
        SimpleFrame3D frame = new SimpleFrame3D(new Point3D(-.5, -.5, -1), 1, 1, 400, 400);
        Camera3D camera = new Camera3D(new Point3D(0, 0, 0));
        Scene3D scene = new Scene3D(Color.BLACK, new Color((float) .05, (float) .05, (float) .05), new Sphere3D(new Point3D(1, 1, -5), 1, null, new Material(Color.red, 0, 0)));
        List<Light3D> lights = new ArrayList<>();
        lights.add(new Light3D(new Point3D(2, 1, 0), Color.WHITE));
        RayTracer rt = new RayTracer(frame, camera, scene, lights);
        MovingLightsDemo.display(rt.render(32));
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
