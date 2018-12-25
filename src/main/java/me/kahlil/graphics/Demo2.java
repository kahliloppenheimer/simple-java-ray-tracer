package me.kahlil.graphics;

import java.util.concurrent.ExecutionException;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import me.kahlil.scene.ImmutableScene3D;
import me.kahlil.scene.Light3D;
import me.kahlil.scene.LinearTransformation;
import me.kahlil.scene.Material;
import me.kahlil.scene.Object3D;
import me.kahlil.scene.Plane3D;
import me.kahlil.scene.Scene3D;
import me.kahlil.scene.Sphere3D;
import me.kahlil.scene.Vector;

/**
 * A second demo of the ray tracer.
 */
public class Demo2 {

    private static final int NUM_THREADS = 4;

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        Camera3D camera = ImmutableCamera3D.builder()
            .setLocation(new Vector(0, 0, 0))
            .build();
        SimpleFrame3D frame = new SimpleFrame3D(new Vector(-1, -1, -1), 2, 2, 800, 800);

        // Objects in scene
        List<Object3D> objects = new ArrayList<Object3D>();
        Sphere3D sphere1 = new Sphere3D(new Vector(-.5, 1, -3), .4, null, new Material(Color.blue, 10, 1))
                .transform(LinearTransformation
                    .scale(1, 1, 1).compose(LinearTransformation.translate(-1, 0, 0)));
        Sphere3D sphere2 = new Sphere3D(new Vector(1.5, .8, -2), .9, null, new Material(Color.red, 10, 1))
                .transform(LinearTransformation.scale(1, 1, 1).compose(LinearTransformation.translate(-1, 0, 0)));
        Sphere3D sphere3 = new Sphere3D(new Vector(2.5, 1, -4), .3, null, new Material(Color.green, 10, 1))
                .transform(LinearTransformation.scale(1, 1, 1).compose(LinearTransformation.translate(-1, 0, 0)));
//        objects.add(new Plane3D(new Vector(0, -1, 0), new Vector(0, 1, 0), new Material(Color.GREEN, 1, .25)));
//        objects.add(new Sphere3D(new Vector(-3, 0, -8), 1, null, new Material(Color.pink, 10, .7)));
        objects.add(sphere1);
        objects.add(sphere2);
        objects.add(sphere3);
        objects.add(new Plane3D(new Vector(0, 0, -5), new Vector(0, 1, 1), new Material(Color.blue, 10, .1)));

        // Lights in scene
        List<Light3D> lights = new ArrayList<>();
        lights.add(new Light3D(new Vector(3, 3, 0), new Color(115, 115, 115)));
        lights.add(new Light3D(new Vector(-6, 5, 0), new Color(200, 200, 200)));

        // Whole scene
        Scene3D scene = ImmutableScene3D.builder()
            .setObjects(objects)
            .setLights(lights)
            .setBackgroundColor(Color.BLACK)
            .setAmbient(new Color((float) .075, (float) .075, (float) .075))
            .build();

        RayTracerCoordinator rt = new RayTracerCoordinator(frame, camera, scene);

        long start = System.currentTimeMillis();
        SimpleFrame3D rendered = rt.render(true, NUM_THREADS);
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
