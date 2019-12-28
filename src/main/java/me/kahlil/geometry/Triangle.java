package me.kahlil.geometry;

import static java.lang.Math.abs;
import static me.kahlil.geometry.Constants.EPSILON;

import java.util.Optional;
import me.kahlil.scene.Material;

public class Triangle extends Shape {

  private final Material material;
  private final Vector p0;
  private final Vector p1;
  private final Vector p2;

  public Triangle(
      Material material,
      Vector p0,
      Vector p1,
      Vector p2) {
    this.material = material;
    this.p0 = p0;
    this.p1 = p1;
    this.p2 = p2;
  }

  /**
   * Perform Moller-Trumbore alogorithm for efficient ray-triangle intersection, described at:
   * https://www.scratchapixel.com/lessons/3d-basic-rendering/ray-tracing-rendering-a-triangle/moller-trumbore-ray-triangle-intersection
   */
  @Override
  Optional<RayHit> intersectInObjectSpace(Ray ray) {
    Vector p0p1 = p1.subtract(p0);
    Vector p0p2 = p2.subtract(p0);

    Vector pVec = ray.getDirection().cross(p0p2);
    double determinant = p0p1.dot(pVec);

    // Ray and triangle are parallel if determinant is too close to zero.
    if (abs(determinant) < EPSILON) {
      return Optional.empty();
    }

    double inverseDeterminant = 1 / determinant;

    // Compute barycentric coordinates.
    Vector tVec = ray.getStart().subtract(p0);
    Vector qVec = tVec.cross(p0p1);

    double u = tVec.dot(pVec) * inverseDeterminant;
    if (u < 0 || u > 1) { return Optional.empty(); }

    double v = ray.getDirection().dot(qVec) * inverseDeterminant;
    if (v < 0 || u + v > 1) { return Optional.empty(); }

    double t = p0p2.dot(qVec) * inverseDeterminant;
    return Optional.of(ImmutableRayHit.builder()
        .setObject(this)
        .setTime(t)
        .setNormal(p0p1.cross(p0p2))
        .setRay(ray)
        .build());
  }

  @Override
  public Material getOutsideMaterial() {
    return this.material;
  }
}
