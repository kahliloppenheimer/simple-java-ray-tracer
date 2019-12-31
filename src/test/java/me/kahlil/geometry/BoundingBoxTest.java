package me.kahlil.geometry;

import static com.google.common.truth.Truth.assertThat;
import static me.kahlil.geometry.Constants.EPSILON;

import com.google.common.truth.Truth;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Unit tests for {@link BoundingBox}.
 */
@RunWith(JUnit4.class)
public class BoundingBoxTest {

  private static final Vector RAY_ORIGIN = new Vector(0, 0, 1);

  // Coordinates for a simple square centered on the origin, translated back -1 along the z axis.
  private static final Vector BOTTOM_LEFT = new Vector(-1, -1, -1);
  private static final Vector BOTTOM_RIGHT = new Vector(1, -1, -1);
  private static final Vector TOP_RIGHT = new Vector(1, 1, -1);
  private static final Vector TOP_LEFT = new Vector(-1, 1, -1);

  private static final BoundingBox BOX = new BoundingBox(BOTTOM_LEFT, TOP_RIGHT.translate(0, 0, 1.0));

  @Test
  public void basicCube_middleIntersectionIsCorrect() {
    Ray downZAxis = new Ray(RAY_ORIGIN, new Vector(0, 0, -1));

    Truth.assertThat(BOX.intersectsWith(downZAxis)).isTrue();
  }

  @Test
  public void basicCube_bottomLeftCorner_intersectionsAreCorrect() {
    Ray inside = new Ray(RAY_ORIGIN, BOTTOM_LEFT.translate(EPSILON, EPSILON));
    Ray leftOf = new Ray(RAY_ORIGIN, BOTTOM_LEFT.translate(-1 * EPSILON, EPSILON));
    Ray below = new Ray(RAY_ORIGIN, BOTTOM_LEFT.translate(EPSILON, -1 * EPSILON));

    assertThat(BOX.intersectsWith(inside)).isTrue();
    assertThat(BOX.intersectsWith(leftOf)).isFalse();
    assertThat(BOX.intersectsWith(below)).isFalse();
  }

  @Test
  public void basicCube_bottomRightCorner_intersectionsAreCorrect() {
    Ray inside = new Ray(RAY_ORIGIN, BOTTOM_RIGHT.translate(-1 * EPSILON, EPSILON));
    Ray rightOf = new Ray(RAY_ORIGIN, BOTTOM_RIGHT.translate(EPSILON, EPSILON));
    Ray below = new Ray(RAY_ORIGIN, BOTTOM_RIGHT.translate(-1 * EPSILON, -1 * EPSILON));

    assertThat(BOX.intersectsWith(inside)).isTrue();
    assertThat(BOX.intersectsWith(rightOf)).isFalse();
    assertThat(BOX.intersectsWith(below)).isFalse();
  }

  @Test
  public void basicCube_topRightCorner_intersectionsAreCorrect() {
    Ray inside = new Ray(RAY_ORIGIN, TOP_RIGHT.translate(-1 * EPSILON, -1 * EPSILON));
    Ray rightOf = new Ray(RAY_ORIGIN, TOP_RIGHT.translate(EPSILON, -1 * EPSILON));
    Ray above = new Ray(RAY_ORIGIN, TOP_RIGHT.translate(-1 * EPSILON, EPSILON));

    assertThat(BOX.intersectsWith(inside)).isTrue();
    assertThat(BOX.intersectsWith(rightOf)).isFalse();
    assertThat(BOX.intersectsWith(above)).isFalse();
  }

  @Test
  public void basicCube_topLeftCorner_intersectionsAreCorrect() {
    Ray inside = new Ray(RAY_ORIGIN, TOP_LEFT.translate(EPSILON, -1 * EPSILON));
    Ray leftOf = new Ray(RAY_ORIGIN, TOP_LEFT.translate(-1, -1 * EPSILON));
    Ray above = new Ray(RAY_ORIGIN, TOP_LEFT.translate(EPSILON, EPSILON));

    assertThat(BOX.intersectsWith(inside)).isTrue();
    assertThat(BOX.intersectsWith(leftOf)).isFalse();
    assertThat(BOX.intersectsWith(above)).isFalse();
  }

}