package me.kahlil.graphics;

import me.kahlil.scene.Ray3D;

final class SimpleAntiAliaser extends RayTracer {

  private final RayTracer rayTracer;
  private final AntiAliasingMethod antiAliasingMethod;
  private final int numSamples;
  private final SamplingRadius samplingRadius;

  public SimpleAntiAliaser(
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
    Ray3D[] raysToSample = antiAliasingMethod.getRaysToSample(ray, samplingRadius, numSamples);
    int[] averageRgba = new int[raysToSample.length];
    for (Ray3D rayToSample : raysToSample) {
      Color tracedColor = this.rayTracer.traceRay(rayToSample);
      averageRgba[0] += Math.round(1.0f * tracedColor.getRed() / raysToSample.length);
      averageRgba[1] += Math.round(1.0f * tracedColor.getGreen() / raysToSample.length);
      averageRgba[2] += Math.round(1.0f * tracedColor.getBlue() / raysToSample.length);
      averageRgba[3] += Math.round(1.0f * tracedColor.getAlpha() / raysToSample.length);
    }
    return new Color(
        Math.min(averageRgba[0], 255),  // rounding errors can cause this to be greater than 255.
        Math.min(averageRgba[1], 255),
        Math.min(averageRgba[2], 255),
        Math.min(averageRgba[3], 255));
  }
}
