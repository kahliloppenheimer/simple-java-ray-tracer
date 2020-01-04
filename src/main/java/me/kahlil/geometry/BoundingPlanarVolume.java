package me.kahlil.geometry;

import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.POSITIVE_INFINITY;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.sqrt;
import static me.kahlil.config.Counters.NUM_BOUNDING_INTERSECTIONS;
import static me.kahlil.config.Counters.NUM_BOUNDING_INTERSECTION_TESTS;
import static me.kahlil.geometry.Constants.EPSILON;

/**
 * Bounding volume for containing any amount of polygons.
 *
 * <p>Each polygon is bounded by intersecting 7 planes. From:
 *
 * <p>https://www.scratchapixel.com/lessons/advanced-rendering/introduction-acceleration-structure/bounding-volume-hierarchy-BVH-part1
 */
public class BoundingPlanarVolume implements BoundingVolume {

  private static final double A = sqrt(3) / 3;
  private static final double B = -1.0 * A;
  // 7 plane-set normals pre-defined in the referenced docs.
  private static final Vector[] PLANE_SET_NORMALS =
      new Vector[] {
        new Vector(1, 0, 0), new Vector(0, 1, 0), new Vector(0, 0, 1),
        new Vector(A, A, A),
        new Vector(B, A, A),
        new Vector(B, B, A),
        new Vector(A, B, A)
      };

  // First index maps over all contained polygons. Second index
  // records d_near and d_far of each ray intersection with the planes (d is from the plane equation
  // Ax + By + Cz = d
  private final double[] dNear;
  private final double[] dFar;

  public BoundingPlanarVolume(ConvexPolygon polygon) {
    dNear = new double[PLANE_SET_NORMALS.length];
    dFar = new double[PLANE_SET_NORMALS.length];

    initializeBoundingDistances(polygon, dNear, dFar);
  }

  /**
   * Returns if the ray intersects the bounding volume.
   */
  @Override
  public boolean intersectsWith(Ray ray) {
    NUM_BOUNDING_INTERSECTION_TESTS.getAndIncrement();
    double timeNearMax = NEGATIVE_INFINITY;
    double timeFarMin = POSITIVE_INFINITY;
    for (int i = 0; i < PLANE_SET_NORMALS.length; i++) {
      double numerator = PLANE_SET_NORMALS[i].dot(ray.getStart());
      double denominator = PLANE_SET_NORMALS[i].dot(ray.getDirection());

      // Ray and plane are parallel, so we say they don't intersect.
      if (Math.abs(denominator) < EPSILON) {
        continue;
      }

      double timeNear = (dNear[i] - numerator) / denominator;
      double timeFar = (dFar[i] - numerator) / denominator;

      timeNearMax = max(timeNearMax, min(timeNear, timeFar));
      timeFarMin = min(timeFarMin, max(timeNear, timeFar));

      if (timeNearMax > timeFarMin) {
        return false;
      }
    }
    NUM_BOUNDING_INTERSECTIONS.getAndIncrement();
    return true;
  }

  private static void initializeBoundingDistances(
      ConvexPolygon polygon, double[] dNear, double[] dFar) {
    for (int i = 0; i < PLANE_SET_NORMALS.length; i++) {
      Vector planeNormal = PLANE_SET_NORMALS[i];
      for (Triangle triangle : polygon.getTriangles()) {
        for (Vector vertex : triangle.getVertexes()) {
          double d = vertex.dot(planeNormal);
          if (d < dNear[i]) {
            dNear[i] = d;
          }
          if (d > dFar[i]) {
            dFar[i] = d;
          }
        }
      }
    }
  }
}
