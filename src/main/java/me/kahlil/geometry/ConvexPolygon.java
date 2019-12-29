package me.kahlil.geometry;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Optional;
import me.kahlil.scene.Material;

/** Shape representing a convex polygon. */
public class ConvexPolygon extends Shape {

  private final Material material;
  private final Triangle[] triangles;

  public ConvexPolygon(Material material, Vector[] vertices) {
    checkArgument(
        vertices.length >= 3, "A polygon must have at least 3 vertices. Found %d", vertices.length);
    this.material = material;
    this.triangles = convertVerticesToTriangles(material, vertices);
  }

  public static ConvexPolygon square(Material material) {
    return new ConvexPolygon(material, new Vector[]{
        new Vector(-1, -1, 0),
        new Vector(1, -1, 0),
        new Vector(1, 1, 0),
        new Vector(-1, 1, 0)
    });
  }

  @Override
  Optional<RayHit> intersectInObjectSpace(Ray ray) {
    double minTime = Integer.MAX_VALUE;
    Optional<RayHit> closestHit = Optional.empty();
    for (Triangle triangle : triangles) {
      Optional<RayHit> rayHit = triangle.intersectInObjectSpace(ray);
      if (rayHit.isPresent()) {
        if (rayHit.get().getTime() < minTime) {
          closestHit = rayHit;
        }
      }
    }
    return closestHit;
  }

  @Override
  public Material getOutsideMaterial() {
    return this.material;
  }

  /**
   * Converts the given set of vertices into triangles using the simple algorithm described at:
   * https://www.scratchapixel.com/lessons/3d-basic-rendering/ray-tracing-polygon-mesh/polygon-to-triangle-mesh
   */
  private static Triangle[] convertVerticesToTriangles(Material material, Vector[] vertices) {
    Triangle[] triangles = new Triangle[vertices.length - 2];
    for (int i = 2; i < vertices.length; i++) {
      triangles[i - 2] =
          new Triangle(material, vertices[0], vertices[i - 1], vertices[i]);
    }
    return triangles;
  }
}
