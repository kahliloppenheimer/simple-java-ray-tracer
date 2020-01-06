package me.kahlil.geometry;

import static com.google.common.truth.Truth8.assertThat;
import static com.google.common.truth.Truth.assertThat;
import static me.kahlil.scene.Materials.DUMMY_MATERIAL;

import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class PlaneTest {

  private static final Plane xzPlane =
      new Plane(new Vector(0, 0, 0), new Vector(0, 1, 0), DUMMY_MATERIAL);

  @Test
  public void testPerfectlyNormalIntersection() {
    Ray directlyAbove = new Ray(new Vector(0, 1, 0), new Vector(0, -1, 0));
    Optional<RayHit> rayHit = xzPlane.intersectWith(directlyAbove);

    assertThat(rayHit).isPresent();
    RayHit expected =
        ImmutableRayHit.builder()
            .setObject(xzPlane)
            .setNormal(new Vector(0, 1, 0))
            .setRay(directlyAbove)
            .setTime(1)
            .build();
    assertThat(rayHit.get()).isEqualTo(expected);
  }

  @Test
  public void testPerfectlyParallelIntersection() {
    Ray parallel = new Ray(new Vector(0, 0, 0), new Vector(1, 0, 1));
    Optional<RayHit> rayHit = xzPlane.intersectWith(parallel);
    assertThat(rayHit).isEmpty();
  }
}
