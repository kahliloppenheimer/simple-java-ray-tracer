package me.kahlil.graphics;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static me.kahlil.config.Counters.NUM_INTERSECTIONS_COMPUTED;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import com.google.common.primitives.Doubles;
import java.util.List;
import java.util.Optional;
import me.kahlil.geometry.LightSphere;
import me.kahlil.geometry.Ray;
import me.kahlil.geometry.RayHit;
import me.kahlil.scene.Scene;

/** Static helper class for determining ray intersections with a given scene. */
final class RayIntersections {

  private RayIntersections() {}

  /**
   * Returns the RayHit with the lowest distance from the visionVector to each obj in the scene.
   * Returns optional.empty() if no object is hit.
   */
  static Optional<RayHit> findFirstIntersection(Ray visionVector, Scene scene) {
    return findAllIntersections(visionVector, scene).stream()
        .min((rayHit1, rayHit2) -> Doubles.compare(rayHit1.getDistance(), rayHit2.getDistance()));
  }

  /** Returns all intersections the given ray has with the objects in the scene. */
  static ImmutableList<RayHit> findAllIntersections(Ray visionVector, Scene scene) {
    List<Optional<RayHit>> allIntersectionsConsidered =
        Streams.concat(scene.getShapes().stream(), scene.getLights().stream().map(LightSphere::new))
            .map(object -> object.intersectWith(visionVector))
            .collect(toImmutableList());

    NUM_INTERSECTIONS_COMPUTED.getAndAdd(allIntersectionsConsidered.size());

    return allIntersectionsConsidered.stream().flatMap(Optional::stream).collect(toImmutableList());
  }
}
