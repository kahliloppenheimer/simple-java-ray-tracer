package me.kahlil.scene;

import me.kahlil.geometry.Vector;

/** A static helper class which contains useful camera constants. */
public final class Cameras {

  /**
   * The standard camera centered at the origin (0, 0, 0) which should be used for all ray tracing
   * as described at:
   *
   * <p>https://www.scratchapixel.com/lessons/3d-basic-rendering/ray-tracing-generating-camera-rays/generating-camera-rays
   */
  public static final Camera STANDARD_CAMERA =
      ImmutableCamera.builder()
          .setLocation(new Vector(0, 0, 0))
          .setFieldOfVisionDegrees(60)
          .build();

  /**
   * A camera centered at the origin (0, 0, 0) but with a 90 degree FOV, which is useful for testing
   * because the FOV effect (tan(FOV / 2)) is 1, and does not affect the coordinate computation.
   */
  public static final Camera NINETY_DEGREE_FOV =
      ImmutableCamera.builder()
          .setLocation(new Vector(0, 0, 0))
          .setFieldOfVisionDegrees(90)
          .build();
}
