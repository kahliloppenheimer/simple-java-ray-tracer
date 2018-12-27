package me.kahlil.graphics;

import static com.google.common.collect.ImmutableList.toImmutableList;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import com.google.common.primitives.Doubles;
import java.util.Optional;
import me.kahlil.geometry.LightSphere;
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

  RayTracer(Scene3D scene, SimpleFrame frame, Camera3D camera, boolean shadowsEnabled) {
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

  /** Traces a ray through ith and jth pixel, returning a color for that pixel. */
  final Color traceRay(int i, int j) {
    return traceRay(
        new Ray3D(
            camera.getLocation(),
            frame
                .getBottomLeftCorner()
                .translate(
                    // Translate into coordinate space and center
                    i * frame.getPixelWidthInCoordinateSpace()
                        + 0.5 * frame.getPixelWidthInCoordinateSpace(),
                    j * frame.getPixelHeightInCoordinateSpace()
                        + 0.5 * frame.getPixelHeightInCoordinateSpace())));
  }

  /**
   * Computes the shading at a particular ray intersection with an object, returning a color based
   * on the lights in the scene, with which the given point should be shaded.
   */
  Color computeShading(RayHit rayHit) {
    // Perform custom logic for LightSpheres since they are exceptional
    if (rayHit.getObject() instanceof LightSphere) {
      return shadeLightSphere(rayHit);
    }
    Material material = rayHit.getObject().getOutsideMaterial();
    // Initialize color with ambient light
    Color lighted = scene.getAmbient().multiply(material.getColor());
    for (Light3D light : scene.getLights()) {
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
  private boolean isObjectBetweenLightAndPoint(Light3D l, Vector point) {
    Vector shadowVec = l.getLocation().subtract(point);
    ImmutableList<RayHit> allIntersections =
        findAllIntersections(new Ray3D(point.add(shadowVec.scale(.0001)), shadowVec), scene);
    return allIntersections
        .stream()
        .filter(rayHit -> !(rayHit.getObject() instanceof LightSphere))
        .map(RayHit::getDistance)
        .anyMatch(distance -> distance < shadowVec.magnitude());
  }

  /**
   * Returns the RayHit with the lowest distance from the visionVector to each obj in the scene.
   * Returns optional.empty() if no object is hit.
   */
  Optional<RayHit> findFirstIntersection(Ray3D visionVector, Scene3D scene) {
    return findAllIntersections(visionVector, scene)
        .stream()
        .min((rayHit1, rayHit2) -> Doubles.compare(rayHit1.getDistance(), rayHit2.getDistance()));
  }

  /** Returns all intersections the given ray has with the objects in the scene. */
  ImmutableList<RayHit> findAllIntersections(Ray3D visionVector, Scene3D scene) {
    return Streams.concat(
            scene.getObjects().stream(), scene.getLights().stream().map(LightSphere::new))
        .map(object -> object.findIntersection(visionVector))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(toImmutableList());
  }

  public Scene3D getScene() {
    return scene;
  }

  public boolean isShadowsEnabled() {
    return shadowsEnabled;
  }
}
