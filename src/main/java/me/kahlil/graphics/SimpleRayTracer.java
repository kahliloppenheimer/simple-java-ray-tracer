package me.kahlil.graphics;

import java.util.Optional;
import me.kahlil.scene.Camera3D;
import me.kahlil.geometry.Ray3D;
import me.kahlil.geometry.RayHit;
import me.kahlil.scene.Scene3D;
import me.kahlil.scene.SimpleFrame;

/** Ray tracer that performs single-intersection ray tracing (i.e. no reflection or refraction). */
class SimpleRayTracer extends RayTracer {

  private final Scene3D scene;

  SimpleRayTracer(
      Scene3D scene,
      SimpleFrame frame,
      Camera3D camera,
      boolean shadowsEnabled) {
    super(scene, frame, camera, shadowsEnabled);
    this.scene = scene;
  }

  @Override
  Color traceRay(Ray3D ray) {
    // Cast the ray from the camera to the pixel in the frame we are currently coloring,
    // and color the pixel based on the first object we hit (or the background if we hit none).
    return findFirstIntersection(ray, scene)
        .map(this::computeShading)
        .orElse(scene.getBackgroundColor());
  }

}
