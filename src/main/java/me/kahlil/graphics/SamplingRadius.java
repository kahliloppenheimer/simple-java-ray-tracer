package me.kahlil.graphics;

import me.kahlil.config.JavaStyle;
import org.immutables.value.Value.Immutable;

/** A rectangular sampling area in which anti-aliasing methods can trace rays. */
@Immutable
@JavaStyle
interface SamplingRadius {

  double getHeight();

  double getWidth();
}
