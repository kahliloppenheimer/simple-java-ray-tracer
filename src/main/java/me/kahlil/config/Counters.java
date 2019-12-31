package me.kahlil.config;

import java.util.concurrent.atomic.AtomicLong;

/**
 * A collection of {@link ThreadLocal} counters used to count interesting things during the ray
 * tracing computation.
 */
public final class Counters {

  /**
   * Counter of total number of rays traced during the ray tracing algorithm.
   */
  public static final AtomicLong NUM_TRACES = new AtomicLong();

  /**
   * Counter of total number of shape-ray intersections computed during the ray tracing algorithm.
   */
  public static final AtomicLong NUM_INTERSECTIONS_COMPUTED = new AtomicLong();

}
