package me.kahlil.scene;

import me.kahlil.graphics.Color;

/**
 * A representation of a simple light source with a location and intensity.
 */
public class Light3D {

  // Percentage brightness that specular lighting should get. Lower numbers
  // yield a "duller" effect, while higher numbers yield a more artificial
  // "plastic-y" effect
  private static final double SPECULAR_INTENSITY = .25;

  private final Vector location;
  private final Color color;

  public Light3D(Vector location, Color color) {
    this.location = location;
    this.color = color;
  }

  /**
   * Returns the diffuse lighting value given a vector from the point on the object to the light
   * source and the normal vector to that point from the object.
   */
  double diffuse(RayHit rayHit) {
    Vector point = rayHit.getIntersection();
    Vector normal = rayHit.getNormal();
    Vector lightVector = getLocation().subtract(point);

    if (lightVector.magnitude() != 1) {
      lightVector = lightVector.normalize();
    }

    if (normal.magnitude() != 1) {
      normal = normal.normalize();
    }

    return Math.max(lightVector.dot(normal), 0);
  }

  /**
   * Returns the specular light at a given RayHit with the given light and eye positions.
   */
  double specular(Vector eyePos, RayHit rayHit) {
    Vector lightVec = getLocation().subtract(rayHit.getIntersection()).normalize();
    Vector eyeVec = eyePos.subtract(rayHit.getIntersection()).normalize();
    Vector normal = rayHit.getNormal();
    Vector lProjectedOntoN = normal.scale(lightVec.dot(normal));
    Vector lProjectedOntoPlane = lightVec.subtract(lProjectedOntoN);
    Vector reflectedLight = lightVec.subtract(lProjectedOntoPlane.scale(2)).normalize();
    return Math.pow(Math.max(
        reflectedLight.dot(eyeVec), 0),
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
    if (specularCoefficient * SPECULAR_INTENSITY > diffuseCoefficient) {
      diffuseCoefficient = 1 - SPECULAR_INTENSITY * specularCoefficient;
    }
    Material material = rayHit.getObject().getOutsideMaterial();
    return material.getColor().multiply(getColor()).scaleFloat((float) diffuseCoefficient)
        .add(getColor().scaleFloat((float) specularCoefficient)
            .scaleFloat((float) material.getSpecularIntensity()));
  }
}