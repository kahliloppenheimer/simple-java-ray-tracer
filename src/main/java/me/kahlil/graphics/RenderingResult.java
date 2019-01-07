package me.kahlil.graphics;

import java.awt.Color;
import me.kahlil.config.JavaStyle;
import org.immutables.value.Value.Immutable;

@Immutable
@JavaStyle
public interface RenderingResult {

  Color getColor();
  long getNumRaysTraced();

}
