package me.kahlil.geometry;

import me.kahlil.config.JavaStyle;
import me.kahlil.scene.Material;
import org.immutables.value.Value.Derived;
import org.immutables.value.Value.Immutable;

/** Represents the intersection of a ray with an object. */
@Immutable
@JavaStyle
public abstract class RayHit {

  // Ray involved in the collision.
  public abstract Ray getRay();

  // Time at which the ray intersected the object.
  public abstract double getTime();

  // Object that the ray intersects.
  public abstract Shape getObject();

  // Material at point of intersection.
  public abstract Material getMaterial();

  // Normal at the point of intersection.
  public abstract Vector getNormal();

  // Point at which the ray first intersects the object.
  @Derived
  public Vector getIntersection() {
    return getRay().atTime(getTime());
  }

  // Distance along ray to the first intersection.
  @Derived
  public double getDistance() {
    return getIntersection().subtract(getRay().getStart()).magnitude();
  }

}
