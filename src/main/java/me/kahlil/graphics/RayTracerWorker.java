package me.kahlil.graphics;

import com.google.common.base.Preconditions;
import me.kahlil.scene.Scene3D;
import java.util.List;

public class RayTracerWorker implements Runnable {

  private static final int ANTI_ALIAS_VARIANCE_THRESHOLD = 20;
  private static final int MIN_ANTI_ALIAS_SAMPLES = 2;
  private static final int MAX_ANTI_ALIAS_SAMPLES = 32;

  private final RayTracer rayTracer;
  private final SimpleFrame3D frame;
  private final Camera3D camera;
  private final Scene3D scene;
  private final boolean shadowsEnabled;
  private final int startingPixel;
  private final int pixelIncrement;
  private final double widthDelta;
  private final double heightDelta;

  private int numTraces; // Count total # of traces across ray-tracing run

  RayTracerWorker(
      RayTracer rayTracer,
      SimpleFrame3D frame,
      Camera3D camera,
      Scene3D scene,
      boolean shadowsEnabled,
      int startingPixel,
      int pixelIncrement
  ) {
    this.rayTracer = rayTracer;
    this.frame = frame;
    this.camera = camera;
    this.scene = scene;
    this.shadowsEnabled = shadowsEnabled;
    this.startingPixel = startingPixel;
    this.pixelIncrement = pixelIncrement;
    // Increments in coordinate space between actual pixels
    this.widthDelta = frame.getWidth() / frame.getWidthPx();
    this.heightDelta = frame.getHeight() / frame.getHeightPx();

    this.numTraces = 0;
  }

  @Override
  public void run() {
    for (int i = 0; i < frame.getWidthPx(); ++i) {
      for (int j = startingPixel; j < frame.getHeightPx(); j += pixelIncrement) {
        frame.setPixel(
            i,
            j,
            rayTracer.traceRay(i, j));
      }
    }
    System.out.println("Thread " + startingPixel + " finished!");
  }

  int getNumTraces() {
    return numTraces;
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
