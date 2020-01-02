package me.kahlil.scene;

import static me.kahlil.graphics.Colors.BLACK;
import static me.kahlil.graphics.Colors.GREEN;

public final class Materials {

  private Materials() { }

  public static Material REFLECTIVE = ImmutableMaterial.builder()
      .setColor(BLACK)
      .setReflectiveness(1.0)
      .setHardness(250)
      .setSpecularIntensity(1.0)
      .build();

  public static Material BASIC_GREEN = ImmutableMaterial.builder()
      .setColor(GREEN)
      .setHardness(200)
      .setSpecularIntensity(0.5f)
      .setReflectiveness(0.0)
      .build();

  /**
   * Returns an glossy material builder with no color specified.
   */
  public static ImmutableMaterial.Builder glossy() {
    return ImmutableMaterial.builder()
        .setHardness(10)
        .setSpecularIntensity(0.4)
        .setReflectiveness(0.0);
  }

  /**
   * Returns a shiny material builder with no color specified.
   */
  public static ImmutableMaterial.Builder shiny() {
    return ImmutableMaterial.builder()
        .setHardness(250)
        .setSpecularIntensity(1.0)
        .setReflectiveness(0.55);
  }

}
