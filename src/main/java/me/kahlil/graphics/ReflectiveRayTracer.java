package me.kahlil.graphics;

import java.util.Optional;
import me.kahlil.scene.Camera3D;
import me.kahlil.geometry.Ray3D;
import me.kahlil.geometry.RayHit;
import me.kahlil.scene.Scene3D;
import me.kahlil.scene.SimpleFrame;

/**
 * Ray tracer that performs a simple implementation of reflection-based ray tracing (i.e. no
 * refraction or partial reflection).
 */
class ReflectiveRayTracer extends RayTracer {

  private final Scene3D scene;
  private final int maxReflectionsToTrace;

  ReflectiveRayTracer(
      Scene3D scene,
      SimpleFrame frame,
      Camera3D camera,
      boolean shadowsEnabled,
      int maxReflectionsToTrace) {
    super(scene, frame, camera, shadowsEnabled);
    this.scene = scene;
    this.maxReflectionsToTrace = maxReflectionsToTrace;
  }

  @Override
  Color traceRay(Ray3D ray) {
    // Cast the ray from the camera to the pixel in the frame we are currently coloring,
    // and color the pixel based on the first object we hit (or the background if we hit none).
    Optional<RayHit> rayHit = findFirstIntersection(ray, scene);
    if (!rayHit.isPresent()) {
      return scene.getBackgroundColor();
    }
//    if (rayHit.get().getObject().getOutsideMaterial().)
//        .map(this::computeShading)
//        .orElse(scene.getBackgroundColor());
    return null;
  }



  private Color recursiveTraceRay(Ray3D ray, int recursionDepth) {
    if (recursionDepth > maxReflectionsToTrace) {
      return scene.getBackgroundColor();
    }
    return null;
  }

}
