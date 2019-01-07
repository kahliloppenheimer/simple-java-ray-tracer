package me.kahlil.geometry;

import me.kahlil.config.JavaStyle;
import org.immutables.value.Value.Immutable;

/** Represents the intersection of a ray with an object. */
@Immutable
@JavaStyle
public interface RayHit {
  // Ray that caused the rayHit
  Ray getRay();
  // time at which the ray intersected the object
  double getTime();
  // distance along ray to the first intersection
  double getDistance();
  // point at which the ray first intersects the object
  Vector getIntersection();
  // Normal vector to object at the point that the ray intersects the object
  Vector getNormal();
  // object that the ray intersects
  Shape getObject();
}
