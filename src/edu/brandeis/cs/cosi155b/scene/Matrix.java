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
    public Vector getRow(int i) {
        return new Vector(entries[i][0], entries[i][1], entries[i][2], entries[i][3]);
    }

    /**
     * Returns a vector representing the jth row of the matrix
     *
     * @param j
     * @return
     */
    public Vector getColumn(int j) {
        return new Vector(entries[0][j], entries[1][j], entries[2][j], entries[3][j]);
    }

    /**
     * Returns the left-product with the vector. I.e. given this matrix A and vector V,
     * return Av
     *
     * @param vector
     * @return
     */
    public Vector multiply(Vector vector) {
        if(getColumnCount() != 4) {
            throw new IllegalArgumentException(vector + " must have magnitude " + getColumnCount());
        }
        return new Vector( getRow(0).dot(vector),
                            getRow(1).dot(vector),
                            getRow(2).dot(vector),
                            getRow(3).dot(vector));
    }

    /**
     * Left multiplies this matrix by the passed matrix. In other words if this matrix is A and other
     * is B, this computes AB
     *
     * @param other
     * @return
     */
    public Matrix multiply(Matrix other) {
        if(getColumnCount() != other.getRowCount()) {
            throw new IllegalArgumentException(other + " must have " + getColumnCount() + " rows!");
        }
        double[][] entries = new double[getRowCount()][other.getColumnCount()];
        for(int i = 0; i <= getRowCount(); ++i) {
            for(int j = 0; j <= other.getColumnCount(); ++j) {
                entries[i][j] = getRow(i).dot(other.getColumn(j));
            }
        }
        return new Matrix(entries);
    }


    public Matrix transpose() {
//        double[][] transposed = new double[]
        return null;

    }

    public int getRowCount() {
        return entries.length;
    }

    public int getColumnCount() {
        return entries[0].length;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < getRowCount(); ++i) {
            sb.append(getRow(i).toString());
        }
        return sb.toString();
    }

}
