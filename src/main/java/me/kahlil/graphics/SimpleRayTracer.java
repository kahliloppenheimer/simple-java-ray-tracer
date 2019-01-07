package me.kahlil.graphics;

import static me.kahlil.graphics.RayIntersections.findFirstIntersection;

import java.awt.Color;
import me.kahlil.geometry.Ray;
import me.kahlil.scene.Camera;
import me.kahlil.scene.Raster;
import me.kahlil.scene.Scene;

/** Ray tracer that performs single-intersection ray tracing (i.e. no reflection or refraction). */
class SimpleRayTracer extends RayTracer {

  private final Shader shader;
  private final Scene scene;
  private final ThreadLocal<Long> numTraces = ThreadLocal.withInitial(() -> 0L);

  SimpleRayTracer(
      Shader shader,
      Scene scene,
      Raster frame, Camera camera) {
    super(frame, camera);
    this.shader = shader;
    this.scene = scene;
  }

  @Override
  RenderingResult traceRay(Ray ray) {
    numTraces.set(numTraces.get() + 1);
    // Cast the ray from the camera to the pixel in the frame we are currently coloring,
    // and color the pixel based on the first object we hit (or the background if we hit none).
    Color shaded = findFirstIntersection(ray, scene)
        .map(shader::shade)
        .orElse(scene.getBackgroundColor());
    return ImmutableRenderingResult.builder()
        .setColor(shaded)
        .setNumRaysTraced(1)
        .build();
  }

  @Override
  long getNumTraces() {
    return numTraces.get();
  }
}
