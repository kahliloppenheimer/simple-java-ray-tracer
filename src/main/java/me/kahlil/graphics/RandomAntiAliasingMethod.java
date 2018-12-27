package me.kahlil.graphics;

import java.util.Random;
import me.kahlil.geometry.Ray3D;

final class RandomAntiAliasingMethod implements AntiAliasingMethod {

  private static final ThreadLocal<Random> RAND = ThreadLocal.withInitial(Random::new);
  private final int numSamples;

  RandomAntiAliasingMethod(int numSamples) {
    this.numSamples = numSamples;
  }

  @Override
  public Ray3D[] getRaysToSample(Ray3D ray, SamplingRadius samplingRadius) {
    Ray3D[] raysToSample = new Ray3D[numSamples];
    for (int i = 0; i < raysToSample.length; i++) {
      raysToSample[i] =
          new Ray3D(
              ray.getStart(),
              ray.getDirection()
                  .translate(
                      RAND.get().nextDouble() * samplingRadius.getWidth() * negativeOrPositive(),
                      RAND.get().nextDouble() * samplingRadius.getHeight() * negativeOrPositive()));
    }
    return raysToSample;
  }

  /** Returns either 1 or -1 with a 50% chance for both. */
  private static int negativeOrPositive() {
    return RAND.get().nextBoolean() ? -1 : 1;
  }
}
