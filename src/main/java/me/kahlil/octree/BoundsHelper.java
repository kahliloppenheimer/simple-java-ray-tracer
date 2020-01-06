package me.kahlil.octree;

import static com.google.common.base.Preconditions.checkState;
import static java.lang.Float.NEGATIVE_INFINITY;
import static java.lang.Float.POSITIVE_INFINITY;

import java.util.Arrays;
import me.kahlil.geometry.Polygon;
import me.kahlil.geometry.Vector;

public final class BoundsHelper {

  /**
   * Returns a 2-sized array containing the minimum and maximum bounds of all of the given shapes.
   */
  public static Vector[] computeGlobalMinAndMax(Polygon[] polygons) {
    checkState(polygons.length > 0);

    double[] minXyz = new double[3];
    Arrays.fill(minXyz, POSITIVE_INFINITY);
    double[] maxXyz = new double[3];
    Arrays.fill(maxXyz, NEGATIVE_INFINITY);

    for (Polygon polygon : polygons) {
      Vector min = polygon.minBound();
      Vector max = polygon.maxBound();
      for (int i = 0; i < 3; i++) {
        if (min.getComponent(i) < minXyz[i]) {
          minXyz[i] = min.getComponent(i);
        }
        if (max.getComponent(i) > maxXyz[i]) {
          maxXyz[i] = max.getComponent(i);
        }
      }
    }

    return new Vector[]{
        new Vector(minXyz[0], minXyz[1], minXyz[2]),
        new Vector(maxXyz[0], maxXyz[1], maxXyz[2])
    };
  }

}
