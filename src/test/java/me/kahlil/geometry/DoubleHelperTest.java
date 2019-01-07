package me.kahlil.geometry;

import static com.google.common.truth.Truth.assertThat;
import static me.kahlil.geometry.Constants.EPSILON;
import static me.kahlil.geometry.DoubleHelper.nearEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Unit tests for {@link DoubleHelper}.
 */
@RunWith(JUnit4.class)
public class DoubleHelperTest {

  @Test
  public void basicChecks() {
    // Check just within bounds.
    assertThat(nearEquals(1.0, 1.0 + (0.9 * EPSILON))).isTrue();
    assertThat(nearEquals(1.0, 1.0 - (0.9 * EPSILON))).isTrue();
    // Check just outside of bounds.
    assertThat(nearEquals(1.0, 1.0 + (1.1 * EPSILON))).isFalse();
    assertThat(nearEquals(1.0, 1.0 - (1.1 * EPSILON))).isFalse();
  }

}