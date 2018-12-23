package me.kahlil.graphics;

import java.util.Optional;
import me.kahlil.scene.Light3D;
import me.kahlil.scene.Material;
import me.kahlil.scene.Object3D;
import me.kahlil.scene.Ray3D;
import me.kahlil.scene.RayHit;
import me.kahlil.scene.Scene3D;
import me.kahlil.scene.Vector;

class BasicTracer extends RayTracer {

  private final Scene3D scene;
  private final Camera3D camera;
  private final SimpleFrame3D frame;
  private final boolean shadowsEnabled;

  BasicTracer(
      Scene3D scene,
      Camera3D camera,
      SimpleFrame3D frame,
      boolean shadowsEnabled) {
    super(camera, frame);

    this.scene = scene;
    this.camera = camera;
    this.frame = frame;
    this.shadowsEnabled = shadowsEnabled;
  }

  @Override
  public Color traceRay(Ray3D ray) {
    // Create the ray from the camera to the pixel in the frame we are currently coloring
    Optional<RayHit> optClosest = findFirstIntersection(ray, scene);
    // Color the pixel depending on which object was hit
    if (optClosest.isPresent()) {
      RayHit closest = optClosest.get();
      Material material = closest.getObj().getOutsideMaterial();
      // Initialize color with ambient light
      Color lighted = scene.getAmbient().multiply(material.getColor());
      for (Light3D light : scene.getLights()) {
        // Check to see if shadow should be cast
        if (!shadowsEnabled || !isObjectBetweenLightAndPoint(light, closest.getPoint())) {
          lighted = lighted.add(light.phongIllumination(closest, camera.getLocation()));
        }
      }
      return lighted;
    } else {
      return scene.getBackgroundColor();
    }
  }

  /**
   * Returns the RayHit with the lowest distance from the visionVec to each obj in the scene.
   * Returns optional.empty() if no object is hit.
   *
   * @param visionVec
   * @param scene
   * @return
   */
  private Optional<RayHit> findFirstIntersection(Ray3D visionVec, Scene3D scene) {
    RayHit closest = null;
    double closestDistance = Double.POSITIVE_INFINITY;
    for (Object3D o : scene.getObjects()) {
      Optional<RayHit> intersection = o.findIntersection(visionVec);
      if (intersection.isPresent() && intersection.get().getDistance() < closestDistance) {
        closestDistance = intersection.get().getDistance();
        closest = intersection.get();
      }
    }
    return Optional.ofNullable(closest);
  }

  /**
   * Returns true iff there is an object in the scene between the light and the given
   * point
   *
   * @param l
   * @param point
   * @return
   */
  private boolean isObjectBetweenLightAndPoint(Light3D l, Vector point) {
    Vector shadowVec = l.getLocation().subtract(point);
    Optional<RayHit> closestHit = findFirstIntersection(new Ray3D(point.add(shadowVec.scale(.0001)), shadowVec), scene);
    return closestHit.isPresent() && closestHit.get().getDistance() < shadowVec.magnitude();
  }
}
