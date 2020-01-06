package me.kahlil.geometry;

import static me.kahlil.geometry.Constants.EPSILON;
import static me.kahlil.scene.Materials.DUMMY_MATERIAL;

import java.util.Optional;

/**
 * Represents an object which is a single point. Only used for testing various properties of objects
 * like rotations. Should not actually be used when rendering scenes.
 */
public class PointObject extends Shape {

  private final Sphere pointSphere;

  PointObject(double x, double y, double z) {
    this.pointSphere = new Sphere(new Vector(x, y, z), EPSILON, DUMMY_MATERIAL);
  }

  @Override
  protected Optional<RayHit> internalIntersectInObjectSpace(Ray ray) {
    return pointSphere.intersectInObjectSpace(ray);
  }

}
