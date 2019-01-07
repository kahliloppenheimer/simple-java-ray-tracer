package me.kahlil.scene;

import static com.google.common.truth.Truth.assertThat;

import java.awt.Color;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Unit tests for {@link Raster}.
 */
@RunWith(JUnit4.class)
public class RasterTest {

  @Test
  public void pixelSizesAreCorrect() {
    Raster raster = new Raster(15, 10);
    assertThat(raster.getWidthPx()).isEqualTo(15);
    assertThat(raster.getHeightPx()).isEqualTo(10);
  }

  @Test
  public void pixelSetAndGetFunctionsAsExpected() {
    Raster raster = new Raster(15, 10);
    for (int i = 0; i < raster.getHeightPx(); i++) {
      for (int j = 0; j < raster.getWidthPx(); j++) {
        float colorVal = (i + j) / (1.0f * raster.getHeightPx() * raster.getWidthPx());
        raster.setPixel(i, j, new Color(colorVal, colorVal, colorVal, colorVal));
        assertThat(raster.getPixel(i, j))
            .isEqualTo(new Color(colorVal, colorVal, colorVal, colorVal));
      }
    }
  }

}