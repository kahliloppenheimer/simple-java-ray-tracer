package me.kahlil.graphics;

import me.kahlil.scene.Raster;

public class RayTracerWorker implements Runnable {

  private final RayTracer rayTracer;
  private final Raster frame;
  private final int startingPixel;
  private final int pixelIncrement;

  private long numTraces;

  RayTracerWorker(
      RayTracer rayTracer,
      Raster frame,
      int startingPixel,
      int pixelIncrement) {
    this.rayTracer = rayTracer;
    this.frame = frame;
    this.startingPixel = startingPixel;
    this.pixelIncrement = pixelIncrement;
    this.numTraces = 0L;
  }

  @Override
  public void run() {
    for (int i = 0; i < frame.getHeightPx(); ++i) {
      for (int j = startingPixel; j < frame.getWidthPx(); j += pixelIncrement) {
        RenderingResult renderingResult = rayTracer.traceRay(i, j);
        numTraces += renderingResult.getNumRaysTraced();
        frame.setPixel(i, j, renderingResult.getColor());
      }
    }
    System.out.println("Thread " + startingPixel + " finished!");
  }

  long getNumTraces() {
    return rayTracer.getNumTraces();
  }

  //  private static boolean samplesAreVerySimilar(List<int[]> colorSamples) {
  //    // Always count at least 3 samples.
  //    if (colorSamples.size() < 3) {
  //      return false;
  //    }
  //    int[] colorVariance = computeVariance(colorSamples);
  //    // If any one RGB component stdev is above the threshold, return false
  //    for (int i = 0; i < colorVariance.length; i++) {
  //      if (colorVariance[i] > ANTI_ALIAS_VARIANCE_THRESHOLD) {
  //        return false;
  //      }
  //    }
  //    return true;
  //  }

  //  private static int[] computeVariance(List<int[]> samples) {
  //    int[] averages = computeAverage(samples);
  //    int[] variance = new int[samples.get(0).length];
  //    for (int i = 0; i < samples.size(); i++) {
  //      int[] nextSample = samples.get(i);
  //      for (int j = 0; j < nextSample.length; j++) {
  //        int distanceFromMean = nextSample[j] - averages[j];
  //        variance[j] = Math.round((1.0f * distanceFromMean * distanceFromMean) / samples.size());
  //      }
  //    }
  //    return variance;
  //  }

}
