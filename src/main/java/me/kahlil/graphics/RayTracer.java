package me.kahlil.graphics;

import static me.kahlil.graphics.CoordinateMapper.convertPixelToCameraSpaceCoordinates;

import me.kahlil.geometry.Ray;
import me.kahlil.geometry.Vector;
import me.kahlil.scene.Camera;
import me.kahlil.scene.Raster;

/**
 * An object which traces a single ray and returns the corresponding color that should be rendered,
 * as defined by the ray tracing algorithm.
 */
abstract class RayTracer {

  private final Camera camera;
  private final Raster raster;

  RayTracer(Raster raster, Camera camera) {
    this.camera = camera;
    this.raster = raster;
  }

  /**
   * Traces the given ray, returning the corresponding color. Note, this is called with a ray that
   * points to the middle of a given pixel during the main ray tracing algorithm.
   */
  abstract RenderingResult traceRay(Ray ray);

  /** Returns the number of rays traced thus far by this particular ray tracer instance. */
  abstract long getNumTraces();

  /** Traces a ray through ith and jth pixel, returning a color for that pixel. */
  final RenderingResult traceRay(int i, int j) {
    Point2D inCameraSpace = convertPixelToCameraSpaceCoordinates(raster, camera, i, j);
    return traceRay(
        new Ray(
            camera.getLocation(),
            new Vector(inCameraSpace.getX(), inCameraSpace.getY(), -1.0)
                .subtract(camera.getLocation())));
  }
}
