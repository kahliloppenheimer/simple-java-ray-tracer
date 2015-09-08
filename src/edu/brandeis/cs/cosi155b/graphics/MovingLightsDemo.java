package edu.brandeis.cs.cosi155b.graphics;

import edu.brandeis.cs.cosi155b.scene.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kahliloppenheimer on 9/4/15.
 */
public class MovingLightsDemo {
    private static final int FRAMES_TO_RENDER = 1;
    private static final int ANTI_ALIASING_FACTOR = 32;
    private static final int FPS = 200;

    private static Scene3D sampleScene = new Scene3D(
            Color.BLACK,
            new Color((float) .05, (float) .05, (float) .05),
            new Sphere3D(new Point3D(2, -.5, -5), .5, null, new Material(Color.RED, 1)),
            new Sphere3D(new Point3D(-1, .5, -2), .5, null, new Material(Color.YELLOW, 1)),
            new Sphere3D(new Point3D(-.5, -.5, -2), .5, null, new Material(Color.WHITE, 1)),
            new Sphere3D(new Point3D(-1, 3, -4), .5, null, new Material(Color.CYAN, 1)),
            new Sphere3D(new Point3D(1, 1, -5), 1, null, new Material(Color.GREEN, 1)));

    public static void main(String[] args) throws InterruptedException {
        /*
        List<SimpleFrame3D> toDisplay = new ArrayList<>();
        double radians = 0;
        SimpleFrame3D frame = new SimpleFrame3D(new Point3D(-.5, -.5, -1), 1, 1, 800, 800);
        Camera3D camera = new Camera3D(new Point3D(0, 0, 0));
        List<Light3D> lights = new ArrayList<>();
//        lights.add(new Light3D(new Point3D(-2, -2.5, 1), Color.YELLOW));
        lights.add(new Light3D(new Point3D(2, 1, 0), Color.magenta));
        lights.add(new Light3D(new Point3D(-2, 1, 0), Color.YELLOW));
//         lights.add(new Light3D(new Point3D(-1, -1, -1), .2));
        RayTracer rt = new RayTracer(frame, camera, sampleScene, lights);
        SimpleFrame3D rendered = rt.render(ANTI_ALIASING_FACTOR);
        toDisplay.add(rendered);

        for(int i = 0; i < FRAMES_TO_RENDER; ++i) {
            System.out.println((i + 1) + " / " + FRAMES_TO_RENDER + " rendered");
            radians += 2 * Math.PI / FRAMES_TO_RENDER;

            /*
            sampleScene.get(0).setLocation(0, .3 * Math.cos(2 * radians),
                                                   .3 * Math.sin(2 * radians));
            sampleScene.get(1).setLocation(.5 * Math.cos(.5 * radians),
                                                 .5 * Math.sin(.5 * radians), 0);
            sampleScene.get(2).setLocation(0, .02, .02);
            sampleScene.get(3).setLocation(.6 * Math.sin(4 * radians), -.1,
                                                 .6 * Math.cos(4 * radians));
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

//            lights.get(0).setLocation(lights.get(0).getLocation().translate(0, .3 * Math.sin(.03 * radians), .3 * Math.cos(.03 * radians)));
//            lights.get(1).setLocation(lights.get(1).getLocation().translate(0, .3 * Math.cos(.5 * radians), .3 * Math.sin(.5 * radians)));
        /*
            rendered = rt.render(ANTI_ALIASING_FACTOR);
            toDisplay.add(rendered);
        }

        Canvas3D canvas = display(toDisplay.remove(0));

        /*
        while(true) {
            ++counter;
            System.out.println("Scene repeat: " + counter);
            for (SimpleFrame3D f : toDisplay) {
                display(f, canvas);
            }
        }
        */
    }


}
