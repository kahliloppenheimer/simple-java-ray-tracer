package edu.brandeis.cs.cosi155b.scene;

/**
 * Created by kahliloppenheimer on 9/10/15.
 */
public class Matrix {
    private final double[][] entries;

    public Matrix (double[][] entries) {
        this.entries = entries;
    }

    public double get(int i, int j) {
        return entries[i][j];
    }

    /**
     * Returns a vector representing the ith row of the matrix
     *
     * @param i
     * @return
     */
    public Point3D getRow(int i) {
        return new Point3D(entries[i][0], entries[i][1], entries[i][2]);
    }

    /**
     * Returns a vector representing the jth row of the matrix
     *
     * @param j
     * @return
     */
    public Point3D getColumn(int j) {
        return new Point3D(entries[0][j], entries[1][j], entries[2][j]);
    }

    /**
     * Returns the left-product with the vector. I.e. given this matrix A and vector V,
     * return Av
     *
     * @param vector
     * @return
     */
    public Point3D multiply(Point3D vector) {
        return new Point3D( getRow(0).dot(vector),
                            getRow(1).dot(vector),
                            getRow(2).dot(vector) );
    }

    /**
     * Left multiplies this matrix by the passed matrix. In other words if this matrix is A and other
     * is B, this computes AB
     *
     * @param other
     * @return
     */
    public Matrix multiply(Matrix other) {
        double[][] entries = new double[3][3];
        for(int i = 0; i <= 2; ++i) {
            for(int j = 0; j <= 2; ++j) {
                entries[i][j] = getRow(i).dot(other.getColumn(j));
            }
        }
        return new Matrix(entries);
    }

}
