package edu.brandeis.cs.cosi155b.scene;

import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * Created by kahliloppenheimer on 9/13/15.
 */
public class LinearTransformation implements UnaryOperator<Vector> {

    private Matrix matrix;

    private LinearTransformation(Matrix m) {
        this.matrix = m;
    }

    /**
     * Returns a matrix that will translates a vector x units
     * in the x-direction, y units in the y-direction, and z units in the
     * z-direction
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    public static LinearTransformation translate(double x, double y, double z) {
        return new LinearTransformation(new Matrix(new double[][]{
                {1, 0, 0, x},
                {0, 1, 0, y},
                {0, 0, 1, z},
                {0, 0, 0, 1}
        }));
    }

    public static LinearTransformation translate(Vector v) {
        return translate(v.getX(), v.getY(), v.getZ());
    }

    /**
     * Returns a matrix that scales a vector (v1, v2, v3) to
     * (v1 * xFactor, v2 * yFactor, v3 * zFactor)
     * @param xFactor
     * @param yFactor
     * @param zFactor
     * @return
     */
    public static LinearTransformation scale(double xFactor, double yFactor, double zFactor) {
        return new LinearTransformation(new Matrix(new double[][] {
                {xFactor, 0, 0, 0},
                {0, yFactor, 0, 0},
                {0, 0, zFactor, 0},
                {0, 0, 0, 1}
        }));
    }

    @Override
    public Vector apply(Vector vector) {
        return matrix.multiply(vector);
    }

    @Override
    public <V> Function<V, Vector> compose(Function<? super V, ? extends Vector> before) {
        return v -> apply(before.apply(v));
    }

    @Override
    public <V> Function<Vector, V> andThen(Function<? super Vector, ? extends V> after) {
        return p -> after.apply(apply(p));
    }

}
