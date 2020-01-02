package me.kahlil.graphics;

import static me.kahlil.graphics.CoordinateMapper.getPixelHeightInCameraSpace;
import static me.kahlil.graphics.CoordinateMapper.getPixelWidthInCameraSpace;

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
  MutableColor traceRay(Ray ray) {
    Ray[] raysToSample = antiAliasingMethod.getRaysToSample(ray, samplingRadius);
    MutableColor[] results = new MutableColor[raysToSample.length];

    // Trace all the sample rays and count the total number of rays traced.
    for (int i = 0; i < raysToSample.length; i++) {
      results[i] = rayTracer.traceRay(raysToSample[i]);
    }

    float weight = 1.0f / results.length;
    ColorComputation runningAverage = ColorComputation.modifyingInPlace(results[0]).scaleFloat(weight);
    for (int i = 1; i < results.length; i++) {
      runningAverage.add(ColorComputation.modifyingInPlace(results[i]).scaleFloat(weight).compute());
    }
    return runningAverage.compute();
  }
}
