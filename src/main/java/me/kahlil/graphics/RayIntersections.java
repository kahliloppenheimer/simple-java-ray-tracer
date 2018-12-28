package me.kahlil.graphics;

import static com.google.common.collect.ImmutableList.toImmutableList;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import com.google.common.primitives.Doubles;
import java.util.Optional;
import me.kahlil.geometry.LightSphere;
import me.kahlil.geometry.Ray3D;
import me.kahlil.geometry.RayHit;
import me.kahlil.scene.Scene;

/**
 * Static helper class for determining ray intersections with a given scene.
 */
final class RayIntersections {

  private RayIntersections() {}

  /**
   * Returns the RayHit with the lowest distance from the visionVector to each obj in the scene.
   * Returns optional.empty() if no object is hit.
   */
  static Optional<RayHit> findFirstIntersection(Ray3D visionVector, Scene scene) {
    return findAllIntersections(visionVector, scene)
        .stream()
        .min((rayHit1, rayHit2) -> Doubles.compare(rayHit1.getDistance(), rayHit2.getDistance()));
  }

  /** Returns all intersections the given ray has with the objects in the scene. */
  static ImmutableList<RayHit> findAllIntersections(Ray3D visionVector, Scene scene) {
    return Streams.concat(
        scene.getObjects().stream(), scene.getLights().stream().map(LightSphere::new))
        .map(object -> object.findIntersection(visionVector))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(toImmutableList());
  }
}
