package me.kahlil.graphics;

import java.util.Optional;
import me.kahlil.geometry.Object3D;
import me.kahlil.geometry.Ray3D;
import me.kahlil.geometry.RayHit;
import me.kahlil.geometry.Vector;
import me.kahlil.scene.Camera3D;
import me.kahlil.scene.Light3D;
import me.kahlil.scene.Material;
import me.kahlil.scene.Scene3D;
import me.kahlil.scene.SimpleFrame;

/**
 * An object which traces a single ray and returns the corresponding color that should be rendered,
 * as defined by the ray tracing algorithm.
 */
abstract class RayTracer {

  private final Camera3D camera;
  private final SimpleFrame frame;
  private final Scene3D scene;
  private final boolean shadowsEnabled;

  RayTracer(
      Scene3D scene,
      SimpleFrame frame,
      Camera3D camera,
      boolean shadowsEnabled
  ) {
    this.camera = camera;
    this.frame = frame;
    this.scene = scene;
    this.shadowsEnabled = shadowsEnabled;
  }

  /**
   * Traces the given ray, returning the corresponding color. Note, this is called with a ray that
   * points to the middle of a given pixel during the main ray tracing algorithm.
   */
  abstract Color traceRay(Ray3D ray);

  /**
   * Traces a ray through ith and jth pixel, returning a color for that pixel.
   */
  final Color traceRay(int i, int j) {
    return traceRay(new Ray3D(camera.getLocation(),
        frame.getBottomLeftCorner()
            .translate(
                // Translate into coordinate space and center
                i * frame.getPixelWidthInCoordinateSpace()
                    + 0.5 * frame.getPixelWidthInCoordinateSpace(),
                j * frame.getPixelHeightInCoordinateSpace()
                    + 0.5 * frame.getPixelHeightInCoordinateSpace())));
  }

  Color computeShading(RayHit closest) {
    Material material = closest.getObject().getOutsideMaterial();
    // Initialize color with ambient light
    Color lighted = scene.getAmbient().multiply(material.getColor());
    for (Light3D light : scene.getLights()) {
      // Check to see if shadow should be cast
      if (!shadowsEnabled || !isObjectBetweenLightAndPoint(light, closest.getIntersection())) {
        lighted = lighted.add(light.phongIllumination(closest, camera.getLocation()));
      }
    }
    return lighted;
  }

  /**
   * Returns true iff there is an object in the scene between the light and the given
   * point.
   */
  boolean isObjectBetweenLightAndPoint(Light3D l, Vector point) {
    Vector shadowVec = l.getLocation().subtract(point);
    Optional<RayHit> closestHit = findFirstIntersection(new Ray3D(point.add(shadowVec.scale(.0001)), shadowVec), scene);
    return closestHit.isPresent() && closestHit.get().getDistance() < shadowVec.magnitude();
  }

  /**
   * Returns the RayHit with the lowest distance from the visionVec to each obj in the scene.
   * Returns optional.empty() if no object is hit.
   */
  Optional<RayHit> findFirstIntersection(Ray3D visionVec, Scene3D scene) {
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

  public Scene3D getScene() {
    return scene;
  }

  public boolean isShadowsEnabled() {
    return shadowsEnabled;
  }
}
