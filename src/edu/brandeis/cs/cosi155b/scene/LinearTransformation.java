package edu.brandeis.cs.cosi155b.scene;

import java.util.function.UnaryOperator;

/**
 * Created by kahliloppenheimer on 9/13/15.
 */
public class LinearTransformation implements UnaryOperator<Vector> {

    // Identity map
    public static final LinearTransformation IDENTITY = new LinearTransformation(Matrix.IDENTITY, Matrix.IDENTITY);

    private Matrix transform;
    private Matrix inverse;

    private LinearTransformation(Matrix transform, Matrix inverse) {
        this.transform = transform;
        this.inverse = inverse;
    }

    /**
     * Returns a transform that will translates a vector x units
     * in the x-direction, y units in the y-direction, and z units in the
     * z-direction
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    public static LinearTransformation translate(double x, double y, double z) {
        return new LinearTransformation(
                new Matrix(new double[][]{
                        {1, 0, 0, x},
                        {0, 1, 0, y},
                        {0, 0, 1, z},
                        {0, 0, 0, 1}
                }),
                new Matrix(new double[][]{
                        {1, 0, 0, -1 * x},
                        {0, 1, 0, -1 * y},
                        {0, 0, 1, -1 * z},
                        {0, 0, 0, 1}
                }));
    }

    /**
     * returns the translating transformation with each of v's components as each
     * translation factor
     *
     * @param v
     * @return
     */
    public static LinearTransformation translate(Vector v) {
        return translate(v.getX(), v.getY(), v.getZ());
    }

    /**
     * Returns a transform that scales a vector (v1, v2, v3) to
     * (v1 * xFactor, v2 * yFactor, v3 * zFactor)
     *
     * @param xFactor
     * @param yFactor
     * @param zFactor
     * @return
     */
    public static LinearTransformation scale(double xFactor, double yFactor, double zFactor) {
        return new LinearTransformation(
                new Matrix(new double[][]{
                        {xFactor, 0, 0, 0},
                        {0, yFactor, 0, 0},
                        {0, 0, zFactor, 0},
                        {0, 0, 0, 1}
                }),
                new Matrix(new double[][]{
                        {1 / xFactor, 0, 0, 0},
                        {0, 1 / yFactor, 0, 0},
                        {0, 0, 1 / zFactor, 0},
                        {0, 0, 0, 1}
                })
        );
    }

    /**
     * Returns the scaling transformation with each of v's components as each scaling factor
     *
     * @param v
     * @return
     */
    public static LinearTransformation scale(Vector v) {
        return scale(v.getX(), v.getY(), v.getZ());
    }

    /**
     * Applies this linear transformation to the given vector
     *
     * @param vector
     * @return
     */
    @Override
    public Vector apply(Vector vector) {
        return transform.multiply(vector);
    }

    /**
     * Returns the composition of this linear transformation with the passed Linear Transformation
     *
     * @param lt
     * @return
     */
    public LinearTransformation compose(LinearTransformation lt) {
        return new LinearTransformation(transform.multiply(lt.transform), inverse.multiply(lt.inverse));
    }

    /**
     * Returns the inverse linear transformation
     *
     * @return
     */
    public LinearTransformation inverse() {
        return new LinearTransformation(inverse, transform);
    }

    /**
     * Returns the transpose matrix of the inverse linear transformation
     *
     * @return
     */
    public LinearTransformation inverseTranspose() {
        return new LinearTransformation(inverse.transpose(), transform.transpose());
    }

}
