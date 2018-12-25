package me.kahlil.geometry;

import java.util.Arrays;

/**
 * A representation of a matrix used for the required linear algebra in the ray tracing algorithm.
 * */
public class Matrix {

    private final double[][] entries;
    public static final Matrix IDENTITY = new Matrix(new double[][] {{1, 0, 0, 0}, {0, 1, 0, 0}, {0, 0, 1, 0}, {0, 0, 0, 1}});
    public static final Matrix ZERO = new Matrix(new double[][] {{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}});

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
        return new Vector( getRow(0).dot4D(vector),
                            getRow(1).dot4D(vector),
                            getRow(2).dot4D(vector),
                            getRow(3).dot4D(vector));
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
        int numRows = getRowCount();
        int numCols = getColumnCount();
        for(int i = 0; i < numRows; ++i) {
            for(int j = 0; j < numCols; ++j) {
                entries[i][j] = getRow(i).dot4D(other.getColumn(j));
            }
        }
        return new Matrix(entries);
    }


    /**
     * Returns the transpose of this matrix, i.e. the matrix formed by using every column
     * of this matrix as the rows of the returned matrix.
     *
     * @return
     */
    public Matrix transpose() {
        int numRows = getRowCount();
        int numCols = getColumnCount();
        double[][] transposed = new double[numRows][numCols];
        for(int i = 0; i < numRows; ++i) {
            for(int j = 0; j < numCols; ++j) {
                transposed[i][j] = entries[j][i];
            }
        }
        return new Matrix(transposed);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Matrix matrix = (Matrix) o;

        return Arrays.deepEquals(entries, matrix.entries);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(entries);
    }

}
