package me.kahlil.geometry;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth8.assertThat;
import static me.kahlil.geometry.Constants.EPSILON;
import static me.kahlil.geometry.LinearTransformation.scale;
import static me.kahlil.geometry.LinearTransformation.translate;
import static me.kahlil.scene.Materials.BASIC_GREEN;

import java.util.Optional;
import java.util.Random;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Unit tests for {@link Sphere}. */
@RunWith(JUnit4.class)
public class SphereTest {

  private static final Sphere unitSphere = new Sphere(BASIC_GREEN);

  @Test
  public void testRayIntersectFromInside() {
    Ray r = new Ray(new Vector(0, 0, 0), new Vector(1, 1, 1));
    Optional<RayHit> rayHit = unitSphere.intersectWith(r);
    assertThat(rayHit.isPresent()).isTrue();
    assertThat(rayHit.get().getDistance()).isWithin(EPSILON).of(1);
    assertThat(rayHit.get().getIntersection()).isEqualTo(new Vector(1, 1, 1).normalize());
  }

  @Test
  public void testRayIntersectOnceFromOutside() {
    Ray r = new Ray(new Vector(-1, 1, 0), new Vector(1, 0, 0));
    Optional<RayHit> rayHit = unitSphere.intersectWith(r);
    assertThat(rayHit).isPresent();
    assertThat(rayHit.get().getDistance()).isWithin(EPSILON).of(1);
    assertThat(rayHit.get().getIntersection()).isEqualTo(new Vector(0, 1, 0));
  }

  @Test
  public void testRayIntersectTwiceFromOutside() {
    for (int i = 0; i < 100; ++i) {
      Vector p = getRandPointBiggerThan(1);
      Ray ray = new Ray(p, p.scale(-1));
      Optional<RayHit> rh = unitSphere.intersectWith(ray);
      assertThat(rh).isPresent();
      assertThat(rh.get().getDistance()).isLessThan(p.magnitude());
    }
  }

  @Test
  public void testRayDoesNotIntersectSphere() {
    Ray r = new Ray(new Vector(0, -2, 0), new Vector(0, -1, 0));
    Optional<RayHit> rh = unitSphere.intersectWith(r);
    assertThat(rh).isEmpty();
  }

  @Test
  public void edgeTest() {
    Sphere sphere = new Sphere(BASIC_GREEN).transform(translate(0.0, 0.0, -2.0));

    Ray towardsMiddle = new Ray(new Vector(0, 0, 0), new Vector(0, 0, -1.0));
    Ray insideEdge = new Ray(
        new Vector(0, 0, 0),
        new Vector(1 - EPSILON, 1 - EPSILON, -1.0));
    Ray outsideEdge = new Ray(
        new Vector(0, 0, 0),
        new Vector(1 + EPSILON, 1 + EPSILON, -1.0));

    assertThat(sphere.intersectWith(towardsMiddle)).isPresent();
    // Check edges
    assertThat(sphere.intersectWith(insideEdge)).isPresent();
    assertThat(sphere.intersectWith(outsideEdge)).isEmpty();
  }

  private static Vector getRandPointBiggerThan(int i) {
    Random rand = new Random();
    return new Vector(rand.nextInt(100) + i, rand.nextInt(100) + i, rand.nextInt(100) + i);
  }
}
