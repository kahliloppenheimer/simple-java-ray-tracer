package me.kahlil.graphics;

import static com.google.common.collect.ImmutableList.toImmutableList;

import com.google.common.base.Preconditions;
import java.util.Arrays;
import java.util.List;
import me.kahlil.scene.Ray3D;

final class SimpleAntiAliaser extends RayTracer {

  private final RayTracer rayTracer;
  private final AntiAliasingMethod antiAliasingMethod;
  private final int numSamples;
  private final SamplingRadius samplingRadius;

  SimpleAntiAliaser(
      Camera3D camera,
      SimpleFrame3D frame,
      RayTracer rayTracer,
      AntiAliasingMethod antiAliasingMethod,
      int numSamples) {
    super(camera, frame);
    this.samplingRadius = ImmutableSamplingRadius.builder()
        .setWidth(frame.getPixelWidthInCoordinateSpace() * 0.5)
        .setHeight(frame.getPixelHeightInCoordinateSpace() * 0.5)
        .build();
    this.rayTracer = rayTracer;
    this.antiAliasingMethod = antiAliasingMethod;
    this.numSamples = numSamples;
  }

  @Override
  Color traceRay(Ray3D ray) {
    int[] averageRgba = computeAverage(
        Arrays.stream(antiAliasingMethod.getRaysToSample(ray, samplingRadius, numSamples))
            .map(rayTracer::traceRay)
            .map(Color::getRgba)
            .collect(toImmutableList()));
    return new Color(
        Math.min(averageRgba[0], 255),  // rounding errors can cause this to be greater than 255.
        Math.min(averageRgba[1], 255),
        Math.min(averageRgba[2], 255),
        Math.min(averageRgba[3], 255));
  }

  private static int[] computeAverage(List<int[]> samples) {
    Preconditions.checkArgument(samples != null && !samples.isEmpty(),
        "Samples must be non-null and non-empty.");
    int[] averageValues = new int[samples.get(0).length];
    for (int i = 0; i < samples.size(); i++) {
      for (int j = 0; j < samples.get(i).length; j++) {
        averageValues[j] += Math.round(1.0 * samples.get(i)[j] / samples.size());
      }
    }
    return averageValues;
  }
}
