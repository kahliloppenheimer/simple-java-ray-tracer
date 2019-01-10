package me.kahlil.geometry;

import static com.google.common.truth.Truth8.assertThat;
import static com.google.common.truth.Truth.assertThat;
import static me.kahlil.scene.Materials.BASIC_GREEN;

import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class PlaneTest {

  Plane xzPlane = new Plane(
      new Vector(0, 0, 0),
      new Vector(0, 1, 0),
      BASIC_GREEN);

  @Test
  public void testPerfectlyNormalIntersection() {
    Ray directlyAbove = new Ray(new Vector(0, 1, 0), new Vector(0, -1, 0));
    Optional<RayHit> rayHit = xzPlane.intersectInObjectSpace(directlyAbove);

    assertThat(rayHit).isPresent();
    RayHit expected = ImmutableRayHit.builder()
        .setObject(xzPlane)
        .setDistance(1)
        .setIntersection(new Vector(0, 0, 0))
        .setNormal(new Vector(0, 1, 0))
        .setRay(directlyAbove)
        .setTime(1)
        .build();
    assertThat(rayHit.get()).isEqualTo(expected);
  }

  @Test
  public void testPerfectlyParallelIntersection() {
    Ray parallel = new Ray(new Vector(0, 0, 0), new Vector(1, 0, 1));
    Optional<RayHit> rayHit = xzPlane.intersectInObjectSpace(parallel);
    assertThat(rayHit).isEmpty();
  }

}