package me.kahlil.graphics;

import static me.kahlil.graphics.RayIntersections.findAllIntersections;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import java.awt.Color;
import me.kahlil.geometry.LightSphere;
import me.kahlil.geometry.Ray;
import me.kahlil.geometry.RayHit;
import me.kahlil.geometry.Vector;
import me.kahlil.scene.Camera;
import me.kahlil.scene.Material;
import me.kahlil.scene.PointLight;
import me.kahlil.scene.Scene;

/** An implementation of the phong illumination model implementation of shading. */
public final class PhongShading implements Shader {

  private Scene scene;
  private final Camera camera;
  private final boolean shadowsEnabled;

  public PhongShading(Scene scene, Camera camera, boolean shadowsEnabled) {
    this.scene = scene;
    this.camera = camera;
    this.shadowsEnabled = shadowsEnabled;
  }

  @Override
  public Color shade(RayHit rayHit) {
    // Perform custom logic for LightSpheres since they are exceptional
    if (rayHit.getObject() instanceof LightSphere) {
      return shadeLightSphere(rayHit);
    }
    Material material = rayHit.getObject().getOutsideMaterial();
    // Initialize color with ambient light
    Color lighted = ColorComputation.of(scene.getAmbient()).multiply(material.getColor()).compute();
    for (PointLight light : scene.getLights()) {
      // Check to see if shadow should be cast
      if (!shadowsEnabled || !isObjectBetweenLightAndPoint(light, rayHit.getIntersection())) {
        lighted = ColorComputation.of(lighted)
            .add(phongIllumination(light, rayHit, camera.getLocation()))
            .compute();
      }
    }
    return lighted;
  }


  /**
   * Returns the new color of a pixel given the color of the pixel that this light hits and the
   * diffuseCoefficient of that collision.
   */
  private static Color phongIllumination(PointLight light, RayHit rayHit, Vector cameraPosition) {
    double diffuseCoefficient = diffuse(light, rayHit);
    double specularCoefficient = specular(light, cameraPosition, rayHit);

    Material material = rayHit.getObject().getOutsideMaterial();
    return ColorComputation.of(light.getColor())
        .multiply(material.getColor())
        .scaleFloat((float) diffuseCoefficient)
        .add(ColorComputation.of(light.getColor())
            .scaleFloat((float) specularCoefficient)
            .scaleFloat((float) material.getSpecularIntensity())
            .compute())
        .compute();
  }

  /**
   * Returns the diffuse lighting value given a vector from the point on the object to the light
   * source and the normal vector to that point from the object.
   */
  @VisibleForTesting
  static double diffuse(PointLight light, RayHit rayHit) {
    Vector intersection = rayHit.getIntersection();
    Vector normal = rayHit.getNormal().normalize();
    Vector lightVector = light.getLocation().subtract(intersection).normalize();

    return Math.max(0, lightVector.dot(normal));
  }

  /** Returns the specular light at a given RayHit with the given light and eye positions. */
  @VisibleForTesting
  static double specular(PointLight light, Vector eyePos, RayHit rayHit) {
    Vector lightVec = light.getLocation().subtract(rayHit.getIntersection()).normalize();
    Vector eyeVec = eyePos.subtract(rayHit.getIntersection()).normalize();
    Vector normal = rayHit.getNormal();
    Vector lProjectedOntoN = normal.scale(lightVec.dot(normal));
    Vector lProjectedOntoPlane = lightVec.subtract(lProjectedOntoN);
    Vector reflectedLight = lightVec.subtract(lProjectedOntoPlane.scale(2)).normalize();
    return Math.pow(
        Math.max(reflectedLight.dot(eyeVec), 0),
        rayHit.getObject().getOutsideMaterial().getHardness());
  }

  private static Color shadeLightSphere(RayHit rayHit) {
    return new Color(1.0f, 1.0f, 1.0f);
  }

  /** Returns true iff there is an object in the scene between the light and the given point. */
  private boolean isObjectBetweenLightAndPoint(PointLight l, Vector point) {
    Vector shadowVec = l.getLocation().subtract(point);
    ImmutableList<RayHit> allIntersections =
        findAllIntersections(new Ray(point.add(shadowVec.scale(.0001)), shadowVec), scene);
    return allIntersections
        .stream()
        .filter(rayHit -> !(rayHit.getObject() instanceof LightSphere))
        .map(RayHit::getDistance)
        .anyMatch(distance -> distance < shadowVec.magnitude());
  }
}
