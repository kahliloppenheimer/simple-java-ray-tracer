package me.kahlil.demos;

import static me.kahlil.geometry.LinearTransformation.rotateAboutXAxis;
import static me.kahlil.geometry.LinearTransformation.translate;
import static me.kahlil.graphics.Color.RED;
import static me.kahlil.graphics.Color.YELLOW;
import static me.kahlil.scene.Cameras.STANDARD_CAMERA;

import com.google.common.collect.ImmutableList;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import me.kahlil.geometry.Plane3D;
import me.kahlil.geometry.Sphere;
import me.kahlil.geometry.Vector;
import me.kahlil.graphics.Color;
import me.kahlil.graphics.MyCanvas3D;
import me.kahlil.graphics.RayTracerCoordinator;
import me.kahlil.scene.ImmutableMaterial;
import me.kahlil.scene.ImmutableScene;
import me.kahlil.scene.PointLight;
import me.kahlil.scene.Raster;
import me.kahlil.scene.Scene;

/** A second demo of the ray tracer. */
public class Demo {

  public static void main(String[] args) throws InterruptedException, ExecutionException {

    Raster frame = new Raster(400, 400);

    // Objects in scene
    Sphere sphere1 =
        new Sphere(
                ImmutableMaterial.builder()
                    .setColor(YELLOW)
                    .setHardness(10)
                    .setSpecularIntensity(1.0)
                    .build())
            .transform(rotateAboutXAxis(0))
            .transform(translate(1, 1, -3));
    Plane3D plane =
        new Plane3D(
                new Vector(0, 0, 1),
                ImmutableMaterial.builder()
                    .setColor(RED)
                    .setHardness(10)
                    .setSpecularIntensity(0.1)
                    .build())
            .transform(rotateAboutXAxis(145))
            .transform(translate(0, 0, -5));

    // Lights in scene
    List<PointLight> lights =
        ImmutableList.of(
            new PointLight(new Vector(3, 3, 0), new Color(115, 115, 115)),
            new PointLight(new Vector(-6, 5, 0), new Color(200, 200, 200)));

    // Whole scene
    Scene scene =
        ImmutableScene.builder()
            .setShapes(ImmutableList.of(sphere1, plane))
            .setLights(lights)
            .setBackgroundColor(Color.BLACK)
            .setAmbient(new Color((float) .075, (float) .075, (float) .075))
            .build();

    RayTracerCoordinator rt = new RayTracerCoordinator(frame, STANDARD_CAMERA, scene);

    long start = System.currentTimeMillis();
    Raster rendered = rt.render(false);
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
