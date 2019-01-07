package me.kahlil.geometry;

import static me.kahlil.geometry.Constants.EPSILON;
import static me.kahlil.geometry.Constants.ORIGIN;

import java.util.Optional;
import me.kahlil.scene.Material;

/**
 * Representation of a plane in 3-dimensional space.
 *
 * All planes initially pass through the origin, but may be transformed. */
public class Plane3D extends Shape {
  private final Vector normal;
  private final Vector point;
  private final Material front;

  public Plane3D(Vector normal, Material front) {
    this.normal = normal.normalize();
    this.point = ORIGIN;
    this.front = front;
  }

  @Override
  protected Optional<RayHit> intersectInObjectSpace(Ray ray) {

    double denominator = ray.getDirection().dot(normal);
    if (Math.abs(denominator) < EPSILON) {
      return Optional.empty();
    }

    double time = (point.subtract(ray.getStart())).dot(normal) / denominator;
    if (time < EPSILON) {
      return Optional.empty();
    }

    // Check if the ray hit the back of the plane, and we need to invert the normal.
    boolean hitBackOfPlane = denominator < EPSILON;
    Vector adjustedNormal = hitBackOfPlane ? this.normal.scale(-1) : this.normal;

    return Optional.of(
        ImmutableRayHit.builder()
            .setRay(ray)
            .setTime(time)
            .setDistance(ray.atTime(time).subtract(ray.getStart()).magnitude())
            .setIntersection(ray.atTime(time))
            .setNormal(adjustedNormal)
            .setObject(this)
            .build());
  }

  @Override
  public Material getInsideMaterial() {
    return front;
  }

  @Override
  public Material getOutsideMaterial() {
    return front;
  }
}
