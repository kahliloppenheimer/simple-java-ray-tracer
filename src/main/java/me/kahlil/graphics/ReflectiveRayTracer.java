package me.kahlil.graphics;

import java.util.Optional;
import me.kahlil.geometry.Vector;
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

  private static final double EPSILON = 0.0000000001;
  private final Scene3D scene;
  private final int maxRayDepth;

  /**
   * Constructs a ReflectiveRayTracer with a given maxRayDepth, indicating the maximum number
   * of recursive rays that should be traced for reflections.
   */
  ReflectiveRayTracer(
      Scene3D scene,
      SimpleFrame frame,
      Camera3D camera,
      boolean shadowsEnabled,
      int maxRayDepth) {
    super(scene, frame, camera, shadowsEnabled);
    this.scene = scene;
    this.maxRayDepth = maxRayDepth;
  }

  @Override
  Color traceRay(Ray3D ray) {
    return recursiveTraceRay(ray, 0);
  }

  private Color recursiveTraceRay(Ray3D ray, int rayDepth) {
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
      return super.computeShading(rayHit.get()).scaleFloat(lossToReflection);
    }
    return recursiveTraceRay(computeReflectionRay(rayHit.get()), rayDepth + 1);
  }

  /**
   * Math for computing the reflection ray R can be expressed with the following formula,
   * given an incident Ray I and a normal ray N at the point of intersection N:
   *
   * R = I - 2 * (I dot N) * N
   *
   * Sources:
   * https://www.scratchapixel.com/lessons/3d-basic-rendering/introduction-to-shading/reflection-refraction-fresnel
   * http://web.cse.ohio-state.edu/~shen.94/681/Site/Slides_files/reflection_refraction.pdf
   */
  private static Ray3D computeReflectionRay(RayHit rayHit) {
    Vector incident = rayHit.getRay().getDirection();
    Vector normal = rayHit.getNormal();
    Vector reflection = incident.subtract(normal.scale(2 * incident.dot(normal)));
    Vector perturbedStartingPoint = rayHit.getIntersection().add(reflection.scale(EPSILON));
    return new Ray3D(perturbedStartingPoint, reflection);
  }

}
