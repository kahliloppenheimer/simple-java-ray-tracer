package me.kahlil.scene;

import static com.google.common.base.Preconditions.checkArgument;

import me.kahlil.config.JavaStyle;
import me.kahlil.graphics.Color;
import org.immutables.value.Value.Check;
import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

/**
 * A representation of the material of a given shape.
 */
@Immutable
@JavaStyle
public interface Material {

  /**
   * The color of the material.
   */
  Color getColor();

  /**
   * An integer between 1 (inclusive) and 511 (inclusive) indicating the "hardness" of a meterial,
   * which factors into specular lighting computation.
   */
  int getHardness();

  /**
   * The specular intensity of a material (i.e. "shiny-ness") which is specified as a double
   * between 0.0 (inclusive) and 1.0 (inclusive).
   */
  double getSpecularIntensity();

  /**
   * True iff the object has a reflective surface (i.e. rays should bounce off of the surface for
   * shading computation).
   */
  @Default
  default boolean isReflective() {
    return false;
  }

  @Check
  default void checkPreconditions() {
    checkArgument(getHardness() > 0 && getHardness() < 512,
        "Hardness must be an integer between 1 (inclusive) and 511 (inclusive) but was %d",
        getHardness());
    checkArgument(getSpecularIntensity() >= 0.0 && getSpecularIntensity() <= 1.0,
        "Specular intensity must be between 0.0 (inclusive) and 1.0 (inclusive) but was %f",
        getSpecularIntensity());
  }
}
