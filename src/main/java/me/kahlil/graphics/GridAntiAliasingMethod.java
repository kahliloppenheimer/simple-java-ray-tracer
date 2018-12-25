package me.kahlil.graphics;

import me.kahlil.scene.Ray3D;

/**
 * A method for anti-aliasing by generating a deterministic, uniformly distributed grid of
 * points to sample.
 */
class GridAntiAliasingMethod implements AntiAliasingMethod {

  private final int gridSize;

  /**
   * Constructs an instance given a gridSize, which determines the number of samples along one
   * length of the grid (i.e. the sqrt of the total number of samples that will be taken).
   */
  GridAntiAliasingMethod(int gridSize) {
    this.gridSize = gridSize;
  }

  /**
   * Returns rays sampled deterministically and uniformly in a grid produced from a given sampling
   * radius. The width/height deltas between points in the grid should be:
   *
   * 1/1 * 2 * samplingRadius for grid size of 2 (i.e. leftmost and rightmost).
   * 1/2 * 2 * samplingRadius for grid size of 3 (i.e. leftmost, center, and rightmost).
   * 1/3 * 2 * samplingRadius for grid size of 4 (i.e. leftmost, first-third, second-third, rightmost).
   * ...
   * 1/(n - 1) * 2 * samplingRadius for grid size of n.
   */
  @Override
  public Ray3D[] getRaysToSample(Ray3D ray, SamplingRadius samplingRadius) {
    if (gridSize == 1) {
      return new Ray3D[]{ray};
    }
    // We start in middle of pixel, so offset to lowest values of (x, y) in the grid.
    Ray3D[] samples = new Ray3D[gridSize * gridSize];
    Ray3D origin = new Ray3D(
        ray.getStart(),
        ray.getDirection()
            .translate(-1.0 * samplingRadius.getWidth(), -1.0 * samplingRadius.getHeight()));

    double heightDelta = (1.0 / (gridSize - 1)) * 2 * samplingRadius.getHeight();
    double widthDelta = (1.0 / (gridSize - 1)) * 2 * samplingRadius.getWidth();
    for (int i = 0; i < gridSize; i++) {
      for (int j = 0; j < gridSize; j++) {
        // Translate origin's height/width
        samples[i * gridSize + j] = new Ray3D(
            origin.getStart(),
            origin.getDirection().translate(
                j * widthDelta,
                i * heightDelta));
      }
    }
    return samples;
  }
}
