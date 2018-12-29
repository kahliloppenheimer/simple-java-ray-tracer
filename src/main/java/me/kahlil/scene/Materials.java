package me.kahlil.scene;

import static me.kahlil.graphics.Color.GREEN;

public final class Materials {

  private Materials() { }

  public static Material BASIC_GREEN = ImmutableMaterial.builder()
      .setColor(GREEN)
      .setHardness(200)
      .setSpecularIntensity(0.5f)
      .setReflective(false)
      .build();
}
