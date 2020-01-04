package me.kahlil.geometry;

import static me.kahlil.geometry.Constants.EPSILON;

import java.util.Optional;
import me.kahlil.scene.Material;

/**
 * Representation of a plane in 3-dimensional space.
 *
 * <p>All planes initially pass through the origin, but may be transformed.
 */
public class Plane extends Shape {
  private final Vector normal;
  private final Vector point;
  private final Material material;

  public Plane(Vector point, Vector normal, Material front) {
    this.normal = normal.normalize();
    this.point = point;
    this.material = front;
  }

  @Override
  protected Optional<RayHit> internalIntersectInObjectSpace(Ray ray) {

    double denominator = ray.getDirection().dot(normal);
    if (Math.abs(denominator) < EPSILON) {
      return Optional.empty();
    }

    double time = (point.subtract(ray.getStart())).dot(normal) / denominator;
    if (time < 0.0) {
      return Optional.empty();
    }

    // Check if the ray hit the back of the plane, and we need to invert the normal.
    boolean hitBackOfPlane = denominator > 0;
    Vector adjustedNormal = hitBackOfPlane ? this.normal.scale(-1) : this.normal;

    return Optional.of(
        ImmutableRayHit.builder()
            .setRay(ray)
            .setTime(time)
            .setNormal(adjustedNormal)
            .setObject(this)
            .setMaterial(material)
            .build());
  }

}
