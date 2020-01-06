package me.kahlil.geometry;

/**
 * Represents a shape that can be broken into {@link Triangle}s.
 */
public interface Polygon {

  /**
   * Returns all {@link Triangle}s that compose this shape.
   */
  Triangle[] getTriangles();

  /**
   * Returns the min bound of this shape.
   */
  Vector minBound();

  /**
   * Returns the max bound of this shape.
   */
  Vector maxBound();

}
