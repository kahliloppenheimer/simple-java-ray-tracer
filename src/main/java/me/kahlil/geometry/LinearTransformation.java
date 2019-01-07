package me.kahlil.geometry;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

import java.util.Objects;
import java.util.function.UnaryOperator;

/** Representation of a given linear transformation (i.e. translate, scale, rotate). */
public class LinearTransformation implements UnaryOperator<Vector> {

  // Identity map
  static final LinearTransformation IDENTITY =
      new LinearTransformation(Matrix.IDENTITY, Matrix.IDENTITY);

  private Matrix matrix;
  private Matrix inverse;

  private LinearTransformation(Matrix matrix, Matrix inverse) {
    this.matrix = matrix;
    this.inverse = inverse;
  }

  /** Factory method to construct matrices that are orthogonal (i.e. inverse(M) = transpose(M)). */
  private static LinearTransformation transformationForOrthogonalMatrix(Matrix transform) {
    return new LinearTransformation(transform, transform.transpose());
  }

  /**
   * Returns a matrix that will translates a vector x units in the x-direction, y units in the
   * y-direction, and z units in the z-direction
   */
  public static LinearTransformation translate(double x, double y, double z) {
    return new LinearTransformation(
        new Matrix(
            new double[][] {
              {1, 0, 0, x},
              {0, 1, 0, y},
              {0, 0, 1, z},
              {0, 0, 0, 1}
            }),
        new Matrix(
            new double[][] {
              {1, 0, 0, -1 * x},
              {0, 1, 0, -1 * y},
              {0, 0, 1, -1 * z},
              {0, 0, 0, 1}
            }));
  }

  /**
   * Returns the translating transformation with each of v's components as each translation factor.
   */
  public static LinearTransformation translate(Vector v) {
    return translate(v.getX(), v.getY(), v.getZ());
  }

  /**
   * Uniformly scales (x, y, z) components by factor.
   */
  public static LinearTransformation scale(double factor) {
    return scale(factor, factor, factor);
  }

  /** Returns the scaling transformation with each of v's components as each scaling factor. */
  public static LinearTransformation scale(Vector v) {
    return scale(v.getX(), v.getY(), v.getZ());
  }

  /**
   * Returns a matrix that scales a vector (v1, v2, v3) to (v1 * xFactor, v2 * yFactor, v3 *
   * zFactor).
   */
  public static LinearTransformation scale(double xFactor, double yFactor, double zFactor) {
    return new LinearTransformation(
        new Matrix(
            new double[][] {
              {xFactor, 0, 0, 0},
              {0, yFactor, 0, 0},
              {0, 0, zFactor, 0},
              {0, 0, 0, 1}
            }),
        new Matrix(
            new double[][] {
              {1 / xFactor, 0, 0, 0},
              {0, 1 / yFactor, 0, 0},
              {0, 0, 1 / zFactor, 0},
              {0, 0, 0, 1}
            }));
  }

  /** Returns the rotation of the Vector v about the x axis counterclockwise by theta degrees. */
  public static LinearTransformation rotateAboutXAxis(double theta) {
    // Convert theta from degrees to radians.
    theta = toRadians(theta);
    Matrix rotateAboutX =
        new Matrix(
            new double[][] {
              {1, 0, 0, 0},
              {0, cos(theta), -1.0 * sin(theta), 0},
              {0, sin(theta), cos(theta), 0},
              {0, 0, 0, 1}
            });
    return transformationForOrthogonalMatrix(rotateAboutX);
  }

  /** Returns the rotation of the Vector v about the x axis counterclockwise by theta degrees. */
  public static LinearTransformation rotateAboutYAxis(double theta) {
    // Convert theta from degrees to radians.
    theta = toRadians(theta);
    Matrix rotateAboutY =
        new Matrix(
            new double[][] {
              {cos(theta), 0, sin(theta), 0},
              {0, 1.0, 0, 0},
              {-1.0 * sin(theta), 0, cos(theta), 0},
              {0, 0, 0, 1}
            });
    return transformationForOrthogonalMatrix(rotateAboutY);
  }

  /** Returns the rotation of the Vector v about the x axis counterclockwise by theta degrees. */
  public static LinearTransformation rotateAboutZAxis(double theta) {
    // Convert theta from degrees to radians.
    theta = toRadians(theta);
    Matrix rotateAboutZ =
        new Matrix(
            new double[][] {
              {cos(theta), -1.0 * sin(theta), 0, 0},
              {sin(theta), cos(theta), 0, 0},
              {0, 0, 1, 0},
              {0, 0, 0, 1}
            });
    return transformationForOrthogonalMatrix(rotateAboutZ);
  }

  /** Applies this linear transformation to the given vector. */
  @Override
  public Vector apply(Vector vector) {
    return matrix.multiply(vector);
  }

  /**
   * Returns the linear transformation that represents applying the current linear transformation,
   * followed by applying lt.
   */
  public LinearTransformation then(LinearTransformation lt) {
    return new LinearTransformation(
        lt.matrix.multiply(this.matrix), this.inverse.multiply(lt.inverse));
  }

  /** Returns the inverse linear transformation. */
  public LinearTransformation inverse() {
    return new LinearTransformation(inverse, matrix);
  }

  /** Returns the transpose matrix of the inverse linear transformation */
  public LinearTransformation inverseTranspose() {
    return new LinearTransformation(inverse.transpose(), matrix.transpose());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LinearTransformation that = (LinearTransformation) o;
    return Objects.equals(matrix, that.matrix) && Objects.equals(inverse, that.inverse);
  }

  Matrix getMatrix() {
    return this.matrix;
  }

  @Override
  public int hashCode() {
    return Objects.hash(matrix, inverse);
  }

  public String toString() {
    return String.format("Linear transformation represented by matrix:\n%s", matrix.toString());
  }
}
