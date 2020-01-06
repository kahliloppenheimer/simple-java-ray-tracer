package me.kahlil.geometry;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth8.assertThat;
import static me.kahlil.geometry.Constants.ORIGIN;
import static me.kahlil.scene.Materials.DUMMY_MATERIAL;

import java.util.Optional;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class TriangleTest {

  private static final Vector[] vertexes = new Vector[]{
      new Vector(-1, -1, -1), new Vector(1, -1, -1), new Vector(0, 1, -1)
  };

  // Triangle placed 1 unit backwards down the z axis and centered around it.
  private static final Triangle CENTERED_ON_NEGATIVE_Z_AXIS =
      Triangle.withSurfaceNormals(
          DUMMY_MATERIAL, vertexes);

  private static final Triangle WITH_VERTEX_NORMALS =
      Triangle.withVertexNormals(
          DUMMY_MATERIAL, vertexes, vertexes);

  // JUnitParamsRunner way of providing both triangles (with surface and vertex normals) to be
  // passed along to tests, since all should perform equivalently, with the exception of normal
  // computation.
  private Object[] provideAllTriangles() {
    return new Object[]{
        CENTERED_ON_NEGATIVE_Z_AXIS,
        WITH_VERTEX_NORMALS
    };
  }

  @Test
  @Parameters(method = "provideAllTriangles")
  public void intersectInObjectSpace_basicPositiveIntersection(Triangle triangle) {
    Ray downZAxis = new Ray(ORIGIN, new Vector(0, 0, -1));

    Optional<RayHit> intersection = triangle.intersectInObjectSpace(downZAxis);
    assertThat(intersection).isPresent();

    // Check references are stored correctly.
    assertThat(intersection.get().getRay()).isEqualTo(downZAxis);
    assertThat(intersection.get().getObject()).isEqualTo(triangle);

    // Check computation is correct.
    assertThat(intersection.get().getTime()).isEqualTo(1.0);
  }

  @Test
  public void intersectInObjectSpace_surfaceNormalIsCorrect() {
    Ray downZAxis = new Ray(ORIGIN, new Vector(0, 0, -1));

    Optional<RayHit> intersection = CENTERED_ON_NEGATIVE_Z_AXIS.intersectInObjectSpace(downZAxis);
    assertThat(intersection).isPresent();

    // Check surface normal is correct.
    assertThat(intersection.get().getNormal()).isEqualTo(new Vector(0, 0, 1));
  }

  @Test
  @Parameters(method = "provideAllTriangles")
  public void intersectInObjectSpace_basicFalseIntersection(Triangle triangle) {
    Ray awayFromTriangle = new Ray(ORIGIN, new Vector(0, 0, 1));

    assertThat(triangle.intersectInObjectSpace(awayFromTriangle)).isEmpty();
  }

  @Test
  public void intersectInObjectSpace_parallelRayAndTriangleDoNotIntersect() {
    Ray downZAxis = new Ray(ORIGIN, new Vector(0, 0, -1));

    Vector[] vertexes = {new Vector(-1, 0, -1), new Vector(0, 0, -2), new Vector(1, 0, -1)};
    Triangle onXZPlane =
        Triangle.withSurfaceNormals(
            DUMMY_MATERIAL,
            vertexes);

    Triangle onXZPlane2 =
        Triangle.withVertexNormals(
            DUMMY_MATERIAL, vertexes, vertexes);

    assertThat(onXZPlane.intersectInObjectSpace(downZAxis)).isEmpty();
    assertThat(onXZPlane2.intersectInObjectSpace(downZAxis)).isEmpty();
  }

  @Test
  public void intersectInObjectSpace_almostParallelRayAndTriangleDoIntersect() {
    Ray downZAxis = new Ray(ORIGIN, new Vector(0, 0, -1));

    Vector[] vertexes = {new Vector(-1, 0.1, -1), new Vector(0, -0.1, -2), new Vector(1, 0, -1)};
    Triangle almostOnXzPlane =
        Triangle.withSurfaceNormals(
            DUMMY_MATERIAL,
            vertexes);

    Triangle almostOnXzPlane2 =
        Triangle.withVertexNormals(DUMMY_MATERIAL, vertexes, vertexes);

    assertThat(almostOnXzPlane.intersectInObjectSpace(downZAxis)).isPresent();
    assertThat(almostOnXzPlane2.intersectInObjectSpace(downZAxis)).isPresent();
  }

  @Test
  public void boundsComputedCorrectly() {
    Triangle t = Triangle.withSurfaceNormals(
        DUMMY_MATERIAL, new Vector(-5, 0, 2), new Vector(-3, 3, 3), new Vector(8, 2, 5));

    assertThat(t.minBound()).isEqualTo(new Vector(-5, 0, 2));
    assertThat(t.maxBound()).isEqualTo(new Vector(8, 3, 5));
  }

}
