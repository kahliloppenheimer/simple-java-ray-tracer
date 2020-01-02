package me.kahlil.geometry;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth8.assertThat;
import static me.kahlil.graphics.Colors.BLACK;
import static me.kahlil.graphics.Colors.WHITE;
import static me.kahlil.geometry.Constants.EPSILON;
import static me.kahlil.geometry.Constants.ORIGIN;
import static me.kahlil.geometry.LinearTransformation.IDENTITY;
import static me.kahlil.geometry.LinearTransformation.rotateAboutXAxis;
import static me.kahlil.geometry.LinearTransformation.rotateAboutZAxis;
import static me.kahlil.geometry.LinearTransformation.scale;
import static me.kahlil.geometry.LinearTransformation.translate;
import static me.kahlil.scene.Cameras.STANDARD_CAMERA;
import static me.kahlil.scene.Materials.BASIC_GREEN;

import me.kahlil.graphics.MutableColor;
import java.util.Arrays;
import java.util.Optional;
import me.kahlil.graphics.PhongShading;
import me.kahlil.scene.ImmutablePointLight;
import me.kahlil.scene.ImmutableScene;
import me.kahlil.scene.Scene;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ShapeTest {

  @Test
  public void transformMethod() {
    PointObject beforeTransformation = new PointObject(0, 0, 0);
    assertThat(beforeTransformation.getTransformation()).isEqualTo(IDENTITY);

    PointObject afterTransformation = beforeTransformation.transform(translate(1.0, 1.0, 1.0));

    assertThat(afterTransformation.getTransformation().getMatrix())
        .isEqualTo(translate(1.0, 1.0, 1.0).getMatrix());

    afterTransformation = beforeTransformation.transform(rotateAboutZAxis(90));
    assertThat(afterTransformation.getTransformation().getMatrix())
        .isEqualTo(rotateAboutZAxis(90).getMatrix());

    afterTransformation =
        beforeTransformation.transform(translate(1.0, 1.0, 1.0)).transform(rotateAboutZAxis(90));
    assertThat(afterTransformation.getTransformation().getMatrix())
        .isEqualTo(translate(1.0, 1.0, 1.0).then(rotateAboutZAxis(90)).getMatrix());
  }

  @Test
  public void translatedIntersection() {
    PointObject beforeTranslation = new PointObject(0, 0, -1);
    PointObject afterTranslation = beforeTranslation.transform(translate(0.0, -1.0, 0.0));
    Ray towardsOriginal = new Ray(ORIGIN, new Vector(0, 0, -1));
    Ray towardsTranslated = new Ray(ORIGIN, new Vector(0, -1, -1));

    assertThat(beforeTranslation.intersectWith(towardsOriginal)).isPresent();
    assertThat(beforeTranslation.intersectWith(towardsTranslated)).isEmpty();

    assertThat(afterTranslation.intersectWith(towardsOriginal)).isEmpty();
    assertThat(afterTranslation.intersectWith(towardsTranslated)).isPresent();
  }

  @Test
  public void rotatedAboutZAxisIntersection() {
    PointObject beforeRotation = new PointObject(1.0, 0.0, 0.0);
    PointObject afterRotation = beforeRotation.transform(rotateAboutZAxis(90));
    Ray towardsOriginal = new Ray(ORIGIN, new Vector(1.0, 0, 0.0));
    Ray towardsTranslated = new Ray(ORIGIN, new Vector(0, 1.0, 0.0));

    assertThat(beforeRotation.intersectWith(towardsOriginal)).isPresent();
    assertThat(beforeRotation.intersectWith(towardsTranslated)).isEmpty();

    assertThat(afterRotation.intersectWith(towardsOriginal)).isEmpty();
    assertThat(afterRotation.intersectWith(towardsTranslated)).isPresent();
  }

  @Test
  public void sphereRotationDoesNotAffectRayNormal() {
    Sphere notRotated = new Sphere(BASIC_GREEN).transform(translate(0.0, 0.0, -2.0));

    Sphere rotated =
        new Sphere(BASIC_GREEN)
            .transform(rotateAboutXAxis(90))
            .transform(translate(0.0, 0.0, -2.0));

    Ray towardsSphere = new Ray(new Vector(0, 0, 0), new Vector(0, 0, -1.0));

    // Alter object field, since it will be different.
    Optional<RayHit> maybeNotRotatedHit = notRotated.intersectWith(towardsSphere);
    Optional<RayHit> maybeRotatedHit = rotated.intersectWith(towardsSphere);
    assertThat(maybeNotRotatedHit).isPresent();
    assertThat(maybeRotatedHit).isPresent();

    RayHit expectedRayHit =
        ImmutableRayHit.builder().from(maybeNotRotatedHit.get()).setObject(rotated).build();
    assertThat(maybeRotatedHit.get()).isEqualTo(expectedRayHit);
  }

  @Test
  public void sphereRotationDoesNotAffectShading() {
    Sphere notRotated = new Sphere(BASIC_GREEN).transform(translate(0.0, 0.0, -2.0));

    Sphere rotated =
        new Sphere(BASIC_GREEN)
            .transform(rotateAboutXAxis(90))
            .transform(translate(0.0, 0.0, -2.0));

    Ray towardsSphere = new Ray(new Vector(0, 0, 0), new Vector(-0.25, -0.50, -1.0));

    // Compute Phong shading for both and compare.
    MutableColor expectedColor =
        new PhongShading(simpleScene(notRotated), STANDARD_CAMERA, true)
            .shade(notRotated.intersectWith(towardsSphere).get());
    MutableColor actualColor =
        new PhongShading(simpleScene(rotated), STANDARD_CAMERA, true)
            .shade(rotated.intersectWith(towardsSphere).get());
    assertThat(actualColor).isEqualTo(expectedColor);
  }

  @Test
  public void sphereScaling() {
    Sphere scaled =
        new Sphere(BASIC_GREEN)
            .transform(translate(1.0, 0.0, -1.0))
            .transform(scale(0.1));

    Ray towardsMiddle = new Ray(new Vector(0, 0, 0), new Vector(1.0, 0.0, -1.0));
    Ray insideEdge = new Ray(
        new Vector(0, 0, 0),
        new Vector(EPSILON, 0.0, -1.0));
    Ray outsideEdge = new Ray(
        new Vector(0, 0, 0),
        new Vector(-1 * EPSILON, 0.0, -1.0));

    assertThat(scaled.intersectWith(towardsMiddle)).isPresent();
    // Check edges of scaled sphere
    assertThat(scaled.intersectWith(insideEdge)).isPresent();
    assertThat(scaled.intersectWith(outsideEdge)).isEmpty();
  }

  private static Scene simpleScene(Shape... shapes) {
    return ImmutableScene.builder()
        .setAmbient(BLACK)
        .setBackgroundColor(BLACK)
        .addLights(
            ImmutablePointLight.builder().setLocation(new Vector(0, 2, 5)).setColor(WHITE).build())
        .addAllShapes(Arrays.asList(shapes))
        .build();
  }
}
