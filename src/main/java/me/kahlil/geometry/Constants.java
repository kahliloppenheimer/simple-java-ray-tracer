package me.kahlil.geometry;

public final class Constants {

  private Constants() { }

  /**
   * Represents a tiny delta used for approximating equality to zero.
   */
  public static final double EPSILON = 0.0000001;

  /**
   * Represents the origin of the x, y, z coordinate plane.
   */
  public static final Vector ORIGIN = new Vector(0.0, 0.0, 0.0);

}
