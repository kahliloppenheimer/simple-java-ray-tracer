package me.kahlil.scene;

import static com.google.common.base.Preconditions.checkArgument;

import me.kahlil.geometry.Vector;
import org.immutables.value.Value.Check;

/** A representation of a camera positioned at a certain point in the scene. */

public interface Camera {

  /**
   * Indicates the position of the camera.
   */
  Vector getLocation();

  /**
   * Represents the Field of Vision (FOV) of the camera.
   */
  double getFieldOfVisionDegrees();

  @Check
  default void checkPreconditions() {
    double fieldOfVisionDegrees = getFieldOfVisionDegrees();
    checkArgument(0 < fieldOfVisionDegrees && fieldOfVisionDegrees < 180,
        "Field of vision must be between 1 and 179 degrees, but was: %f", fieldOfVisionDegrees);
  }
}
