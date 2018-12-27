package me.kahlil.scene;

import static com.google.common.truth.Truth.assertThat;

import me.kahlil.geometry.ImmutableRayHit;
import me.kahlil.geometry.Ray3D;
import me.kahlil.geometry.RayHit;
import me.kahlil.geometry.Sphere3D;
import me.kahlil.geometry.Vector;
import me.kahlil.graphics.Color;
import org.junit.Test;

public class Light3DTest {

  private static final Sphere3D DUMMY_SPHERE = new Sphere3D(null, 0.0, null, null);

  /**
   * When computing the diffuse light at a point, it is possible that the dot product of the surface
   * normal and the light vector is negative. This means that the light should have no effect at
   * this angle, and thus the diffuse lighting should be zero.
   */
  @Test
  public void diffuseLightingIsZeroWhenSurfaceNormalDotLightIsNegative() {
    Light3D light = new Light3D(new Vector(0, 0, 0), Color.RED);
    RayHit rayHit =
        ImmutableRayHit.builder()
            // Unused for this test but required by builder to be set
            .setDistance(1)
            .setObject(DUMMY_SPHERE)
            .setTime(1)
            .setRay(new Ray3D(new Vector(0, 0, 0), new Vector(0, 0, 0)))
            // Relevant attributes for this test
            .setIntersection(new Vector(1, 0, 0))
            .setNormal(new Vector(1, 0, 0))
            .build();
    assertThat(light.diffuse(rayHit)).isZero();
  }
}
