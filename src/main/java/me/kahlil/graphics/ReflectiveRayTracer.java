package me.kahlil.graphics;

import static me.kahlil.graphics.RayIntersections.findFirstIntersection;

import java.awt.Color;
import java.util.Optional;
import me.kahlil.geometry.Ray;
import me.kahlil.geometry.RayHit;
import me.kahlil.geometry.Vector;
import me.kahlil.scene.Camera;
import me.kahlil.scene.Raster;
import me.kahlil.scene.Scene;

/**
 * Ray tracer that performs a simple implementation of reflection-based ray tracing (i.e. no
 * refraction or partial reflection).
 */
class ReflectiveRayTracer extends RayTracer {

  private static final double EPSILON = 0.0000000001;
  private final Shader shader;
  private final Scene scene;
  private final int maxRayDepth;

  private final ThreadLocal<Long> numTraces = ThreadLocal.withInitial(() -> 0L);

  /**
   * Constructs a ReflectiveRayTracer with a given maxRayDepth, indicating the maximum number of
   * recursive rays that should be traced for reflections.
   */
  ReflectiveRayTracer(
      Shader shader,
      Scene scene,
      Raster raster,
      Camera camera,
      int maxRayDepth) {
    super(raster, camera);
    this.shader = shader;
    this.scene = scene;
    this.maxRayDepth = maxRayDepth;
  }

  @Override
  RenderingResult traceRay(Ray ray) {
    return ImmutableRenderingResult.builder()
        .setColor(recursiveTraceRay(ray, 0))
        .setNumRaysTraced(numTraces.get())
        .build();
  }

  @Override
  long getNumTraces() {
    return numTraces.get();
  }

  private Color recursiveTraceRay(Ray ray, int rayDepth) {
    numTraces.set(numTraces.get() + 1);
    if (rayDepth > maxRayDepth) {
      return scene.getBackgroundColor();
    }
    Optional<RayHit> rayHit = findFirstIntersection(ray, scene);
    if (!rayHit.isPresent()) {
      return scene.getBackgroundColor();
    }
    if (!rayHit.get().getObject().getOutsideMaterial().isReflective()) {
      // if we reflected at all, we want to reduce the value by 20% to mimic imperfect reflection.
      float lossToReflection = rayDepth > 0 ? 0.8f : 1.0f;
      return ColorComputation.of(shader.shade(rayHit.get()))
          .scaleFloat(lossToReflection)
          .compute();
    }
    return recursiveTraceRay(computeReflectionRay(rayHit.get()), rayDepth + 1);
  }

  /**
   * Math for computing the reflection ray R can be expressed with the following formula, given an
   * incident Ray I and a normal ray N at the point of intersection N:
   *
   * <p>R = I - 2 * (I dot N) * N
   *
   * <p>Sources:
   * https://www.scratchapixel.com/lessons/3d-basic-rendering/introduction-to-shading/reflection-refraction-fresnel
   * http://web.cse.ohio-state.edu/~shen.94/681/Site/Slides_files/reflection_refraction.pdf
   */
  private static Ray computeReflectionRay(RayHit rayHit) {
    Vector incident = rayHit.getRay().getDirection();
    Vector normal = rayHit.getNormal();
    Vector reflection = incident.subtract(normal.scale(2 * incident.dot(normal)));
    Vector perturbedStartingPoint = rayHit.getIntersection().add(reflection.scale(EPSILON));
    return new Ray(perturbedStartingPoint, reflection);
  }
}
