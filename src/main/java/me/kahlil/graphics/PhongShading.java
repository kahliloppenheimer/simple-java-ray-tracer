package me.kahlil.graphics;

import static me.kahlil.graphics.RayIntersections.findAllIntersections;

import com.google.common.collect.ImmutableList;
import me.kahlil.geometry.LightSphere;
import me.kahlil.geometry.Ray;
import me.kahlil.geometry.RayHit;
import me.kahlil.geometry.Vector;
import me.kahlil.scene.Camera;
import me.kahlil.scene.PointLight;
import me.kahlil.scene.Material;
import me.kahlil.scene.Scene;

/**
 * An implementation of the phong illumination model implementation of shading.
 */
public final class PhongShading implements Shader {

  private Scene scene;
  private final Camera camera;
  private final boolean shadowsEnabled;

  public PhongShading(
      Scene scene,
      Camera camera,
      boolean shadowsEnabled
  ) {
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
    Color lighted = scene.getAmbient().multiply(material.getColor());
    for (PointLight light : scene.getLights()) {
      // Check to see if shadow should be cast
      if (!shadowsEnabled || !isObjectBetweenLightAndPoint(light, rayHit.getIntersection())) {
        lighted = lighted.add(light.phongIllumination(rayHit, camera.getLocation()));
      }
    }
    return lighted;
  }

  private Color shadeLightSphere(RayHit rayHit) {
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
