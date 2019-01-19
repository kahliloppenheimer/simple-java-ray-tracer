package me.kahlil.geometry;

import static me.kahlil.geometry.Constants.EPSILON;
import static java.awt.Color.WHITE;

import java.util.Optional;
import me.kahlil.scene.ImmutableMaterial;
import me.kahlil.scene.Material;

/**
 * Represents an object which is a single point. Only used for testing various properties of objects
 * like rotations. Should not actually be used when rendering scenes.
 */
public class PointObject extends Shape {

  private static final Material MATERIAL =
      ImmutableMaterial.builder()
          .setColor(WHITE)
          .setHardness(50)
          .setSpecularIntensity(1.0)
          .setReflective(false)
          .build();

  private final Sphere pointSphere;

  PointObject(double x, double y, double z) {
    this.pointSphere =
        new Sphere(new Vector(x, y, z), EPSILON, MATERIAL);
  }

  @Override
  protected Optional<RayHit> intersectInObjectSpace(Ray ray) {
    return pointSphere.intersectInObjectSpace(ray);
  }

  @Override
  public Material getOutsideMaterial() {
    return MATERIAL;
  }
}
