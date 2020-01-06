package me.kahlil.geometry;

import java.util.Optional;

/** Representation of a geometric object which can describe its intersections with Rays. */
public interface Intersectable {

  /**
   * Returns a {@link Optional<RayHit>} describing the intersection if it ocurred, and {@link
   * Optional#empty otherwise}.
   */
  Optional<RayHit> intersectWith(Ray ray);
}
