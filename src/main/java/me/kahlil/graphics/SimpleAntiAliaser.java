package me.kahlil.graphics;

import static me.kahlil.graphics.CoordinateMapper.getPixelHeightInCameraSpace;
import static me.kahlil.graphics.CoordinateMapper.getPixelWidthInCameraSpace;

import java.awt.Color;
import me.kahlil.geometry.Ray;
import me.kahlil.scene.Camera;
import me.kahlil.scene.Raster;

/**
 * A simple anti-aliasing implementation of ray tracing that uses a given {@link AntiAliasingMethod}
 * to generate a set of rays to sample, and then averages their results together to produce one
 * final pixel color.
 *
 * <p>No adaptive anti-aliasing or more complicated combination logic is performed.
 */
public final class SimpleAntiAliaser extends RayTracer {

  private final RayTracer rayTracer;
  private final AntiAliasingMethod antiAliasingMethod;
  private final SamplingRadius samplingRadius;

  private final ThreadLocal<Long> numTraces = ThreadLocal.withInitial(() -> 0L);

  public SimpleAntiAliaser(
      Raster frame, Camera camera, RayTracer rayTracer, AntiAliasingMethod antiAliasingMethod) {
    super(frame, camera);
    this.samplingRadius =
        ImmutableSamplingRadius.builder()
            .setWidth(getPixelWidthInCameraSpace(frame, camera) * 0.5)
            .setHeight(getPixelHeightInCameraSpace(frame, camera) * 0.5)
            .build();
    this.rayTracer = rayTracer;
    this.antiAliasingMethod = antiAliasingMethod;
  }

  @Override
  Color traceRay(Ray ray) {
    Ray[] raysToSample = antiAliasingMethod.getRaysToSample(ray, samplingRadius);
    Color[] results = new Color[raysToSample.length];

    // Trace all the sample rays and count the total number of rays traced.
    int numTraces = 0;
    for (int i = 0; i < raysToSample.length; i++) {
      results[i] = rayTracer.traceRay(raysToSample[i]);
    }

    // Average each RGBA component of the color results using an unweighted average.
    // Use pass-by-reference array semantics to avoid excess array allocations.
    float[] currentColor = new float[4];
    float[] runningAverage = new float[4];
    for (Color color : results) {
      color.getRGBComponents(currentColor);
      incrementAverages(runningAverage, currentColor, 1.0f / results.length);
    }
    return new Color(runningAverage[0], runningAverage[1], runningAverage[2], runningAverage[3]);
  }

  private void incrementAverages(float[] runningAverage, float[] currentColor, float weight) {
    for (int i = 0; i < 4; i++) {
      runningAverage[i] += currentColor[i] * weight;
    }
  }
}
