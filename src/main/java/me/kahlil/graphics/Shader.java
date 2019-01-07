package me.kahlil.graphics;

import java.awt.Color;
import me.kahlil.geometry.RayHit;

/**
 * A shading model that knows how to determine the color of a particular {@link RayHit}.
 */
public interface Shader {

  Color shade(RayHit rayHit);

}
