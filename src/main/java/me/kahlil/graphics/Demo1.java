package me.kahlil.graphics;

import java.util.concurrent.ExecutionException;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import me.kahlil.scene.ImmutableScene3D;
import me.kahlil.scene.Light3D;
import me.kahlil.scene.Material;
import me.kahlil.scene.Object3D;
import me.kahlil.scene.Plane3D;
import me.kahlil.scene.Scene3D;
import me.kahlil.scene.Vector;

/**
 * An initial demo of the ray tracer.
 */
public class Demo1 {
    private static final int ANTI_ALIASING = 4;
    private static final int NUM_THREADS = 4;

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        Camera3D camera = ImmutableCamera3D.builder()
            .setLocation(new Vector(0, 0, 0))
            .build();
        SimpleFrame3D frame = new SimpleFrame3D(new Vector(-.5, -.5, -1), 1, 1, 400, 400);

        // Objects in scene
        List<Object3D> objects = new ArrayList<>();
//        objects.add(new Sphere3D(new Vector(1, 1, -5), 1, null, new Material(Color.WHITE, 100, .2)));
        objects.add(new Plane3D(new Vector(0, -1, 0), new Vector(0, 1, 0), new Material(Color.WHITE, 1, .2)));
        objects.add(new Plane3D(new Vector(1.5, 0, 0), new Vector(-1, 0, 0), new Material(Color.WHITE, 1, .2)));

        // Lights in scene
        List<Light3D> lights = new ArrayList<>();
        lights.add(new Light3D(new Vector(-2, 1, 0), Color.WHITE));

        // Whole scene
        Scene3D scene = ImmutableScene3D.builder()
            .setObjects(objects)
            .setLights(lights)
            .setBackgroundColor(Color.BLUE)
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
