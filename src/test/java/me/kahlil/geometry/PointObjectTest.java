package me.kahlil.geometry;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth8.assertThat;
import static me.kahlil.geometry.Constants.EPSILON;
import static me.kahlil.geometry.Constants.ORIGIN;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Unit tests for {@link PointObject}.
 */
@RunWith(JUnit4.class)
public class PointObjectTest {

  @Test
  public void testTrivialIntersection() {
    PointObject pointObject = new PointObject(1.0, 0.0, 0.0);

    Ray shouldIntersect = new Ray(ORIGIN, new Vector(1.0, 0.0, 0.0));
    assertThat(pointObject.intersectWith(shouldIntersect).isPresent()).isTrue();
  }

  @Test
  public void testIntersectionJustMisses() {
    PointObject pointObject = new PointObject(10.0, 10.0, 0.0);

    Ray shouldHit = new Ray(ORIGIN, new Vector(1.0, 1.0, 0.0));
    assertThat(pointObject.intersectWith(shouldHit)).isPresent();

    Ray shouldMiss = new Ray(ORIGIN, new Vector(1.0, 1.0 + 10 * EPSILON, 0.0));
    assertThat(pointObject.intersectWith(shouldMiss)).isEmpty();

    shouldMiss = new Ray(ORIGIN, new Vector(1.0, 1.0 - 10 * EPSILON, 0.0));
    assertThat(pointObject.intersectWith(shouldMiss)).isEmpty();

    shouldMiss = new Ray(ORIGIN, new Vector(1.0 + 10 * EPSILON, 1.0, 0.0));
    assertThat(pointObject.intersectWith(shouldMiss)).isEmpty();

    shouldMiss = new Ray(ORIGIN, new Vector(1.0 - 10 * EPSILON, 1.0, 0.0));
    assertThat(pointObject.intersectWith(shouldMiss)).isEmpty();

    shouldMiss = new Ray(ORIGIN, new Vector(1.0, 1.0, 10 * EPSILON));
    assertThat(pointObject.intersectWith(shouldMiss)).isEmpty();

    shouldMiss = new Ray(ORIGIN, new Vector(1.0, 1.0, -10 * EPSILON));
    assertThat(pointObject.intersectWith(shouldMiss)).isEmpty();
  }

  @Test
  public void testNegativeDirection() {
    PointObject pointObject = new PointObject(1.0, 0.0, 0.0);

    Ray shouldMiss = new Ray(ORIGIN, new Vector(-1.0, 0.0, 0.0));
    assertThat(pointObject.intersectWith(shouldMiss)).isEmpty();
  }

}