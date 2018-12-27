package me.kahlil.demos;

import static me.kahlil.graphics.Color.BLACK;
import static me.kahlil.graphics.Color.GREEN;
import static me.kahlil.graphics.Color.RED;
import static me.kahlil.graphics.Color.YELLOW;
import static me.kahlil.scene.Cameras.STANDARD_CAMERA;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingUtilities;
import me.kahlil.geometry.BoundedPlane3D;
import me.kahlil.geometry.LinearTransformation;
import me.kahlil.geometry.Object3D;
import me.kahlil.geometry.Plane3D;
import me.kahlil.geometry.Sphere3D;
import me.kahlil.geometry.Vector;
import me.kahlil.graphics.Color;
import me.kahlil.graphics.MyCanvas3D;
import me.kahlil.graphics.RayTracerCoordinator;
import me.kahlil.scene.ImmutableMaterial;
import me.kahlil.scene.ImmutableScene;
import me.kahlil.scene.Light3D;
import me.kahlil.scene.Scene;
import me.kahlil.scene.SimpleFrame;

/** A second demo of the ray tracer. */
public class Demo3 {

  public static void main(String[] args) throws InterruptedException, ExecutionException {

    SimpleFrame frame = new SimpleFrame(new Vector(-1, -1, -1), 2, 2, 400, 400);

    // Objects in scene
    Sphere3D sphere1 =
        new Sphere3D(
                new Vector(-.5, 1, -3),
                .4,
                null,
                ImmutableMaterial.builder()
                    .setColor(YELLOW)
                    .setHardness(10)
                    .setSpecularIntensity(1.0)
                    .build())
            .transform(
                LinearTransformation.scale(1, 1, 1)
                    .compose(LinearTransformation.translate(-1, 0, 0)));
    Sphere3D sphere2 =
        new Sphere3D(
                new Vector(.8, .8, -5),
                .6,
                null,
                ImmutableMaterial.builder()
                    .setColor(RED)
                    .setHardness(10)
                    .setSpecularIntensity(1.0)
                    .setReflective(true)
                    .build())
            .transform(
                LinearTransformation.scale(1, 1, 1)
                    .compose(LinearTransformation.translate(-1, 0, 0)));
    Sphere3D sphere3 =
        new Sphere3D(
                new Vector(-2, 2, 0),
                .3,
                null,
                ImmutableMaterial.builder()
                    .setColor(GREEN)
                    .setHardness(10)
                    .setSpecularIntensity(1.0)
                    .build())
            .transform(LinearTransformation.translate(2, -2, 0));
    Plane3D basePlane =
        new Plane3D(
            new Vector(-2, -2, -15),
            new Vector(0, 1, -1),
            ImmutableMaterial.builder()
                .setColor(BLACK)
                .setHardness(10)
                .setSpecularIntensity(0.1)
                .build());

    Plane3D leftSidePlane =
        new BoundedPlane3D(
            new Vector(-5, 0, 0),
            new Vector(-10, 0, 1),
            new Vector(-20, -1, -20),
            new Vector(5, 3, 1),
            ImmutableMaterial.builder()
                .setSpecularIntensity(0.5)
                .setHardness(100)
                .setColor(YELLOW)
                .build());

    Plane3D rightSidePlane =
        new BoundedPlane3D(
            new Vector(5, 0, 0),
            new Vector(-10, 0, 1),
            new Vector(-1, -1, -20),
            new Vector(5, 3, 1),
            ImmutableMaterial.builder()
                .setSpecularIntensity(0.5)
                .setHardness(100)
                .setColor(BLACK)
                .setReflective(true)
                .build());

    ImmutableList<Object3D> objects =
        ImmutableList.of(leftSidePlane, rightSidePlane, basePlane, sphere3);

    // Lights in scene
    List<Light3D> lights = new ArrayList<>();
    lights.add(new Light3D(new Vector(3, 3, 0), new Color(115, 115, 115)));
    lights.add(new Light3D(new Vector(-6, 5, 0), new Color(200, 200, 200)));

    // Whole scene
    Scene scene =
        ImmutableScene.builder()
            .setObjects(objects)
            .setLights(lights)
            .setBackgroundColor(BLACK)
            .setAmbient(new Color((float) .075, (float) .075, (float) .075))
            .build();

    RayTracerCoordinator rt = new RayTracerCoordinator(frame, STANDARD_CAMERA, scene);

    long start = System.currentTimeMillis();
    SimpleFrame rendered = rt.render(true);
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
