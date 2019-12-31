package me.kahlil.graphics;

import java.awt.Color;
import me.kahlil.config.JavaStyle;
import org.immutables.value.Value.Immutable;

@Immutable
@JavaStyle
public interface RenderingResult {

  // Shaded color of intersection.
  Color getColor();
  // Number of rays traced during the color computation for this pixel.
  long getNumRaysTraced();

}
