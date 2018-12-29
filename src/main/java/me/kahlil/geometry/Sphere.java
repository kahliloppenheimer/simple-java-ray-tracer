package me.kahlil.geometry;

import static me.kahlil.geometry.Constants.ORIGIN;

import java.util.Optional;
import me.kahlil.scene.Material;

/**
 * A representation of a unit sphere in 3D space.
 *
 * <p>The unit sphere is centered at the origin with radius 1.0. Any transformations (rotate, scale,
 * translate) should be performed using {@link #transform}.
 */
public class Sphere extends Shape {

  private final Vector center;
  private final double radius;
  // Material of the inside of the sphere
  private final Material inside;
  // Material of the outside of the sphere
  private final Material outside;

  public Sphere(Material material) {
    this(ORIGIN, 1.0, material);
  }

  public Sphere(Vector center, double radius, Material material) {
    this.center = center;
    this.radius = radius;
    this.inside = material;
    this.outside = material;
  }

  @Override
  public Optional<RayHit> intersectInObjectSpace(Ray ray) {
    // coefficients for the quadratic equation we have to solve to find the intersection
    // ax^2 + bx + c = 0
    double a = Math.pow(ray.getDirection().magnitude(), 2);
    double b = ray.getDirection().scale(2).dot(ray.getStart().subtract(center));
    double c = Math.pow(ray.getStart().subtract(center).magnitude(), 2) - Math.pow(radius, 2);

    double determinant = Math.pow(b, 2) - 4 * a * c;
    double timeOfFirstIntersection = -1;

    // Potentially one intersection
    if (-.0000000001 <= determinant && determinant <= .000000001) {
      timeOfFirstIntersection = -1 * b / (2 * a);
    } // Potentially two intersections
    else if (determinant > 0) {
      double t1 = (-1 * b - Math.sqrt(determinant)) / (2 * a);
      double t2 = (-1 * b + Math.sqrt(determinant)) / (2 * a);
      timeOfFirstIntersection = t1 > 0 && t2 > 0 ? t1 : t2;
    }

    if (timeOfFirstIntersection > 0) {
      Vector intersection = ray.atTime(timeOfFirstIntersection);
      double distance = ray.getStart().subtract(intersection).magnitude();
      Vector normal = intersection.subtract(center).normalize();
      return Optional.of(
          ImmutableRayHit.builder()
              .setRay(ray)
              .setTime(timeOfFirstIntersection)
              .setDistance(distance)
              .setIntersection(intersection)
              .setNormal(normal)
              .setObject(this)
              .build());
    } else {
      return Optional.empty();
    }
  }

  @Override
  public Material getInsideMaterial() {
    return this.inside;
  }

  @Override
  public Material getOutsideMaterial() {
    return this.outside;
  }

}
