package me.kahlil.graphics;

import static com.google.common.truth.Truth.assertThat;

import me.kahlil.graphics.MutableColor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Unit tests for {@link ColorComputation}. */
@RunWith(JUnit4.class)
public class MutableColorComputationTest {

  @Test
  public void testAddition() {
    assertThat(ColorComputation.of(new MutableColor(1, 2, 3)).add(new MutableColor(3, 2, 1)).compute())
        .isEqualTo(new MutableColor(4, 4, 4));
  }

  @Test
  public void testScaling() {
    assertThat(ColorComputation.of(new MutableColor(0.1f, 0.2f, 0.3f)).scaleFloat(2.0f).compute())
        .isEqualTo(new MutableColor(2.0f * 0.1f, 2.0f * 0.2f, 2.0f * 0.3f));
  }

  @Test
  public void testMultiplication() {
    assertThat(
            ColorComputation.of(new MutableColor(0.2f, 0.3f, 0.4f))
                .multiply(new MutableColor(0.5f, 0.6f, 0.7f))
                .compute())
        .isEqualTo(new MutableColor(0.2f * 0.5f, 0.3f * 0.6f, 0.4f * 0.7f));
  }
}
