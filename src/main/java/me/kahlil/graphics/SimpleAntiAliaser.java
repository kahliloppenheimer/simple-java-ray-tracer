package me.kahlil.graphics;

import static com.google.common.collect.ImmutableList.toImmutableList;

import java.util.Arrays;
import java.util.List;
import me.kahlil.scene.Ray3D;

final class SimpleAntiAliaser extends RayTracer {

  private static final int NUM_SAMPLES = 16;

  private final RayTracer rayTracer;
  private final AntiAliasingMethod antiAliasingMethod;
  private final SamplingRadius samplingRadius;

  SimpleAntiAliaser(
      Camera3D camera,
      SimpleFrame3D frame,
      RayTracer rayTracer,
      AntiAliasingMethod antiAliasingMethod) {
    super(camera, frame);
    this.samplingRadius = ImmutableSamplingRadius.builder()
        .setWidth(frame.getPixelWidthInCoordinateSpace() * 0.5)
        .setHeight(frame.getPixelHeightInCoordinateSpace() * 0.5)
        .build();
    this.rayTracer = rayTracer;
    this.antiAliasingMethod = antiAliasingMethod;
  }

  @Override
  Color traceRay(Ray3D ray) {
    float[] averageRgba = computeAverage(
        Arrays.stream(antiAliasingMethod.getRaysToSample(ray, samplingRadius, NUM_SAMPLES))
            .map(rayTracer::traceRay)
            .map(Color::getRgbaAsFloats)
            .collect(toImmutableList()));
    return new Color(
        averageRgba[0],
        averageRgba[1],
        averageRgba[2]);
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
