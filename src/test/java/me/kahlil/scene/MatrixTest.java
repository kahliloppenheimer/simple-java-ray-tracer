package me.kahlil.scene;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link Matrix}.
 */
public class MatrixTest {

    private static final double DELTA = .0000001;
    private Matrix identity;
    private Matrix zero;
    private Random rand;

    @Before
    public void setup() {
        identity = new Matrix(new double[][] {{1, 0, 0, 0}, {0, 1, 0, 0}, {0, 0, 1, 0}, {0, 0, 0, 1}});
        zero = new Matrix(new double[][] {{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}});
        rand = new Random();
    }

    @Test
    public void testGet() throws Exception {
        assertEquals(identity.get(0, 0), 1, DELTA);
        assertEquals(identity.get(0, 1), 0, DELTA);
        assertEquals(identity.get(1, 1), 1, DELTA);
        assertEquals(identity.get(2, 0), 0, DELTA);
        assertEquals(identity.get(2, 2), 1, DELTA);
    }

    @Test
    public void testGetRow() throws Exception {
        assertEquals(new Vector(1, 0, 0), identity.getRow(0));
        assertEquals(new Vector(0, 1, 0), identity.getRow(1));
        assertEquals(new Vector(0, 0, 1), identity.getRow(2));
    }

    @Test
    public void testGetColumn() throws Exception {
        assertEquals(new Vector(1, 0, 0), identity.getColumn(0));
        assertEquals(new Vector(0, 1, 0), identity.getColumn(1));
        assertEquals(new Vector(0, 0, 1), identity.getColumn(2));
    }

    @Test
    public void testVectorMultiplication() throws Exception {
        Vector vector = new Vector(1, 2, 3);
        assertEquals(identity.multiply(vector), vector);
        assertEquals(zero.multiply(vector), new Vector(0, 0, 0));
        for(int i = 0; i < 100; ++i) {
            double a = rand.nextDouble() * rand.nextInt(100);
            double b = rand.nextDouble() * rand.nextInt(100);
            double c = rand.nextDouble() * rand.nextInt(100);
            Vector vec = new Vector(a, b, c);
            assertEquals(identity.multiply(vec), new Vector(a, b, c));
        }
    }

    @Test
    public void testMatrixMultiplication() throws Exception {
        assertEquals(identity.multiply(identity), identity);
        assertEquals(identity.multiply(zero), zero);
        assertEquals(zero.multiply(identity), zero);
    }

    @Test
    public void testTranspose() {
        assertEquals(identity.transpose(), identity);
        assertEquals(zero.transpose(), zero);

        Matrix m = new Matrix(new double[][] {
                {1, 0, 1, 0},
                {0, 1, 0, 1},
                {2, 3, 0, 0},
                {1, 5, 5, 5}
        });
        Matrix transposed = new Matrix(new double[][] {
                {1, 0, 2, 1},
                {0, 1, 3, 5},
                {1, 0, 0, 5},
                {0, 1, 0, 5}
        });

        assertEquals(transposed, m.transpose());
    }
}