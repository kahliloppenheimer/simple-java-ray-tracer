package me.kahlil.scene;

import java.awt.Color;
import me.kahlil.config.JavaStyle;
import me.kahlil.geometry.Vector;
import org.immutables.value.Value.Immutable;

/** A representation of a simple light source with a location and intensity. */
@Immutable
@JavaStyle
public interface PointLight {

  /** Returns the location in 3-dimensional space of this light. */
  Vector getLocation();

  /** Returns the color that this light emanates. */
  Color getColor();
}
