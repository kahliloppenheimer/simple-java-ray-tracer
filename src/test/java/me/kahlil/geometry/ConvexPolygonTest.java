package me.kahlil.geometry;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth8.assertThat;
import static me.kahlil.geometry.Constants.EPSILON;
import static me.kahlil.geometry.Constants.ORIGIN;
import static me.kahlil.geometry.LinearTransformation.translate;
import static me.kahlil.scene.Materials.DUMMY_MATERIAL;

import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Unit tests for {@link ConvexPolygon}. */
@RunWith(JUnit4.class)
public class ConvexPolygonTest {

  // Coordinates for a simple square centered on the origin, translated back -1 along the z axis.
  private static final Vector BOTTOM_LEFT = new Vector(-1, -1, -1);
  private static final Vector BOTTOM_RIGHT = new Vector(1, -1, -1);
  private static final Vector TOP_RIGHT = new Vector(1, 1, -1);
  private static final Vector TOP_LEFT = new Vector(-1, 1, -1);

  @Test
  public void basicCube_middleIntersectionIsCorrect() {
    ConvexPolygon polygon = ConvexPolygon.cube(DUMMY_MATERIAL).transform(translate(0, 0, -1));

    Ray downZAxis = new Ray(ORIGIN, new Vector(0, 0, -1));
    Optional<RayHit> hit = polygon.intersectWith(downZAxis);
    assertThat(hit).isPresent();
    // Check that objects are preserved properly.
    assertThat(hit.get().getRay()).isEqualTo(downZAxis);
    assertThat(hit.get().getObject()).isInstanceOf(Triangle.class);
    // Verify intersection math.
    assertThat(hit.get().getTime()).isEqualTo(1.0);
    assertThat(hit.get().getNormal()).isEqualTo(new Vector(0, 0, 1));
    assertThat(hit.get().getDistance()).isEqualTo(1.0);
    assertThat(hit.get().getIntersection()).isEqualTo(new Vector(0, 0, -1));
  }

  @Test
  public void basicCube_bottomLeftCorner_intersectionsAreCorrect() {
    ConvexPolygon polygon = ConvexPolygon.cube(DUMMY_MATERIAL).transform(translate(0, 0, -1));

    Ray inside = new Ray(ORIGIN, BOTTOM_LEFT.translate(EPSILON, EPSILON));
    Ray leftOf = new Ray(ORIGIN, BOTTOM_LEFT.translate(-1 * EPSILON, EPSILON));
    Ray below = new Ray(ORIGIN, BOTTOM_LEFT.translate(EPSILON, -1 * EPSILON));

    assertThat(polygon.intersectWith(inside)).isPresent();
    assertThat(polygon.intersectWith(leftOf)).isEmpty();
    assertThat(polygon.intersectWith(below)).isEmpty();
  }

  @Test
  public void basicCube_bottomRightCorner_intersectionsAreCorrect() {
    ConvexPolygon polygon = ConvexPolygon.cube(DUMMY_MATERIAL).transform(translate(0, 0, -1));

    Ray inside = new Ray(ORIGIN, BOTTOM_RIGHT.translate(-1 * EPSILON, EPSILON));
    Ray rightOf = new Ray(ORIGIN, BOTTOM_RIGHT.translate(EPSILON, EPSILON));
    Ray below = new Ray(ORIGIN, BOTTOM_RIGHT.translate(-1 * EPSILON, -1 * EPSILON));

    assertThat(polygon.intersectWith(inside)).isPresent();
    assertThat(polygon.intersectWith(rightOf)).isEmpty();
    assertThat(polygon.intersectWith(below)).isEmpty();
  }

  @Test
  public void basicCube_topRightCorner_intersectionsAreCorrect() {
    ConvexPolygon polygon = ConvexPolygon.cube(DUMMY_MATERIAL).transform(translate(0, 0, -1));
    Ray inside = new Ray(ORIGIN, TOP_RIGHT.translate(-1 * EPSILON, -1 * EPSILON));
    Ray rightOf = new Ray(ORIGIN, TOP_RIGHT.translate(EPSILON, -1 * EPSILON));
    Ray above = new Ray(ORIGIN, TOP_RIGHT.translate(-1 * EPSILON, EPSILON));

    assertThat(polygon.intersectWith(inside)).isPresent();
    assertThat(polygon.intersectWith(rightOf)).isEmpty();
    assertThat(polygon.intersectWith(above)).isEmpty();
  }

  @Test
  public void basicCube_topLeftCorner_intersectionsAreCorrect() {
    ConvexPolygon polygon = ConvexPolygon.cube(DUMMY_MATERIAL).transform(translate(0, 0, -1));
    Ray inside = new Ray(ORIGIN, TOP_LEFT.translate(EPSILON, -1 * EPSILON));
    Ray leftOf = new Ray(ORIGIN, TOP_LEFT.translate(-1, -1 * EPSILON));
    Ray above = new Ray(ORIGIN, TOP_LEFT.translate(EPSILON, EPSILON));

    assertThat(polygon.intersectWith(inside)).isPresent();
    assertThat(polygon.intersectWith(leftOf)).isEmpty();
    assertThat(polygon.intersectWith(above)).isEmpty();
  }
}
