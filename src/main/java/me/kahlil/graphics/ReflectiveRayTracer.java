package me.kahlil.graphics;

import static me.kahlil.config.Counters.NUM_TOTAL_RAYS;
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
public class ReflectiveRayTracer extends RayTracer {

  private static final double EPSILON = 0.0000000001;
  private final Shader shader;
  private final Scene scene;
  private final int maxRayDepth;

  /**
   * Constructs a ReflectiveRayTracer with a given maxRayDepth, indicating the maximum number of
   * recursive rays that should be traced for reflections.
   */
  public ReflectiveRayTracer(
      Shader shader, Scene scene, Raster raster, Camera camera, int maxRayDepth) {
    super(raster, camera);
    this.shader = shader;
    this.scene = scene;
    this.maxRayDepth = maxRayDepth;
  }

  @Override
  Color traceRay(Ray ray) {
    return recursiveTraceRay(ray, 1);
  }

  private Color recursiveTraceRay(Ray ray, int rayDepth) {
    NUM_TOTAL_RAYS.getAndIncrement();
    if (rayDepth > maxRayDepth) {
      return scene.getBackgroundColor();
    }
    Optional<RayHit> rayHit = findFirstIntersection(ray, scene);
    if (!rayHit.isPresent()) {
      return scene.getBackgroundColor();
    }
    double reflectiveness = rayHit.get().getObject().getOutsideMaterial().getReflectiveness();
    // If surface isn't reflective or we're already at max depth, simply return.
    if (reflectiveness < EPSILON || rayDepth == maxRayDepth) {
      return shader.shade(rayHit.get());
    }

    Color reflectedRayColor =
        ColorComputation.of(recursiveTraceRay(computeReflectionRay(rayHit.get()), rayDepth + 1))
            // Reduce effect of reflection by 20% to mimic imperfect reflection.
            .scaleFloat(0.8f)
            .scaleFloat((float) reflectiveness)
            .compute();

    // For purely reflective surfaces, simply return the reflected ray's color.
    if (Math.abs(reflectiveness - 1.0) < EPSILON) {
      return reflectedRayColor;
    }

    // For partially reflective surfaces, combine the reflected ray's color with the surface color.
    return ColorComputation.of(shader.shade(rayHit.get()))
        .scaleFloat(1.0f - (float) reflectiveness)
        .add(reflectedRayColor)
        .compute();
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
