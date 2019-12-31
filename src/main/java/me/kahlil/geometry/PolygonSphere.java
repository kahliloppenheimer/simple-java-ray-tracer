package me.kahlil.geometry;

import java.util.Optional;
import me.kahlil.scene.Material;

/**
 * A procedurally generated representation of a sphere represented by polygons with radius 1.
 *
 * https://www.scratchapixel.com/lessons/3d-basic-rendering/ray-tracing-polygon-mesh/Ray-Tracing%20a%20Polygon%20Mesh-part-1
 */
public class PolygonSphere extends Shape {

  private final Material material;
  private final int numDivisions;
  private final ConvexPolygon polygon;
  private final boolean useVertexNormals;

  private PolygonSphere(Material material, int numDivisions, boolean useVertexNormals) {
    this.material = material;
    this.numDivisions = numDivisions;
    this.useVertexNormals = useVertexNormals;
    this.polygon = computePolygonSpecification(numDivisions);
  }

  public static PolygonSphere withSurfaceNormals(Material material, int numDivisions) {
    return new PolygonSphere(material, numDivisions, false);
  }

  public static PolygonSphere withVertexNormals(Material material, int numDivisions) {
    return new PolygonSphere(material, numDivisions, true);
  }

  @Override
  Optional<RayHit> internalIntersectInObjectSpace(Ray ray) {
    return polygon.intersectInObjectSpace(ray);
  }

  @Override
  public Material getOutsideMaterial() {
    return material;
  }

  /**
   * Generates polygon representation of sphere by following code example over at:
   * https://www.scratchapixel.com/lessons/3d-basic-rendering/ray-tracing-polygon-mesh/Ray-Tracing%20a%20Polygon%20Mesh-part-1
   */
  private ConvexPolygon computePolygonSpecification(int numDivisions) {
    int numVertexes = (numDivisions - 1) * numDivisions + 2;

    Vector[] vertexes = new Vector[numVertexes];
    Vector[] normals = new Vector[numVertexes];
    Vector[] barycentricCoordinates = new Vector[numVertexes];

    generateVertexes(vertexes, normals, barycentricCoordinates);

    int numPolygons = numDivisions * numDivisions;
    int[] faces = new int[numPolygons];
    int[] vertexIndexes = new int[(6 + (numDivisions - 1) * 4) * numDivisions];
    generateConnectivity(faces, vertexIndexes);

    return useVertexNormals
        ? ConvexPolygon.withVertexNormals(material, vertexes, vertexes, faces, vertexIndexes)
        : ConvexPolygon.withSurfaceNormals(material, vertexes, faces, vertexIndexes);
  }

  /**
   * Generates the connectivity polygon data to populate the faces and vertex indexes arrays.
   */
  private void generateConnectivity(int[] faces, int[] vertexIndexes) {
    int vid = 1;
    int numV = 0;
    int l = 0;
    int k = 0;

    for (int i = 0; i < numDivisions; i++) {
      for (int j = 0; j < numDivisions; j++) {
        if (i == 0) {
          faces[k++] = 3;
          vertexIndexes[l] = 0;
          vertexIndexes[l + 1] = j + vid;
          vertexIndexes[l + 2] = (j == (numDivisions - 1)) ? vid : j + vid + 1;
          l += 3;
        }
        else if (i == (numDivisions - 1)) {
          faces[k++] = 3;
          vertexIndexes[l] = j + vid + 1 - numDivisions;
          vertexIndexes[l + 1] = vid + 1;
          vertexIndexes[l + 2] = (j == (numDivisions - 1)) ? vid + 1 - numDivisions : j + vid + 2 - numDivisions;
          l += 3;
        }
        else {
          faces[k++] = 4;
          vertexIndexes[l] = j + vid + 1 - numDivisions;
          vertexIndexes[l + 1] = j + vid + 1;
          vertexIndexes[l + 2] = (j == (numDivisions - 1)) ? vid + 1 : j + vid + 2;
          vertexIndexes[l + 3] = (j == (numDivisions - 1)) ? vid + 1 - numDivisions : j + vid + 2 - numDivisions;
          l += 4;
        }
        numV++;
      }
      vid = numV;
    }
  }

  /**
   * Generates vertexes, normals, and barycentric coordinates for the sphere and stores them in
   * the passed arrays.
   */
  private void generateVertexes(Vector[] vertexes, Vector[] normals,
      Vector[] barycentricCoordinates) {
    double u = -1 * Math.PI / 2;
    double v = -1 * Math.PI;
    double du = Math.PI / numDivisions;
    double dv = 2 * Math.PI / numDivisions;

    vertexes[0] = new Vector(0, -1.0, 0);
    normals[0] = vertexes[0];

    int k = 1;
    for (int i = 0; i < numDivisions - 1; i++) {
      u += du;
      v = -1 * Math.PI;
      for (int j = 0; j < numDivisions; j++) {
        double x = Math.cos(u) * Math.cos(v);
        double y = Math.sin(u);
        double z = Math.cos(u) * Math.sin(v);

        vertexes[k] = new Vector(x, y, z);
        normals[k] = vertexes[k];

        barycentricCoordinates[k] = new Vector(u / Math.PI + 0.5, v * 0.5 / Math.PI + 0.5);
        v += dv;
        k++;
      }
    }
    vertexes[k] = new Vector(0, 1,0);
    normals[k] = vertexes[k];
  }
}
