package me.kahlil.graphics;

import java.awt.Color;
import me.kahlil.geometry.RayHit;

public class NoShading implements Shader {

  @Override
  public Color shade(RayHit rayHit) {
    return rayHit.getObject().getOutsideMaterial().getColor();
  }
}
