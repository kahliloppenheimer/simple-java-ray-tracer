package me.kahlil.geometry;

import static me.kahlil.config.Counters.NUM_INTERSECTIONS;
import static me.kahlil.config.Counters.NUM_INTERSECTION_TESTS;

import java.util.Optional;
import me.kahlil.scene.Material;

/** A representation of a 3D object in the scene. */
public abstract class Shape implements Cloneable {

  private LinearTransformation transformation = LinearTransformation.IDENTITY;

  /** Finds the intersection of the given ray with this potentially transformed object */
  public Optional<RayHit> intersectWith(Ray ray) {
    // We first transform the ray into object space for this given object before computing
    // intersections.
    Ray objectSpaceRay =
        new Ray(
            worldToObjectSpace().apply(ray.getStart()),
            worldToObjectSpace().apply(ray.getDirection()));

    Optional<RayHit> maybeObjectSpaceIntersection = intersectInObjectSpace(objectSpaceRay);
    if (maybeObjectSpaceIntersection.isEmpty()) {
      return Optional.empty();
    }
    RayHit objectSpaceIntersection = maybeObjectSpaceIntersection.get();
    Vector worldSpaceIntersectionPoint =
        objectToWorldSpace().apply(objectSpaceIntersection.getIntersection());
    Vector worldSpaceNormal = normalsToWorldSpace().apply(objectSpaceIntersection.getNormal());
    return Optional.of(
        ImmutableRayHit.builder()
            .setRay(ray)
            .setTime(ray.timeToPoint(worldSpaceIntersectionPoint))
            .setNormal(worldSpaceNormal)
            .setObject(maybeObjectSpaceIntersection.get().getObject())
            .build());
  }

  /**
   * Computes the {@link RayHit} with this object and the given ray which is specified in object by
   * having the inverse transformation of this object applied to it. The resulting RayHit should
   * also be returned in object space.
   */
  final Optional<RayHit> intersectInObjectSpace(Ray ray) {
    NUM_INTERSECTION_TESTS.getAndIncrement();
    Optional<RayHit> rayHit = internalIntersectInObjectSpace(ray);
    if (rayHit.isPresent()) {
      NUM_INTERSECTIONS.getAndIncrement();
    }
    return rayHit;
  }

  abstract Optional<RayHit> internalIntersectInObjectSpace(Ray ray);

  /** Returns the Material of the outside of the shape */
  public abstract Material getOutsideMaterial();

  /** Returns the object-to-world space transformation currently applied to this object. */
  LinearTransformation getTransformation() {
    return this.transformation;
  }

  void setTransformation(LinearTransformation transformation) {
    this.transformation = transformation;
  }

  /** Transforms the object by the given linear transformation */
  public <V extends Shape> V transform(LinearTransformation lt) {
    try {
      V cloned = (V) this.clone();
      cloned.setTransformation(transformation.then(lt));
      return cloned;
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }
  }

  /** Returns the transformation from object space to world space. */
  private LinearTransformation objectToWorldSpace() {
    return transformation;
  }

  /** Returns the transformation from world space to object space. */
  private LinearTransformation worldToObjectSpace() {
    return transformation.inverse();
  }

  /** Returns the transformation from normals in object space back to world space. */
  private LinearTransformation normalsToWorldSpace() {
    return transformation.inverseTranspose();
  }
}
