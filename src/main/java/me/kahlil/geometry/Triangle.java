package me.kahlil.geometry;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.POSITIVE_INFINITY;
import static java.lang.Math.abs;
import static me.kahlil.config.Counters.NUM_TRIANGLE_INTERSECTIONS;
import static me.kahlil.config.Counters.NUM_TRIANGLE_TESTS;
import static me.kahlil.geometry.Constants.EPSILON;

import java.util.Arrays;
import java.util.Optional;
import me.kahlil.scene.Material;

public class Triangle extends Shape implements Polygon {

  private final Material material;

  // Array of size-3 containing the vertexes of the triangle.
  private final Vector[] vertexes;

  // Array of size 3 containing the vertex normals of the triangle.
  private final Vector[] vertexNormals;

  private final Vector minBound;
  private final Vector maxBound;

  private Triangle(
      Material material,
      Vector[] vertexes,
      Vector[] vertexNormals) {
    checkArgument(vertexes.length == 3, "A triangle must have 3 vertexes. Found: %s", Arrays.toString(vertexes));
    checkArgument(vertexNormals.length == 0 || vertexNormals.length == 3, "A triangle must have no vertex normals or 3 0 surface normals. Found: %s", Arrays.toString(vertexes));
    this.material = material;
    this.vertexes = vertexes;
    this.vertexNormals = vertexNormals;

    double[][] minMaxBounds = computeMinMaxBounds();
    this.minBound = new Vector(minMaxBounds[0][0], minMaxBounds[0][1], minMaxBounds[0][2]);
    this.maxBound = new Vector(minMaxBounds[1][0], minMaxBounds[1][1], minMaxBounds[1][2]);
  }

  public static Triangle withSurfaceNormals(
      Material material,
      Vector vertex0,
      Vector vertex1,
      Vector vertex2) {
     return withSurfaceNormals(material, new Vector[]{vertex0, vertex1, vertex2});
   }

   public static Triangle withSurfaceNormals(
       Material material,
       Vector[] vertexes) {
     return new Triangle(material, vertexes, new Vector[]{});
   }

   public static Triangle withVertexNormals(
       Material material,
       Vector[] vertexes,
       Vector[] normals) {
      return new Triangle(material, vertexes, normals);
   }

  public static Triangle equilateralTriangle(Material material) {
    return Triangle.withSurfaceNormals(
        material,
        new Vector(1, 0, 0),
        new Vector(0, 1, 0),
        new Vector(0, 0, 1));
  }

  /**
   * Perform Moller-Trumbore alogorithm for efficient ray-triangle intersection, described at:
   * https://www.scratchapixel.com/lessons/3d-basic-rendering/ray-tracing-rendering-a-triangle/moller-trumbore-ray-triangle-intersection
   */
  @Override
  Optional<RayHit> internalIntersectInObjectSpace(Ray ray) {
    NUM_TRIANGLE_TESTS.getAndIncrement();

    Vector p0p1 = vertexes[1].subtract(vertexes[0]);
    Vector p0p2 = vertexes[2].subtract(vertexes[0]);

    Vector pVec = ray.getDirection().cross(p0p2);
    double determinant = p0p1.dot(pVec);

    // Ray and triangle are parallel if determinant is too close to zero.
    if (abs(determinant) < EPSILON) {
      return Optional.empty();
    }

    double inverseDeterminant = 1 / determinant;

    // Compute barycentric coordinates.
    Vector tVec = ray.getStart().subtract(vertexes[0]);
    Vector qVec = tVec.cross(p0p1);

    double u = tVec.dot(pVec) * inverseDeterminant;
    if (u < 0 || u > 1) { return Optional.empty(); }

    double v = ray.getDirection().dot(qVec) * inverseDeterminant;
    if (v < 0 || u + v > 1) { return Optional.empty(); }

    double t = p0p2.dot(qVec) * inverseDeterminant;
    if (t < 0) {
      return Optional.empty();
    }

    Vector normal = vertexNormals.length == 3
        ? interpolateNormals(vertexNormals, u, v)
        : p0p1.cross(p0p2).normalize();

    NUM_TRIANGLE_INTERSECTIONS.getAndIncrement();

    return Optional.of(ImmutableRayHit.builder()
        .setObject(this)
        .setTime(t)
        .setNormal(normal)
        .setMaterial(material)
        .setRay(ray)
        .build());
  }

  @Override
  public Triangle[] getTriangles() {
    return new Triangle[]{this};
  }

  @Override
  public Vector minBound() {
    return this.minBound;
  }

  @Override
  public Vector maxBound() {
    return this.maxBound;
  }

  @Override
  public String toString() {
    return String.format("Triangle[%s %s %s]", vertexes[0], vertexes[1], vertexes[2]);
  }

  public Vector[] getVertexes() {
    return this.vertexes;
  }

  private static Vector interpolateNormals(Vector[] normals, double u, double v) {
    double w = 1 - u - v;
    checkArgument(0 <= w && w <= 1);

    return normals[0].scale(w).add(normals[1].scale(u)).add(normals[2].scale(v));
  }


  private double[][] computeMinMaxBounds() {
    double[] minXyz = new double[3];
    Arrays.fill(minXyz, POSITIVE_INFINITY);
    double[] maxXyz = new double[3];
    Arrays.fill(maxXyz, NEGATIVE_INFINITY);

    for (Vector vertex : vertexes) {
      for (int i = 0; i < 3; i++) {
        if (vertex.getComponent(i) < minXyz[i]) {
          minXyz[i] = vertex.getComponent(i);
        }
        if (vertex.getComponent(i) > maxXyz[i]) {
          maxXyz[i] = vertex.getComponent(i);
        }
      }
    }
    return new double[][]{minXyz, maxXyz};
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Triangle triangle = (Triangle) o;
    return Arrays.equals(vertexes, triangle.vertexes) &&
        Arrays.equals(vertexNormals, triangle.vertexNormals);
  }

  @Override
  public int hashCode() {
    int result = Arrays.hashCode(vertexes);
    result = 31 * result + Arrays.hashCode(vertexNormals);
    return result;
  }
}
