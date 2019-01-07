package me.kahlil.graphics;

import java.util.Random;
import me.kahlil.geometry.Ray;

final class RandomAntiAliasingMethod implements AntiAliasingMethod {

  private static final ThreadLocal<Random> RAND = ThreadLocal.withInitial(Random::new);
  private final int numSamples;

  RandomAntiAliasingMethod(int numSamples) {
    this.numSamples = numSamples;
  }

  @Override
  public Ray[] getRaysToSample(Ray ray, SamplingRadius samplingRadius) {
    Ray[] raysToSample = new Ray[numSamples];
    for (int i = 0; i < raysToSample.length; i++) {
      raysToSample[i] =
          new Ray(
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
