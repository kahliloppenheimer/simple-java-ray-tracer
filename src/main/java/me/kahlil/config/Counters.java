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
   * Counter of total number of ray-shape intersections computed during the ray tracing algorithm.
   */
  public static final AtomicLong NUM_TOTAL_INTERSECTIONS = new AtomicLong();

  /**
   * Counter of total number of ray-triangle intersections computed during the ray tracing algorithm.
   */
  public static final AtomicLong NUM_TRIANGLE_INTERSECTIONS = new AtomicLong();

}
