package me.kahlil.geometry;

import static com.google.common.base.Preconditions.checkState;
import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.POSITIVE_INFINITY;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.sqrt;
import static me.kahlil.config.Counters.NUM_BOUNDING_INTERSECTIONS;
import static me.kahlil.config.Counters.NUM_BOUNDING_INTERSECTION_TESTS;
import static me.kahlil.geometry.Constants.EPSILON;

import java.util.Arrays;
import java.util.Optional;

/**
 * Bounding volume for containing any amount of polygons.
 *
 * <p>Each polygon is bounded by intersecting 7 planes. From:
 *
 * <p>https://www.scratchapixel.com/lessons/advanced-rendering/introduction-acceleration-structure/bounding-volume-hierarchy-BVH-part1
 */
public class Extents implements BoundingVolume, Intersectable {

  private static final Extents EMPTY = new Extents(new double[]{}, new double[]{});

  private static final double A = sqrt(3) / 3;
  private static final double B = -1.0 * A;
  // 7 plane-set normals pre-defined in the referenced docs.
  private static final Vector[] PLANE_SET_NORMALS =
      new Vector[] {
        new Vector(1, 0, 0),
        new Vector(0, 1, 0),
        new Vector(0, 0, 1),
        new Vector(A, A, A),
        new Vector(B, A, A),
        new Vector(B, B, A),
        new Vector(A, B, A)
      };

  private final Triangle[] triangles;
  // Records min/max d_near and d_far of each ray intersection with the planes (d is from the plane
  // equation Ax + By + Cz = d
  private final double[] dNear;
  private final double[] dFar;

  public static Extents fromPolygon(Polygon polygon) {
    return Extents.fromTriangles(polygon.getTriangles());
  }

  public static Extents fromTriangles(Triangle[] triangles) {
    double[][] dNearAndDFar = computeDNearAndDFar(triangles);
    return new Extents(triangles, dNearAndDFar[0], dNearAndDFar[1]);
  }

  public static Extents empty() {
    return EMPTY;
  }

  private Extents(double[] dNear, double[] dFar) {
    this(new Triangle[] {}, dNear, dFar);
  }

  private Extents(Triangle[] triangles, double[] dNear, double[] dFar) {
    this.triangles = triangles;
    this.dNear = dNear;
    this.dFar = dFar;
  }

  /**
   * Returns if the ray intersects the bounding volume.
   *
   * <p>This should not be called for union'd extents.
   */
  @Override
  public Optional<RayHit> intersectWith(Ray ray) {
    checkState(
        triangles.length > 0,
        "intersectWith(ray) should only be called for Extents that store references to Triangles. Did you accidentally call this on the result of two unioned extents?\n",
        this);
    return findClosestIntersection(ray);
  }

  /**
   * Returns whether or not the ray intersects the bounding volume, and with a {@link RayHit}
   * describing the intersection with the original bounded shape.
   */
  @Override
  public boolean intersectsWithBoundingVolume(Ray ray) {
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

  /** Returns an {@link Extents} bounding the union of the two volumes. */
  public Extents union(Extents other) {
    if (isEmpty()) {
      return other;
    }
    if (other.isEmpty()) {
      return this;
    }
    double[] dNear = new double[PLANE_SET_NORMALS.length];
    double[] dFar = new double[PLANE_SET_NORMALS.length];
    for (int i = 0; i < PLANE_SET_NORMALS.length; i++) {
      dNear[i] = Math.min(this.dNear[i], other.dNear[i]);
      dFar[i] = Math.max(this.dFar[i], other.dFar[i]);
    }
    return new Extents(dNear, dFar);
  }

  /** Returns if this an empty {@link Extents}. */
  public boolean isEmpty() {
    return dNear.length == 0 || dFar.length == 0;
  }

  /**
   * Returns the closest {@link RayHit} from testing intersections of all triangles captured within
   * this extent.
   */
  private Optional<RayHit> findClosestIntersection(Ray ray) {
    double minTime = Integer.MAX_VALUE;
    Optional<RayHit> closestHit = Optional.empty();
    for (Triangle triangle : triangles) {
      Optional<RayHit> rayHit = triangle.intersectInObjectSpace(ray);
      if (rayHit.isPresent()) {
        double time = rayHit.get().getTime();
        if (time < minTime) {
          minTime = time;
          closestHit = rayHit;
        }
      }
    }
    return closestHit;
  }

  /**
   * Returns the d-near and d-far computation necessary to represent the extents. The first element
   * in the returned array is d-near and the second is d-far.
   */
  private static double[][] computeDNearAndDFar(Triangle[] triangles) {
    double[] dNear = new double[PLANE_SET_NORMALS.length];
    Arrays.fill(dNear, POSITIVE_INFINITY);
    double[] dFar = new double[PLANE_SET_NORMALS.length];
    Arrays.fill(dFar, NEGATIVE_INFINITY);

    for (int i = 0; i < PLANE_SET_NORMALS.length; i++) {
      Vector planeNormal = PLANE_SET_NORMALS[i];
      for (Triangle triangle : triangles) {
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
    return new double[][] {dNear, dFar};
  }

  @Override
  public String toString() {
    return "Extents{"
        + "triangles="
        + Arrays.toString(triangles)
        + ", dNear="
        + Arrays.toString(dNear)
        + ", dFar="
        + Arrays.toString(dFar)
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Extents extents = (Extents) o;
    return Arrays.equals(dNear, extents.dNear) && Arrays.equals(dFar, extents.dFar);
  }

  @Override
  public int hashCode() {
    int result = Arrays.hashCode(dNear);
    result = 31 * result + Arrays.hashCode(dFar);
    return result;
  }
}
