package me.kahlil.geometry;

import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.POSITIVE_INFINITY;
import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Intersection borrowed from:
 * https://tavianator.com/fast-branchless-raybounding-box-intersections-part-2-nans/
 * */
public final class BoundingBox implements BoundingVolume {

  private final Vector minBound;
  private final Vector maxBound;

  public BoundingBox(Vector minBound, Vector maxBound) {
    this.minBound = minBound;
    this.maxBound = maxBound;
  }

  @Override
  public double intersectWithBoundingVolume(Ray ray) {
    double tmin = NEGATIVE_INFINITY, tmax = POSITIVE_INFINITY;

    for (int i = 0; i < 3; ++i) {
      double t1 = (minBound.getComponent(i) - ray.getStart().getComponent(i)) * ray.getInvertedDirection().getComponent(i);
      double t2 = (maxBound.getComponent(i) - ray.getStart().getComponent(i)) * ray.getInvertedDirection().getComponent(i);

      tmin = max(tmin, min(t1, t2));
      tmax = min(tmax, max(t1, t2));
    }
    if (tmax < max(tmin, 0.0)) {
      return -1;
    }
    return tmin;
  }
}
