package me.kahlil.graphics;

import me.kahlil.geometry.Ray;

/** A method for picking out points within a sampling radius to apply anti-aliasing. */
interface AntiAliasingMethod {

  /**
   * Generates the rays to sample to anti-alias a given ray within a given rectangular sampling
   * radius.
   */
  Ray[] getRaysToSample(Ray ray, SamplingRadius samplingRadius);
}
