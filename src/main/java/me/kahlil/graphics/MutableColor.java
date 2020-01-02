package me.kahlil.graphics;

import java.awt.Color;
import java.util.Arrays;

/**
 * MutableColor representation that exposes a mutable float array to optimize performance.
 *
 * Computation around color can happen in-place, rather than through constructing unnecessary
 * copies of a MutableColor object for each intermediate step. This would not be possible with the native
 * Java color object.
 */
public class MutableColor implements Cloneable {

  private final float[] rgb;

  public MutableColor(float red, float green, float blue) {
    this(new float[]{red, green, blue});
  }

  public MutableColor(float[] rgb) {
    this.rgb = rgb;
  }

  public MutableColor(int red, int green, int blue) {
    this(new float[]{red / 255f, green / 255f, blue / 255f});
  }

  public MutableColor(int[] rgb) {
    this(rgb[0], rgb[1], rgb[2]);
  }

  /**
   * Returns a mutable reference to the RGB values of this color.
   */
  public float[] getRgb() {
    return this.rgb;
  }

  public void setRgb(float red, float green, float blue) {
    this.rgb[0] = red;
    this.rgb[1] = green;
    this.rgb[2] = blue;
  }

  public Color toColor() {
    return new Color(this.rgb[0], this.rgb[1], this.rgb[2]);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MutableColor that = (MutableColor) o;
    return Arrays.equals(rgb, that.rgb);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(rgb);
  }
}
