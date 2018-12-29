package me.kahlil.scene;

import me.kahlil.geometry.RayHit;
import me.kahlil.geometry.Vector;
import me.kahlil.graphics.Color;

/** A representation of a simple light source with a location and intensity. */
public class PointLight {

  // Percentage brightness that specular lighting should get. Lower numbers
  // yield a "duller" effect, while higher numbers yield a more artificial
  // "plastic-y" effect
  private static final double SPECULAR_INTENSITY = .25;

  private final Vector location;
  private final Color color;

  public PointLight(Vector location, Color color) {
    this.location = location;
    this.color = color;
  }

  /**
   * Returns the diffuse lighting value given a vector from the point on the object to the light
   * source and the normal vector to that point from the object.
   */
  double diffuse(RayHit rayHit) {
    Vector intersection = rayHit.getIntersection();
    Vector normal = rayHit.getNormal().normalize();
    Vector lightVector = getLocation().subtract(intersection).normalize();

    return Math.max(0, lightVector.dot(normal));
  }

  /** Returns the specular light at a given RayHit with the given light and eye positions. */
  double specular(Vector eyePos, RayHit rayHit) {
    Vector lightVec = getLocation().subtract(rayHit.getIntersection()).normalize();
    Vector eyeVec = eyePos.subtract(rayHit.getIntersection()).normalize();
    Vector normal = rayHit.getNormal();
    Vector lProjectedOntoN = normal.scale(lightVec.dot(normal));
    Vector lProjectedOntoPlane = lightVec.subtract(lProjectedOntoN);
    Vector reflectedLight = lightVec.subtract(lProjectedOntoPlane.scale(2)).normalize();
    return Math.pow(
        Math.max(reflectedLight.dot(eyeVec), 0),
        rayHit.getObject().getOutsideMaterial().getHardness());
  }

  public Vector getLocation() {
    return location;
  }

  public Color getColor() {
    return color;
  }

  /**
   * Returns the new color of a pixel given the color of the pixel that this light hits and the
   * diffuseCoefficient of that collision.
   */
  public Color phongIllumination(RayHit rayHit, Vector cameraPosition) {
    double diffuseCoefficient = diffuse(rayHit);
    double specularCoefficient = specular(cameraPosition, rayHit);

    Material material = rayHit.getObject().getOutsideMaterial();
    return material
        .getColor()
        .multiply(getColor())
        .scaleFloat((float) diffuseCoefficient)
        .add(
            getColor()
                .scaleFloat((float) specularCoefficient)
                .scaleFloat((float) material.getSpecularIntensity()));
  }
}
