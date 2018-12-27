package me.kahlil.graphics;

import static com.google.common.collect.ImmutableList.toImmutableList;

import java.util.Arrays;
import java.util.List;
import me.kahlil.geometry.Ray3D;
import me.kahlil.scene.Camera3D;
import me.kahlil.scene.SimpleFrame;

/**
 * A simple anti-aliasing implementation of ray tracing that uses a given {@link AntiAliasingMethod}
 * to generate a set of rays to sample, and then averages their results together to produce one
 * final pixel color.
 *
 * <p>No adaptive anti-aliasing or more complicated combination logic is performed.
 */
final class SimpleAntiAliaser extends RayTracer {

  private static final int NUM_SAMPLES = 4;

  private final RayTracer rayTracer;
  private final AntiAliasingMethod antiAliasingMethod;
  private final SamplingRadius samplingRadius;

  SimpleAntiAliaser(
      SimpleFrame frame,
      Camera3D camera,
      RayTracer rayTracer,
      AntiAliasingMethod antiAliasingMethod) {
    super(rayTracer.getScene(), frame, camera, rayTracer.isShadowsEnabled());
    this.samplingRadius =
        ImmutableSamplingRadius.builder()
            .setWidth(frame.getPixelWidthInCoordinateSpace() * 0.5)
            .setHeight(frame.getPixelHeightInCoordinateSpace() * 0.5)
            .build();
    this.rayTracer = rayTracer;
    this.antiAliasingMethod = antiAliasingMethod;
  }

  @Override
  Color traceRay(Ray3D ray) {
    float[] averageRgba =
        computeAverage(
            Arrays.stream(antiAliasingMethod.getRaysToSample(ray, samplingRadius))
                .map(rayTracer::traceRay)
                .map(Color::getRgbaAsFloats)
                .collect(toImmutableList()));
    return new Color(averageRgba[0], averageRgba[1], averageRgba[2], averageRgba[3]);
  }

  private static float[] computeAverage(List<float[]> samples) {
    float[] averageValues = new float[samples.get(0).length];
    for (int i = 0; i < samples.size(); i++) {
      for (int j = 0; j < samples.get(i).length; j++) {
        averageValues[j] += samples.get(i)[j] / samples.size();
      }
    }
    return averageValues;
  }
}
