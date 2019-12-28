package me.kahlil.graphics;

import static com.google.common.truth.Truth.assertThat;
import static java.lang.Math.tan;
import static java.lang.Math.toRadians;
import static me.kahlil.graphics.CoordinateMapper.convertPixelToCameraSpaceCoordinates;
import static me.kahlil.graphics.CoordinateMapper.getPixelHeightInCameraSpace;
import static me.kahlil.graphics.CoordinateMapper.getPixelWidthInCameraSpace;

import me.kahlil.geometry.Vector;
import me.kahlil.scene.Camera;
import me.kahlil.scene.ImmutableCamera;
import me.kahlil.scene.Raster;
import org.junit.Test;

/**
 * Unit tests for {@link CoordinateMapper}.
 */
public class CoordinateMapperTest {

  @Test
  public void pixelToCameraSpace_originIsCorrect() {
    Raster raster = new Raster(6, 6);
    Vector location = new Vector(0, 0, 0);
    Camera camera = ImmutableCamera.builder()
        .setFieldOfVisionDegrees(90)
        .setLocation(location)
        .build();
    Point2D converted = convertPixelToCameraSpaceCoordinates(
        raster,
        camera,
        0,
        0);

    assertThat(converted.getX()).isWithin(0.0001).of(- 5.0 / 6.0);
    assertThat(converted.getY()).isWithin(0.0001).of(5.0 / 6.0);
  }

  /**
   * Example values taken from
   * https://www.scratchapixel.com/lessons/3d-basic-rendering/ray-tracing-generating-camera-rays/generating-camera-rays
   */
  @Test
  public void pixelToCameraSpace_providedExampleIsCorrect() {
    Raster raster = new Raster(6, 6);
    Vector location = new Vector(0, 0, 0);
    Camera camera = ImmutableCamera.builder()
        .setFieldOfVisionDegrees(90)
        .setLocation(location)
        .build();
    Point2D converted = convertPixelToCameraSpaceCoordinates(
        raster,
        camera,
        4,
        2);

    assertThat(converted.getX()).isWithin(0.0001).of(0.5);
    assertThat(converted.getY()).isWithin(0.0001).of(1.0 / 6.0);
  }

  /**
   * Example values taken from
   * https://www.scratchapixel.com/lessons/3d-basic-rendering/ray-tracing-generating-camera-rays/generating-camera-rays
   */
  @Test
  public void pixelToCameraSpace_aspectRatioComputationCorrect() {
    Raster raster = new Raster(3, 6);
    Vector location = new Vector(0, 0, 0);
    Camera camera = ImmutableCamera.builder()
        .setFieldOfVisionDegrees(90)
        .setLocation(location)
        .build();
    Point2D converted = convertPixelToCameraSpaceCoordinates(
        raster,
        camera,
        4,
        2);

    assertThat(converted.getX()).isWithin(0.0001).of(1.0);
    assertThat(converted.getY()).isWithin(0.0001).of(1.0 / 6.0);
  }

  /**
   * Example values taken from
   * https://www.scratchapixel.com/lessons/3d-basic-rendering/ray-tracing-generating-camera-rays/generating-camera-rays
   */
  @Test
  public void pixelToCameraSpace_fovComputationIsCorrect() {
    Raster raster = new Raster(3, 6);
    Vector location = new Vector(0, 0, 0);
    int fovDegrees = 60;
    Camera camera = ImmutableCamera.builder()
        .setFieldOfVisionDegrees(fovDegrees)
        .setLocation(location)
        .build();
    Point2D converted = convertPixelToCameraSpaceCoordinates(
        raster,
        camera,
        4,
        2);

    assertThat(converted.getX()).isWithin(0.0001).of(tan(toRadians(fovDegrees / 2)) * 1.0);
    assertThat(converted.getY()).isWithin(0.0001).of(tan(toRadians(fovDegrees / 2)) * 0.16666);
  }

  @Test
  public void getPixelWidthInCameraSpace_providedExampleIsCorrect() {
    Raster raster = new Raster(6, 6);
    Vector location = new Vector(0, 0, 0);
    Camera camera = ImmutableCamera.builder()
        .setFieldOfVisionDegrees(90)
        .setLocation(location)
        .build();
    assertThat(getPixelWidthInCameraSpace(raster, camera)).isWithin(.0001).of(1.0 / 3.0);
  }

  @Test
  public void getPixelHeightInCameraSpace_providedExampleIsCorrect() {
    Raster raster = new Raster(6, 6);
    Vector location = new Vector(0, 0, 0);
    Camera camera = ImmutableCamera.builder()
        .setFieldOfVisionDegrees(90)
        .setLocation(location)
        .build();
    assertThat(getPixelHeightInCameraSpace(raster, camera)).isWithin(.0001).of(1.0 / 3.0);
  }
}