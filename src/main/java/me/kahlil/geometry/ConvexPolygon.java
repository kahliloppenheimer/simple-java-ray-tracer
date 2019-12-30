package me.kahlil.geometry;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Arrays;
import java.util.Optional;
import me.kahlil.scene.Material;

/** Shape representing a convex polygon. */
public class ConvexPolygon extends Shape {

  private final Material material;
  private final Triangle[] triangles;

  public ConvexPolygon(Material material, Vector[] vertexes, int[] faces, int[] vertexIndexes) {
    checkArgument(
        vertexes.length >= 3,
        "A polygon must have at least 3 vertices. Found: %d",
        Arrays.toString(vertexes));
    checkArgument(faces.length > 0, "A convex polygon must have at least one face.");
    checkArgument(
        vertexIndexes.length > 0, "A convex polygon must have at least one vertex index.");
    this.material = material;
    this.triangles = convertVertexesToTriangles(material, vertexes, faces, vertexIndexes);
  }

  public static ConvexPolygon cube(Material material) {
    return new ConvexPolygon(
        material,
        new Vector[] {
          new Vector(-1, -1, 0),
          new Vector(1, -1, 0),
          new Vector(1, 1, 0),
          new Vector(-1, 1, 0),
          new Vector(-1, -1, -1),
          new Vector(1, -1, -1),
          new Vector(1, 1, -1),
          new Vector(-1, 1, -1),
        },
        new int[] {4, 4, 4, 4, 4, 4},
        new int[] {
          0, 1, 2, 3, // front face
          3, 2, 6, 7, // top face
          0, 4, 5, 1, // bottom face
          0, 3, 7, 4, // left face
          1, 5, 6, 2, // right face
          4, 5, 6, 7 // back face
        });
  }

  @Override
  Optional<RayHit> intersectInObjectSpace(Ray ray) {
    double minTime = Integer.MAX_VALUE;
    Optional<RayHit> closestHit = Optional.empty();
    for (Triangle triangle : triangles) {
      Optional<RayHit> rayHit = triangle.intersectInObjectSpace(ray);
      if (rayHit.isPresent()) {
        double time = rayHit.get().getTime();
        if (time < minTime) {
          minTime = time;
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
  private static Triangle[] convertVertexesToTriangles(
      Material material, Vector[] vertexes, int[] faces, int[] vertexIndexes) {
    int vertexIndex = 0;
    int trianglesIndex = 0;
    int numTriangles = computeNumTriangles(faces);
    Triangle[] triangles = new Triangle[numTriangles];
    for (int faceIndex = 0; faceIndex < faces.length; faceIndex++) {
      convertFaceToTriangles(
          triangles,
          material,
          vertexes,
          trianglesIndex,
          faces[faceIndex],
          vertexIndexes,
          vertexIndex);
      vertexIndex += faces[faceIndex];
      trianglesIndex += faces[faceIndex] - 2;
    }

    return triangles;
  }

  private static int computeNumTriangles(int[] faces) {
    int sum = 0;
    for (int i : faces) {
      checkArgument(i >= 3, "A face must have at least 3 vertices. Found: %d", i);
      sum += i - 2;
    }
    return sum;
  }

  /**
   * Converts the given geometric specification of a face (e.g. it's number of vertexes and the
   * vertex index array) into the triangles that can be drawn to represent it.
   *
   * <p>For efficiency, it mutates the triangle array input.
   */
  private static void convertFaceToTriangles(
      Triangle[] triangles,
      Material material,
      Vector[] vertexes,
      int trianglesIndex,
      int numVertexes,
      int[] vertexIndexes,
      int startingVertexIndex) {
    for (int i = 0; i < numVertexes - 2; i++) {
      triangles[trianglesIndex + i] =
          new Triangle(
              material,
              vertexes[vertexIndexes[startingVertexIndex]],
              vertexes[vertexIndexes[startingVertexIndex + i + 1]],
              vertexes[vertexIndexes[startingVertexIndex + i + 2]]);
    }
  }
}
