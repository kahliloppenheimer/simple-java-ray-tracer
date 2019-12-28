package me.kahlil.scene;

import static com.google.common.base.Preconditions.checkArgument;

import me.kahlil.config.JavaStyle;
import me.kahlil.geometry.Vector;
import org.immutables.value.Value;
import org.immutables.value.Value.Check;

/** Immutables implementation of {@link Camera}. */
@Value.Immutable
@JavaStyle
public interface Camera {

  Vector getLocation();

  double getFieldOfVisionDegrees();

  @Check
  default void checkPreconditions() {
    double fieldOfVisionDegrees = getFieldOfVisionDegrees();
    checkArgument(0 < fieldOfVisionDegrees && fieldOfVisionDegrees < 180,
        "Field of vision must be between 1 and 179 degrees, but was: %f", fieldOfVisionDegrees);
  }
}
