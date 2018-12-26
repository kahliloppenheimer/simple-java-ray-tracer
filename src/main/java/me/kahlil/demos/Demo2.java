package me.kahlil.demos;

import static me.kahlil.graphics.Color.BLUE;
import static me.kahlil.graphics.Color.GREEN;
import static me.kahlil.graphics.Color.RED;
import static me.kahlil.graphics.Color.YELLOW;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import me.kahlil.geometry.LinearTransformation;
import me.kahlil.geometry.Object3D;
import me.kahlil.geometry.Plane3D;
import me.kahlil.geometry.Sphere3D;
import me.kahlil.geometry.Vector;
import me.kahlil.graphics.Color;
import me.kahlil.graphics.MyCanvas3D;
import me.kahlil.graphics.RayTracerCoordinator;
import me.kahlil.scene.Camera3D;
import me.kahlil.scene.ImmutableCamera3D;
import me.kahlil.scene.ImmutableMaterial;
import me.kahlil.scene.ImmutableScene3D;
import me.kahlil.scene.Light3D;
import me.kahlil.scene.Scene3D;
import me.kahlil.scene.SimpleFrame;

/**
 * A second demo of the ray tracer.
 */
public class Demo2 {

  private static final int NUM_THREADS = 4;

  public static void main(String[] args) throws InterruptedException, ExecutionException {

    Camera3D camera = ImmutableCamera3D.builder()
        .setLocation(new Vector(0, 0, 0))
        .build();
    SimpleFrame frame = new SimpleFrame(new Vector(-1, -1, -1), 2, 2, 400, 400);

    // Objects in scene
    List<Object3D> objects = new ArrayList<Object3D>();
    Sphere3D sphere1 = new Sphere3D(
        new Vector(-.5, 1, -3),
        .4,
        null,
        ImmutableMaterial.builder()
            .setColor(YELLOW)
            .setHardness(10)
            .setSpecularIntensity(1.0)
            .build())
        .transform(LinearTransformation
            .scale(1, 1, 1).compose(LinearTransformation.translate(-1, 0, 0)));
    Sphere3D sphere2 = new Sphere3D(new Vector(.8, .8, -5), .6, null,
        ImmutableMaterial.builder()
            .setColor(RED)
            .setHardness(10)
            .setSpecularIntensity(1.0)
            .setReflective(true)
            .build())
        .transform(
            LinearTransformation.scale(1, 1, 1).compose(LinearTransformation.translate(-1, 0, 0)));
    Sphere3D sphere3 = new Sphere3D(new Vector(2.5, 1, -4), .3, null,
        ImmutableMaterial.builder()
            .setColor(GREEN)
            .setHardness(10)
            .setSpecularIntensity(1.0)
            .build())
        .transform(
            LinearTransformation.scale(1, 1, 1).compose(LinearTransformation.translate(-1, 0, 0)));
    objects.add(sphere1);
    objects.add(sphere2);
    objects.add(sphere3);
    objects.add(
        new Plane3D(
            new Vector(0, 0, -7),
            new Vector(0, 1, 1),
            ImmutableMaterial.builder()
                .setColor(BLUE)
                .setHardness(10)
                .setSpecularIntensity(0.1)
                .build()));

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
    SimpleFrame rendered = rt.render(true, NUM_THREADS);
    long end = System.currentTimeMillis();

    System.out.println("Rendering took " + (end - start) + " ms");

    start = System.currentTimeMillis();
    paintToJpeg("demo2.png", rendered);
    end = System.currentTimeMillis();
    System.out.println("Painting took " + (end - start) + " ms");
  }

  private static void paintToJFrame(SimpleFrame rendered) throws InterruptedException {
    MyCanvas3D canvas = new MyCanvas3D(rendered.getWidthPx(), rendered.getHeightPx());
    SwingUtilities.invokeLater(() -> canvas.createAndShowGUI());
    Thread.sleep(500);

    canvas.paintFrame(rendered);
    SwingUtilities.invokeLater(() -> canvas.refresh());
  }

  private static void paintToJpeg(String fileName, SimpleFrame rendered) {
    int height = rendered.getHeightPx();
    int width = rendered.getWidthPx();
    BufferedImage theImage = new BufferedImage(width, height,
        BufferedImage.TYPE_INT_ARGB);
    int[][] pixel = new int[width][height];
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        int value = rendered.getPixel(i, height - j - 1).getRGB();
        theImage.setRGB(i, j, value);
      }
    }
    File outputFile = new File(fileName);
    try {
      outputFile.createNewFile();
      ImageIO.write(theImage, "png", outputFile);
    } catch (IOException e1) {
      throw new RuntimeException(e1);
    }
  }
}
