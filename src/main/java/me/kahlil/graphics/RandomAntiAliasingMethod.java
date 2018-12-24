package me.kahlil.graphics;

import java.util.Random;
import me.kahlil.scene.Ray3D;

final class RandomAntiAliasingMethod implements AntiAliasingMethod {

  private static final ThreadLocal<Random> RAND = ThreadLocal.withInitial(Random::new);

  @Override
  public Ray3D[] getRaysToSample(Ray3D ray, SamplingRadius samplingRadius, int numSamples) {
    Ray3D[] raysToSample = new Ray3D[numSamples];
    for (int i = 0; i < raysToSample.length; i++) {
      raysToSample[i] = new Ray3D(
          ray.getStart(),
          ray.getDirection().translate(
              RAND.get().nextDouble() * samplingRadius.getWidth(),
              RAND.get().nextDouble() * samplingRadius.getHeight()));
    }
    return raysToSample;
  }
}
