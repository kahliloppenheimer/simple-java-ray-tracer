package me.kahlil.graphics;

import static com.google.common.truth.Truth.assertThat;
import static me.kahlil.geometry.Constants.EPSILON;
import static me.kahlil.graphics.CoordinateMapper.convertPixelToCameraSpaceCoordinates;
import static me.kahlil.graphics.CoordinateMapper.getPixelHeightInCameraSpace;
import static me.kahlil.graphics.CoordinateMapper.getPixelWidthInCameraSpace;

import me.kahlil.scene.Cameras;
import me.kahlil.scene.Raster;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CoordinateMapperTest {

  @Test
  public void firstPixelToCameraSpace() {
    Point2D inCameraSpace =
        convertPixelToCameraSpaceCoordinates(new Raster(20, 20), Cameras.NINETY_DEGREE_FOV, 0, 0);
    Point2D expected = ImmutablePoint2D.builder().setX(-0.95).setY(0.95).build();
    assertThat(inCameraSpace.getX()).isWithin(EPSILON).of(expected.getX());
    assertThat(inCameraSpace.getY()).isWithin(EPSILON).of(expected.getY());
  }

  @Test
  public void firstPixelToCameraSpaceWithAspectRatioEffect() {
    Point2D inCameraSpace =
        convertPixelToCameraSpaceCoordinates(new Raster(40, 20), Cameras.NINETY_DEGREE_FOV, 0, 0);
    // Standard Camera uses narrower FOV, thus X, Y are more tightly bound.
    Point2D expected =
        ImmutablePoint2D.builder()
            // X is stretched by aspect ratio (width / height).
            .setX(-0.975 * 2.0)
            .setY(0.95)
            .build();
    assertThat(inCameraSpace.getX()).isWithin(EPSILON).of(expected.getX());
    assertThat(inCameraSpace.getY()).isWithin(EPSILON).of(expected.getY());
  }

  @Test
  public void firstPixelToCameraSpaceWithFovEffect() {
    Point2D inCameraSpace =
        convertPixelToCameraSpaceCoordinates(new Raster(20, 20), Cameras.STANDARD_CAMERA, 0, 0);
    // Standard Camera uses narrower FOV, thus X, Y are more tightly bound.
    Point2D expected =
        ImmutablePoint2D.builder().setX(-0.5484827557301444).setY(0.5484827557301444).build();
    assertThat(inCameraSpace.getX()).isWithin(EPSILON).of(expected.getX());
    assertThat(inCameraSpace.getY()).isWithin(EPSILON).of(expected.getY());
  }

  @Test
  public void pixelWidth() {
    assertThat(getPixelWidthInCameraSpace(new Raster(20, 20), Cameras.NINETY_DEGREE_FOV))
        .isWithin(EPSILON)
        .of(0.1);
  }

  @Test
  public void pixelWidthNotAffectedByAspectRatio() {
    assertThat(getPixelWidthInCameraSpace(new Raster(40, 20), Cameras.NINETY_DEGREE_FOV))
        // Aspect ratio effect mitigated by doubling number of x pixels.
        .isWithin(EPSILON)
        .of(0.1);
  }

  @Test
  public void pixelWidthWithFovEffect() {
    assertThat(getPixelWidthInCameraSpace(new Raster(20, 20), Cameras.STANDARD_CAMERA))
        // Narrower FOV means a smaller pixel width.
        .isWithin(EPSILON)
        .of(0.05773502691896254);
  }

  @Test
  public void pixelHeight() {
    assertThat(getPixelHeightInCameraSpace(new Raster(20, 20), Cameras.NINETY_DEGREE_FOV))
        .isWithin(EPSILON)
        .of(0.1);
  }

  @Test
  public void pixelHeightNotEffectedByAspectRatio() {
    assertThat(getPixelHeightInCameraSpace(new Raster(40, 20), Cameras.NINETY_DEGREE_FOV))
        .isWithin(EPSILON)
        .of(0.1);
  }

  @Test
  public void pixelHeightWithFovEffect() {
    assertThat(getPixelHeightInCameraSpace(new Raster(20, 20), Cameras.STANDARD_CAMERA))
        // Narrower FOV means a smaller pixel width.
        .isWithin(EPSILON)
        .of(0.05773502691896254);
  }
}
