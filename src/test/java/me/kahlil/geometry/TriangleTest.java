package me.kahlil.geometry;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth8.assertThat;
import static me.kahlil.geometry.Constants.ORIGIN;
import static me.kahlil.scene.Materials.BASIC_GREEN;

import java.util.Optional;
import org.junit.Test;

public class TriangleTest {

  // Triangle placed 1 unit backwards down the z axis and centered around it.
  public static final Triangle CENTERED_ON_NEGATIVE_Z_AXIS =
      new Triangle(
          BASIC_GREEN, new Vector(-1, -1, -1), new Vector(1, -1, -1), new Vector(0, 1, -1));

  @Test
  public void intersectInObjectSpace_basicPositiveIntersection() {
    Ray downZAxis = new Ray(ORIGIN, new Vector(0, 0, -1));

    Optional<RayHit> intersection = CENTERED_ON_NEGATIVE_Z_AXIS.intersectInObjectSpace(downZAxis);
    assertThat(intersection).isPresent();

    // Check references are stored correctly.
    assertThat(intersection.get().getRay()).isEqualTo(downZAxis);
    assertThat(intersection.get().getObject()).isEqualTo(CENTERED_ON_NEGATIVE_Z_AXIS);

    // Check computation is correct.
    assertThat(intersection.get().getTime()).isEqualTo(1.0);
    assertThat(intersection.get().getNormal()).isEqualTo(new Vector(0, 0, 1));
  }

  @Test
  public void intersectInObjectSpace_basicFalseIntersection() {
    Ray awayFromTriangle = new Ray(ORIGIN, new Vector(0, 0, 1));

    assertThat(CENTERED_ON_NEGATIVE_Z_AXIS.intersectInObjectSpace(awayFromTriangle)).isEmpty();
  }

  @Test
  public void intersectInObjectSpace_parallelRayAndTriangleDoNotIntersect() {
    Ray downZAxis = new Ray(ORIGIN, new Vector(0, 0, -1));

    Triangle onXZPlane =
        new Triangle(
            BASIC_GREEN, new Vector(-1, 0, -1), new Vector(0, 0, -2), new Vector(1, 0, -1));

    assertThat(onXZPlane.intersectInObjectSpace(downZAxis)).isEmpty();
  }

  @Test
  public void intersectInObjectSpace_almostParallelRayAndTriangleDoIntersect() {
    Ray downZAxis = new Ray(ORIGIN, new Vector(0, 0, -1));

    Triangle onXZPlane =
        new Triangle(
            BASIC_GREEN, new Vector(-1, 0.1, -1), new Vector(0, -0.1, -2), new Vector(1, 0, -1));


    Optional<RayHit> intersection = onXZPlane.intersectInObjectSpace(downZAxis);
    assertThat(intersection).isPresent();
  }

  @Test
  public void getOutsideMaterial_preservedProperly() {
    Triangle triangle = new Triangle(BASIC_GREEN, ORIGIN, ORIGIN, ORIGIN);

    assertThat(triangle.getOutsideMaterial()).isEqualTo(BASIC_GREEN);
  }
}
