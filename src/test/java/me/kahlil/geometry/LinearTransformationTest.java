package me.kahlil.geometry;

import static com.google.common.truth.Truth.assertThat;
import static junit.framework.TestCase.assertEquals;
import static me.kahlil.geometry.Constants.ORIGIN;
import static me.kahlil.geometry.LinearTransformation.IDENTITY;
import static me.kahlil.geometry.LinearTransformation.rotateAboutXAxis;
import static me.kahlil.geometry.LinearTransformation.rotateAboutYAxis;
import static me.kahlil.geometry.LinearTransformation.rotateAboutZAxis;
import static me.kahlil.geometry.LinearTransformation.scale;
import static me.kahlil.geometry.LinearTransformation.translate;

import com.google.common.collect.ImmutableList;
import java.util.Random;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Unit tests for {@link LinearTransformation}. */
@RunWith(JUnit4.class)
public class LinearTransformationTest {

  private static Vector ZERO_VEC = new Vector(0, 0, 0, 0);
  private static Random rand = new Random();

  ImmutableList<LinearTransformation> ALL_TRANSFORMATIONS =
      ImmutableList.of(
          translate(1.0, -2.0, 3.0),
          scale(1.0, -2.0, 3.0),
          rotateAboutXAxis(90),
          rotateAboutYAxis(90),
          rotateAboutZAxis(90));

  @Test
  public void simpleTranslations() {
    assertEquals(ZERO_VEC, LinearTransformation.translate(0, 0, 0).apply(ZERO_VEC));
    assertEquals(LinearTransformation.translate(1, 1, 1).apply(ZERO_VEC), ZERO_VEC);
    assertEquals(
        new Vector(1, 1, 1, 1),
        LinearTransformation.translate(1, 1, 1).apply(new Vector(0, 0, 0, 1)));

    for (int i = 0; i < 100; ++i) {
      double a = rand.nextDouble();
      double b = rand.nextDouble();
      double c = rand.nextDouble();

      double a2 = rand.nextDouble();
      double b2 = rand.nextDouble();
      double c2 = rand.nextDouble();

      Vector initial = new Vector(a, b, c, 1);
      Vector translate = new Vector(a2, b2, c2);
      LinearTransformation lt = LinearTransformation.translate(translate);

      assertEquals(new Vector(a + a2, b + b2, c + c2), lt.apply(initial));
      assertEquals(new Vector(1, 1, 1), lt.apply(new Vector(1, 1, 1, 0)));
    }
  }

  @Test
  public void simpleScaling() {
    assertEquals(
        ZERO_VEC, scale(rand.nextDouble(), rand.nextDouble(), rand.nextDouble()).apply(ZERO_VEC));
    for (int i = 0; i < 100; ++i) {
      double a = rand.nextDouble();
      double b = rand.nextDouble();
      double c = rand.nextDouble();

      double a2 = rand.nextDouble();
      double b2 = rand.nextDouble();
      double c2 = rand.nextDouble();

      Vector v1 = new Vector(a, b, c, 1);
      Vector v2 = new Vector(a, b, c, 0);
      Vector args = new Vector(a2, b2, c2);
      LinearTransformation lt = scale(args);

      assertEquals(new Vector(a * a2, b * b2, c * c2), lt.apply(v1));
      assertEquals(new Vector(a * a2, b * b2, c * c2), lt.apply(v2));
    }
  }

  @Test
  public void transformationsCompose() {
    for (int i = 0; i < 100; ++i) {
      Vector untransformed = new Vector(i, -1.0 * (i + 1), i + 2, 1);
      Vector scaleBy = new Vector(i / 100.0, -1.0 * (i + 1) / 100.0, (i + 2) / 100.0);
      Vector translateBy = new Vector(-1.0 * i, 2 * i, -3.0 * i);
      LinearTransformation translateThenScale =
          translate(translateBy).then(LinearTransformation.scale(scaleBy));

      assertThat(translateThenScale.apply(untransformed))
          .isEqualTo(
              new Vector(
                  (untransformed.getX() + translateBy.getX()) * scaleBy.getX(),
                  (untransformed.getY() + translateBy.getY()) * scaleBy.getY(),
                  (untransformed.getZ() + translateBy.getZ()) * scaleBy.getZ()));
    }
  }

  @Test
  public void transformationComposedWithInverseIsIdentity() {
    ALL_TRANSFORMATIONS.forEach(
        transformation ->
            assertThat(transformation.then(transformation.inverse())).isEqualTo(IDENTITY));
  }

  @Test
  public void composedTransformationComposedWithInverseIsIdentity() {
    for (LinearTransformation transformation1 : ALL_TRANSFORMATIONS) {
      for (LinearTransformation transformation2 : ALL_TRANSFORMATIONS) {
        LinearTransformation composition = transformation1.then(transformation2);
        assertThat(composition.then(composition.inverse())).isEqualTo(IDENTITY);
      }
    }
  }

  @Test
  public void rotationAboutXAxis() {
    Vector vector = new Vector(0, 1, 0);

    assertThat(rotateAboutXAxis(90).apply(vector)).isEqualTo(new Vector(0, 0.0, 1.0));
    assertThat(rotateAboutXAxis(180).apply(vector)).isEqualTo(new Vector(0, -1.0, 0));
    assertThat(rotateAboutXAxis(270).apply(vector)).isEqualTo(new Vector(0, 0.0, -1.0));
    assertThat(rotateAboutXAxis(360).apply(vector)).isEqualTo(vector);

    assertThat(rotateAboutXAxis(450)).isEqualTo(rotateAboutXAxis(90));
  }

  @Test
  public void rotationAboutYAxis() {
    Vector vector = new Vector(1, 0, 0);

    assertThat(rotateAboutYAxis(90).apply(vector)).isEqualTo(new Vector(0, 0.0, -1.0));
    assertThat(rotateAboutYAxis(180).apply(vector)).isEqualTo(new Vector(-1.0, 0.0, 0));
    assertThat(rotateAboutYAxis(270).apply(vector)).isEqualTo(new Vector(0, 0.0, 1.0));
    assertThat(rotateAboutYAxis(360).apply(vector)).isEqualTo(vector);

    assertThat(rotateAboutYAxis(450)).isEqualTo(rotateAboutYAxis(90));
  }

  @Test
  public void rotationAboutZAxis() {
    Vector vector = new Vector(1, 0, 0);

    assertThat(rotateAboutZAxis(90).apply(vector)).isEqualTo(new Vector(0, 1.0, 0.0));
    assertThat(rotateAboutZAxis(180).apply(vector)).isEqualTo(new Vector(-1.0, 0.0, 0));
    assertThat(rotateAboutZAxis(270).apply(vector)).isEqualTo(new Vector(0, -1.0, 0.0));
    assertThat(rotateAboutZAxis(360).apply(vector)).isEqualTo(vector);

    assertThat(rotateAboutZAxis(450)).isEqualTo(rotateAboutZAxis(90));
  }

  @Test
  public void rotationOfPointAtOriginDoesNothing() {
    assertThat(rotateAboutXAxis(90).apply(ORIGIN)).isEqualTo(ORIGIN);
    assertThat(rotateAboutYAxis(90).apply(ORIGIN)).isEqualTo(ORIGIN);
    assertThat(rotateAboutZAxis(90).apply(ORIGIN)).isEqualTo(ORIGIN);

    // Check homogeneous coordinates as well.
    assertThat(rotateAboutXAxis(90).apply(new Vector(0, 0, 0, 1.0))).isEqualTo(ORIGIN);
    assertThat(rotateAboutYAxis(90).apply(new Vector(0, 0, 0, 1.0))).isEqualTo(ORIGIN);
    assertThat(rotateAboutZAxis(90).apply(new Vector(0, 0, 0, 1.0))).isEqualTo(ORIGIN);
  }

  @Test
  public void rotatedAtOriginThenTranslatedIsSameAsTranslated() {
    Vector translated = translate(1.0, -2.0, 3.0).apply(ORIGIN);
    Vector rotatedThenTranslated =
        rotateAboutXAxis(90).then(translate(1.0, -2.0, 3.0)).apply(ORIGIN);
    assertThat(rotatedThenTranslated).isEqualTo(translated);
  }

  @Test
  public void negativeRotationIsSameAsInverse() {
    assertThat(rotateAboutXAxis(90).inverse()).isEqualTo(rotateAboutXAxis(-90));
    assertThat(rotateAboutYAxis(90).inverse()).isEqualTo(rotateAboutYAxis(-90));
    assertThat(rotateAboutZAxis(90).inverse()).isEqualTo(rotateAboutZAxis(-90));
  }
}
