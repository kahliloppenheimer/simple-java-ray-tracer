package me.kahlil.demos;

import static me.kahlil.graphics.Color.WHITE;
import static me.kahlil.scene.Cameras.STANDARD_CAMERA;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.*;
import me.kahlil.geometry.Object3D;
import me.kahlil.geometry.Plane3D;
import me.kahlil.geometry.Vector;
import me.kahlil.graphics.Color;
import me.kahlil.graphics.MyCanvas3D;
import me.kahlil.graphics.RayTracerCoordinator;
import me.kahlil.scene.*;

/** An initial demo of the ray tracer. */
public class Demo1 {

  public static void main(String[] args) throws InterruptedException, ExecutionException {

    Raster frame = new Raster(400, 400);

    // Objects in scene
    List<Object3D> objects = new ArrayList<>();
    objects.add(
        new Plane3D(
            new Vector(0, -1, 0),
            new Vector(0, 1, 0),
            ImmutableMaterial.builder()
                .setColor(WHITE)
                .setHardness(1)
                .setSpecularIntensity(0.2)
                .build()));
    objects.add(
        new Plane3D(
            new Vector(1.5, 0, 0),
            new Vector(-1, 0, 0),
            ImmutableMaterial.builder()
                .setColor(WHITE)
                .setHardness(1)
                .setSpecularIntensity(0.2)
                .build()));

    // Lights in scene
    List<Light3D> lights = new ArrayList<>();
    lights.add(new Light3D(new Vector(-2, 1, 0), WHITE));

    // Whole scene
    Scene scene =
        ImmutableScene.builder()
            .setObjects(objects)
            .setLights(lights)
            .setBackgroundColor(Color.BLUE)
            .setAmbient(new Color((float) .075, (float) .075, (float) .075))
            .build();

    RayTracerCoordinator rt = new RayTracerCoordinator(frame, STANDARD_CAMERA, scene);

    long start = System.currentTimeMillis();
    Raster rendered = rt.render(true);
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
