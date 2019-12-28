package me.kahlil.graphics;

import static java.lang.Math.tan;
import static java.lang.Math.toRadians;

import me.kahlil.scene.Camera;
import me.kahlil.scene.Raster;

final class CoordinateMapper {

  private static final double CAMERA_SPACE_MIN_VALUE = -1.0;

  private CoordinateMapper() {}

  /**
   * Converts a coordinate expressed by the pixels (i, j) in the raster into an (x, y) coordinate in
   * the camera space of the scene. The resultant point is centered in the pixel, transformed with
   * image aspect ratio, and transformed to account for field of view (FOV). The math is described
   * in full at:
   *
   * <p>https://www.scratchapixel.com/lessons/3d-basic-rendering/ray-tracing-generating-camera-rays/generating-camera-rays
   */
  static Point2D convertPixelToCameraSpaceCoordinates(
      Raster frame, Camera camera, int i, int j) {
    // Center and transform to Normalized Device Coordinates (i.e. (x, y) in range [0.0, 1.0])
    double x = (i + 0.5) / frame.getWidthPx();
    double y = (j + 0.5) / frame.getHeightPx();

    // Transform to Screen coordinates (i.e. (x, y) ranges between [-1.0, 1.0]).
    x = 2 * x - 1;
    // Inverse Y because pixel coordinates use increasing j to denote
    // lower placement in frame.
    y = 1 - 2 * y;

    // Apply aspect ratio to X because of asymmetry in number of x and y pixels.
    double aspectRatio = 1.0 * frame.getWidthPx() / frame.getHeightPx();
    x *= aspectRatio;

    // Apply Field of Vision (FOV) for zoomed in/out effect based on degrees of visibility.
    double fieldOfVisionMultiplier =
        tan(toRadians(camera.getFieldOfVisionDegrees() * 0.5));
    x *= fieldOfVisionMultiplier;
    y *= fieldOfVisionMultiplier;

    return ImmutablePoint2D.builder().setX(x).setY(y).build();
  }

  /**
   * Returns the width of a single pixel in camera space.
   */
  static double getPixelWidthInCameraSpace(Raster frame, Camera camera) {
    double middleOfZeroZeroInCameraSpace =
        convertPixelToCameraSpaceCoordinates(frame, camera, 0, 0).getX();

    return 2 * (middleOfZeroZeroInCameraSpace - CAMERA_SPACE_MIN_VALUE);
  }

  /**
   * Returns the height of a single pixel in camera space.
   */
  static double getPixelHeightInCameraSpace(Raster frame, Camera camera) {
    double middleOfZeroZeroInCameraSpace =
        convertPixelToCameraSpaceCoordinates(frame, camera, 0, 0).getY();

    return 2 * (-middleOfZeroZeroInCameraSpace - CAMERA_SPACE_MIN_VALUE);
  }
}
