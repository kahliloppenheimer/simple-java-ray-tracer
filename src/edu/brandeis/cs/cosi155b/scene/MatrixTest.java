package edu.brandeis.cs.cosi155b.scene;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by kahliloppenheimer on 9/10/15.
 */
public class MatrixTest {

    private static final double DELTA = .0000001;
    private Matrix identity;
    private Matrix zero;
    private Random rand;

    @Before
    public void setup() {
        identity = new Matrix(new double[][] {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}});
        zero = new Matrix(new double[][] {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}});
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
        assertEquals(new Point3D(1, 0, 0), identity.getRow(0));
        assertEquals(new Point3D(0, 1, 0), identity.getRow(1));
        assertEquals(new Point3D(0, 0, 1), identity.getRow(2));
    }

    @Test
    public void testGetColumn() throws Exception {
        assertEquals(new Point3D(1, 0, 0), identity.getColumn(0));
        assertEquals(new Point3D(0, 1, 0), identity.getColumn(1));
        assertEquals(new Point3D(0, 0, 1), identity.getColumn(2));
    }

    @Test
    public void testVectorMultiplication() throws Exception {
        Point3D vector = new Point3D(1, 2, 3);
        assertEquals(identity.multiply(vector), vector);
        assertEquals(zero.multiply(vector), new Point3D(0, 0, 0));
        for(int i = 0; i < 100; ++i) {
            double a = rand.nextDouble() * rand.nextInt(100);
            double b = rand.nextDouble() * rand.nextInt(100);
            double c = rand.nextDouble() * rand.nextInt(100);
            Point3D vec = new Point3D(a, b, c);
            assertEquals(identity.multiply(vector), new Point3D(a, b, c));
        }
    }

    @Test
    public void testMatrixMultiplication() throws Exception {
        assertEquals(identity.multiply(identity), identity);
        assertEquals(identity.multiply(zero), zero);
        assertEquals(zero.multiply(identity), zero);
    }
}