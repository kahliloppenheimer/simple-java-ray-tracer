package me.kahlil.graphics;

import me.kahlil.scene.Ray3D;

/** A method for picking out points within a sampling radius to apply anti-aliasing. */
interface AntiAliasingMethod {

  /** Generates the rays to sample to anti-alias a given ray within a given rectangular sampling
   * radius. */
  Ray3D[] getRaysToSample(Ray3D ray, SamplingRadius samplingRadius, int numSamples);

}
