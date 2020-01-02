package me.kahlil.graphics;

import me.kahlil.graphics.MutableColor;
import me.kahlil.config.JavaStyle;
import org.immutables.value.Value.Immutable;

@Immutable
@JavaStyle
public interface RenderingResult {

  // Shaded color of intersection.
  MutableColor getColor();
  // Number of rays traced during the color computation for this pixel.
  long getNumRaysTraced();

}
