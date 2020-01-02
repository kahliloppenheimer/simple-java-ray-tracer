package me.kahlil.graphics;

import me.kahlil.graphics.MutableColor;
import me.kahlil.geometry.RayHit;

/**
 * A shading model that knows how to determine the color of a particular {@link RayHit}.
 */
public interface Shader {

  MutableColor shade(RayHit rayHit);

}
