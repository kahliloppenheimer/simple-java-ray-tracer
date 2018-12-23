package me.kahlil.graphics;

import me.kahlil.scene.Ray3D;

final class RandomAntiAliasingMethod implements AntiAliasingMethod {

  @Override
  public Ray3D[] getRaysToSample(Ray3D ray, SamplingRadius samplingRadius, int numSamples) {
    Ray3D[] raysToSample = new Ray3D[numSamples];
    for (int i = 0; i < raysToSample.length; i++) {
      raysToSample[i] = new Ray3D(
          ray.getStart(),
          ray.getDirection().translate(
              Math.random() * samplingRadius.getWidth() * (Math.random() < 0.5 ? 1 : -1),
              Math.random() * samplingRadius.getHeight() * (Math.random() < 0.5 ? 1 : -1)));
    }
    return raysToSample;
  }
}
