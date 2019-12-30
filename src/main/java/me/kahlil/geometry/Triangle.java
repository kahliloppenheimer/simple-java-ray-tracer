package me.kahlil.geometry;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.Math.abs;
import static me.kahlil.geometry.Constants.EPSILON;

import java.util.Arrays;
import java.util.Optional;
import me.kahlil.scene.Material;

public class Triangle extends Shape {

  private final Material material;

  // Array of size-3 containing the vertexes of the triangle.
  private final Vector[] vertexes;

  // Array of size 3 containing the vertex normals of the triangle.
  private final Vector[] vertexNormals;

  private Triangle(
      Material material,
      Vector[] vertexes,
      Vector[] vertexNormals) {
    checkArgument(vertexes.length == 3, "A triangle must have 3 vertexes. Found: %s", Arrays.toString(vertexes));
    checkArgument(vertexNormals.length == 0 || vertexNormals.length == 3, "A triangle must have no vertex normals or 3 0 surface normals. Found: %s", Arrays.toString(vertexes));
    this.material = material;
    this.vertexes = vertexes;
    this.vertexNormals = vertexNormals;
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
  Optional<RayHit> intersectInObjectSpace(Ray ray) {
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

    return Optional.of(ImmutableRayHit.builder()
        .setObject(this)
        .setTime(t)
        .setNormal(normal)
        .setRay(ray)
        .build());
  }

  @Override
  public Material getOutsideMaterial() {
    return this.material;
  }

  @Override
  public String toString() {
    return String.format("Triangle[%s %s %s]", vertexes[0], vertexes[1], vertexes[2]);
  }

  private static Vector interpolateNormals(Vector[] normals, double u, double v) {
    double w = 1 - u - v;
    checkArgument(0 <= w && w <= 1);

    return normals[0].scale(w).add(normals[1].scale(u)).add(normals[2].scale(v));
  }
}
