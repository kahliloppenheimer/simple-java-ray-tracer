package me.kahlil.graphics;

import me.kahlil.scene.Ray3D;

interface AntiAliasingMethod {

  /** Generates the rays to sample to anti-alias a given ray within a given rectangular sampling
   * radius. */
  Ray3D[] getRaysToSample(Ray3D ray, SamplingRadius samplingRadius, int numSamples);

}
