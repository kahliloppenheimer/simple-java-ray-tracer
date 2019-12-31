package me.kahlil.geometry;

/**
 * Representation of a ray in 3-dimensional space.
 *
 * <p>A ray is simply represented as a Vector of the starting point of the ray, and a vector of the
 * direction of the ray.
 */
public class Ray {
  private final Vector start;
  private final Vector direction;
  private final Vector invertedDirection;

  /**
   * This represents a 3D ray with a specified start and direction. The direction of a ray is a
   * normalized vector.
   */
  public Ray(Vector start, Vector direction) {
    direction = direction.normalize();
    this.start = new Vector(start.getX(), start.getY(), start.getZ(), 1);
    this.direction = new Vector(direction.getX(), direction.getY(), direction.getZ(), 0);
    this.invertedDirection =
        new Vector(1.0 / direction.getX(), 1.0 / direction.getY(), 1.0 / direction.getZ());
  }

  /** Returns the point along the ray, t units from its origin p */
  Vector atTime(double t) {
    return start.add(direction.scale(t));
  }

  /** Returns the value of t at which this.atTime() should yield point. */
  double timeToPoint(Vector point) {
    Vector distanceBetween = point.subtract(this.getStart());
    if (Math.abs(this.direction.getX()) > 0.0) {
      return distanceBetween.getX() / this.direction.getX();
    }
    if (Math.abs(this.direction.getY()) > 0.0) {
      return distanceBetween.getY() / this.direction.getY();
    }
    if (Math.abs(this.direction.getZ()) > 0.0) {
      return distanceBetween.getZ() / this.direction.getZ();
    }
    throw new IllegalStateException("This ray has invalid direction vector: " + this.direction);
  }

  public Vector getStart() {
    return start;
  }

  public Vector getDirection() {
    return direction;
  }

  public Vector getInvertedDirection() { return invertedDirection; }

  public String toString() {
    return String.format("start = %s, direction = %s", start, direction);
  }
}
