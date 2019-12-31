package me.kahlil.graphics;

import java.awt.Color;
import me.kahlil.scene.Raster;

public class RayTracerWorker implements Runnable {

  private final RayTracer rayTracer;
  private final Raster frame;
  private final int startingPixel;
  private final int pixelIncrement;

  RayTracerWorker(
      RayTracer rayTracer,
      Raster frame,
      int startingPixel,
      int pixelIncrement) {
    this.rayTracer = rayTracer;
    this.frame = frame;
    this.startingPixel = startingPixel;
    this.pixelIncrement = pixelIncrement;
  }

  @Override
  public void run() {
    for (int i = 0; i < frame.getHeightPx(); ++i) {
      for (int j = startingPixel; j < frame.getWidthPx(); j += pixelIncrement) {
        Color color = rayTracer.traceRay(i, j);
        frame.setPixel(i, j, color);
      }
    }
    System.out.println("Thread " + startingPixel + " finished!");
  }
}
