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
      ImmutableCameraImpl.builder().setLocation(new Vector(0, 0, 0)).build();
}
