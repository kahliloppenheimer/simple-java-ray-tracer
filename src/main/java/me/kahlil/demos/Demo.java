package me.kahlil.demos;

import static java.awt.Color.BLACK;
import static java.awt.Color.BLUE;
import static java.awt.Color.GREEN;
import static java.awt.Color.YELLOW;
import static me.kahlil.geometry.LinearTransformation.translate;
import static me.kahlil.scene.Cameras.STANDARD_CAMERA;

import com.google.common.collect.ImmutableList;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import me.kahlil.geometry.Plane;
import me.kahlil.geometry.Sphere;
import me.kahlil.geometry.Vector;
import me.kahlil.graphics.MyCanvas3D;
import me.kahlil.graphics.RayTracerCoordinator;
import me.kahlil.scene.ImmutableMaterial;
import me.kahlil.scene.ImmutablePointLight;
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
            .transform(translate(1, 1, -5));
    Sphere sphere2 =
        new Sphere(
            ImmutableMaterial.builder()
                .setColor(GREEN)
                .setHardness(20)
                .setSpecularIntensity(0.5)
                .build())
        .transform(translate(-2, 1, -10));
    Plane plane =
        new Plane(new Vector(0, -1, 0), new Vector(0, 1.0, 0.0),
            ImmutableMaterial.builder().setColor(BLUE)
                .setHardness(100)
                .setSpecularIntensity(1.0)
                .setReflective(false)
                .build());

    // Lights in scene
    List<PointLight> lights =
        ImmutableList.of(
            ImmutablePointLight.builder()
                .setLocation(new Vector(3, 3, 0))
                .setColor(new Color(115, 115, 115))
                .build(),
            ImmutablePointLight.builder()
                .setLocation(new Vector(-6, 5, 0))
                .setColor(new Color(200, 200, 200))
                .build());

    // Whole scene
    Scene scene =
        ImmutableScene.builder()
            .setShapes(ImmutableList.of(sphere1, sphere2, plane))
            .setLights(lights)
            .setBackgroundColor(BLACK)
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
