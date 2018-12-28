package me.kahlil.demos;

import static me.kahlil.graphics.Color.GREEN;
import static me.kahlil.graphics.Color.RED;
import static me.kahlil.scene.Cameras.STANDARD_CAMERA;

import com.google.common.collect.ImmutableList;
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
import me.kahlil.scene.ImmutableMaterial;
import me.kahlil.scene.ImmutableScene;
import me.kahlil.scene.Light3D;
import me.kahlil.scene.Raster;
import me.kahlil.scene.Scene;

/** A second demo of the ray tracer. */
public class Demo2 {

  public static void main(String[] args) throws InterruptedException, ExecutionException {

    Raster frame = new Raster(400, 400);

    // Objects in scene
    List<Object3D> objects = new ArrayList<>();
    Sphere3D sphere1 =
        new Sphere3D(
                new Vector(1, 1, -3),
                1.0,
                null,
                ImmutableMaterial.builder()
                    .setColor(GREEN)
                    .setHardness(10)
                    .setSpecularIntensity(1.0)
                    .build());
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
                new Vector(2.5, 1, -4),
                .3,
                null,
                ImmutableMaterial.builder()
                    .setColor(GREEN)
                    .setHardness(10)
                    .setSpecularIntensity(1.0)
                    .build())
            .transform(
                LinearTransformation.scale(1, 1, 1)
                    .compose(LinearTransformation.translate(-1, 0, 0)));
    objects.add(sphere1);
    objects.add(sphere2);
    objects.add(sphere3);
    Plane3D plane =
        new Plane3D(
            new Vector(0, 0, 0),
            new Vector(0, 1, 0),
            ImmutableMaterial.builder()
                .setColor(RED)
                .setHardness(10)
                .setSpecularIntensity(0.1)
                .build());

    // Lights in scene
    List<Light3D> lights = new ArrayList<>();
    lights.add(new Light3D(new Vector(3, 3, 0), new Color(115, 115, 115)));
    lights.add(new Light3D(new Vector(-6, 5, 0), new Color(200, 200, 200)));

    // Whole scene
    Scene scene =
        ImmutableScene.builder()
            .setObjects(ImmutableList.of(sphere1, plane))
            .setLights(lights)
            .setBackgroundColor(Color.BLACK)
            .setAmbient(new Color((float) .075, (float) .075, (float) .075))
            .build();

    RayTracerCoordinator rt = new RayTracerCoordinator(frame, STANDARD_CAMERA, scene);

    long start = System.currentTimeMillis();
    Raster rendered = rt.render(true);
    long end = System.currentTimeMillis();

    System.out.println("Rendering took " + (end - start) + " ms");

    start = System.currentTimeMillis();
//    paintToJpeg("demo2.png", rendered);
    paintToJFrame(rendered);
    end = System.currentTimeMillis();
    System.out.println("Painting took " + (end - start) + " ms");
  }

  private static void paintToJFrame(Raster rendered) throws InterruptedException {
    MyCanvas3D canvas = new MyCanvas3D(rendered.getWidthPx(), rendered.getHeightPx());
    SwingUtilities.invokeLater(() -> canvas.createAndShowGUI());
    Thread.sleep(500);

    canvas.paintFrame(rendered);
    SwingUtilities.invokeLater(() -> canvas.refresh());
  }

  private static void paintToJpeg(String fileName, Raster rendered) {
    int height = rendered.getHeightPx();
    int width = rendered.getWidthPx();
    BufferedImage theImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    int[][] pixel = new int[width][height];
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        int value = rendered.getPixel(i, j).getRGB();
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
