package me.kahlil.geometry;

import java.util.Optional;
import me.kahlil.scene.Material;

public class BoundedPlane3D extends Plane3D {

  private final Vector minCoordinates;
  private final Vector maxCoordinates;

  public BoundedPlane3D(
      Vector normal, Vector minCoordinates, Vector maxCoordinates, Material front) {
    super(normal, front);
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
