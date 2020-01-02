package me.kahlil.graphics;

import static com.google.common.truth.Truth.assertThat;
import static me.kahlil.geometry.Constants.ORIGIN;
import static me.kahlil.graphics.Colors.RED;
import static me.kahlil.scene.Materials.BASIC_GREEN;

import me.kahlil.geometry.ImmutableRayHit;
import me.kahlil.geometry.Ray;
import me.kahlil.geometry.RayHit;
import me.kahlil.geometry.Sphere;
import me.kahlil.geometry.Vector;
import me.kahlil.scene.ImmutablePointLight;
import me.kahlil.scene.PointLight;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Unit tests for {@link me.kahlil.graphics.PhongShading}. */
@RunWith(JUnit4.class)
public class PhongShadingTest {

  private static final Sphere DUMMY_SPHERE = new Sphere(BASIC_GREEN);

  /**
   * When computing the diffuse light at a point, it is possible that the dot product of the surface
   * normal and the light vector is negative. This means that the light should have no effect at
   * this angle, and thus the diffuse lighting should be zero.
   */
  @Test
  public void diffuseLightingIsZeroWhenSurfaceNormalDotLightIsNegative() {
    PointLight light =
        ImmutablePointLight.builder().setLocation(ORIGIN).setColor(RED).build();
    RayHit rayHit =
        ImmutableRayHit.builder()
            // Unused for this test but required by builder to be set
            .setObject(DUMMY_SPHERE)
            .setTime(1)
            .setRay(new Ray(new Vector(0, 0, 0), new Vector(1, 0, 0)))
            // Relevant attributes for this test
            .setNormal(new Vector(1, 0, 0))
            .build();

    assertThat(PhongShading.diffuse(light, rayHit)).isZero();
  }
}
