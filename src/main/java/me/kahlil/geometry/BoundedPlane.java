package me.kahlil.geometry;

import static me.kahlil.geometry.Constants.ORIGIN;

import java.util.Optional;
import me.kahlil.scene.Material;

public class BoundedPlane extends Plane {

  private final Vector minCoordinates;
  private final Vector maxCoordinates;

  public BoundedPlane(
      Vector normal, Vector minCoordinates, Vector maxCoordinates, Material front) {
    super(ORIGIN, normal, front);
    this.minCoordinates = minCoordinates;
    this.maxCoordinates = maxCoordinates;
  }

  @Override
  public Optional<RayHit> intersectInObjectSpace(Ray ray) {
    return super.intersectInObjectSpace(ray)
        .filter(rayHit -> isInBounds(rayHit.getIntersection(), minCoordinates, maxCoordinates));
  }

  private static boolean isInBounds(
      Vector intersection, Vector minCoordinates, Vector maxCoordinates) {
    double x = intersection.getX();
    double y = intersection.getY();
    double z = intersection.getZ();
    return x >= minCoordinates.getX()
        && x <= maxCoordinates.getX()
        && y >= minCoordinates.getY()
        && y <= maxCoordinates.getY()
        && z >= minCoordinates.getZ()
        && z <= maxCoordinates.getZ();
  }
}
