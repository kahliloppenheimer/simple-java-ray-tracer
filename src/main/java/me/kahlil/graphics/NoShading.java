package me.kahlil.graphics;

import me.kahlil.graphics.MutableColor;
import me.kahlil.geometry.RayHit;

public class NoShading implements Shader {

  @Override
  public MutableColor shade(RayHit rayHit) {
    return rayHit.getObject().getOutsideMaterial().getColor();
  }
}
