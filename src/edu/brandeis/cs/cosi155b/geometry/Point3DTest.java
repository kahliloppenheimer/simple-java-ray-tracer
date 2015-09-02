package edu.brandeis.cs.cosi155b.geometry;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by kahliloppenheimer on 9/1/15.
 */
public class Point3DTest {

    private static final double DELTA = .0001;
    private static final Point3D ZERO_VEC = new Point3D(0, 0, 0);

    @Test
    public void testConstructorsAndGetters() {
        Point3D p0 = new Point3D(0, 0, 0);
        assertEquals(p0, ZERO_VEC);

        Point3D p1 = new Point3D(1, 2, 3);
        assertEquals(p1.getX(), 1.0, DELTA);
        assertEquals(p1.getY(), 2.0, DELTA);
        assertEquals(p1.getZ(), 3.0, DELTA);

        Point3D p2 = new Point3D(1.0, 2.0, 3.0);
        assertEquals(p2.getX(), 1.0, DELTA);
        assertEquals(p2.getY(), 2.0, DELTA);
        assertEquals(p2.getZ(), 3.0, DELTA);
    }

    @Test
    public void testSubtract() {
        assertEquals(ZERO_VEC.subtract(ZERO_VEC), ZERO_VEC);

        Point3D p1 = new Point3D(1, 2, 3);
        assertEquals(p1.subtract(ZERO_VEC), p1);
        assertEquals(ZERO_VEC.subtract(p1), p1.scale(-1));
        assertEquals(p1.subtract(p1), ZERO_VEC);

        Point3D p2 = new Point3D(-2, 4, -5);
        assertEquals(p1.subtract(p2), new Point3D(3, -2, 8));
        assertEquals(p2.subtract(p1), new Point3D(-3, 2, -8));
    }

    @Test
    public void testAdd() {
        assertEquals(ZERO_VEC.add(ZERO_VEC), ZERO_VEC);

        Point3D p1 = new Point3D(1, 2, 3);
        assertEquals(p1.add(ZERO_VEC), p1);
        assertEquals(ZERO_VEC.add(p1), p1);

        Point3D p2 = new Point3D(-4, 5, 2);
        assertEquals(p1.add(p2), new Point3D(-3, 7, 5));
        assertEquals(p1.add(p2).add(p2), new Point3D(-7, 12, 7));
    }

    @Test
    public void testScale() {
        assertEquals(ZERO_VEC.scale(0), ZERO_VEC);

        Point3D p = new Point3D (1, 2, 3);
        assertEquals(p.scale(1), p);
        assertEquals(p.scale(0), ZERO_VEC);

        assertEquals(p.scale(2), new Point3D(2, 4, 6));
        assertEquals(p.scale(1.5), new Point3D(1.5, 3, 4.5));
        assertEquals(p.scale(-3), new Point3D(-3, -6, -9));
    }

    @Test
    public void testDot() {
        assertEquals(ZERO_VEC.dot(ZERO_VEC), 0, DELTA);

        Point3D p1 = new Point3D(1, -2, 3);
        assertEquals(p1.dot(ZERO_VEC), 0, DELTA);

        Point3D p2 = new Point3D(-4, 4, 4);
        assertEquals(p1.dot(p2), 0, DELTA);

        Point3D p3 = new Point3D(1, 2, 2);
        assertEquals(p1.dot(p3), 3, DELTA);

        assertEquals(p2.dot(p3), 12, DELTA);
    }

    @Test
    public void testCross() {
        // Test with integer vectors
        for(int i = 0; i < 100; ++i) {
            Point3D p1 = randomIntPoint();
            Point3D p2 = randomIntPoint();
            Point3D crossed = p1.cross(p2);
            // Check order switches sign
            assertEquals(crossed, p2.cross(p1).scale(-1));
            // Check perpendicular
            assertEquals(crossed.dot(p1), 0, DELTA);
            assertEquals(crossed.dot(p2), 0, DELTA);
        }

        // Test with double vectors
        for(int i = 0; i < 100; ++i) {
            Point3D p1 = randomDoublePoint();
            Point3D p2 = randomDoublePoint();
            Point3D crossed = p1.cross(p2);
            // Check order switches sign
            assertEquals(crossed, p2.cross(p1).scale(-1));
            // Check perpendicular
            assertEquals(crossed.dot(p1), 0, DELTA);
            assertEquals(crossed.dot(p2), 0, DELTA);
        }
    }

    @Test
    public void testNormalize() {
        assertEquals(new Point3D(2, 3, 4).normalize(), new Point3D(2 / Math.sqrt(29), 3 / Math.sqrt(29), 4 / Math.sqrt(29)));
        for(int i = 0; i < 100; ++i) {
            assertEquals(randomIntPoint().normalize().length(), 1, DELTA);
            assertEquals(randomDoublePoint().normalize().length(), 1, DELTA);
        }
    }

    @Test
    public void testLength() {
        assertEquals(ZERO_VEC.length(), 0, DELTA);

        Point3D p1 = new Point3D(2, 3, 4);
        assertEquals(p1.length(), Math.sqrt(29), DELTA);
    }

    /**
     * Returns new point with integer components
     *
     * @return
     */
    public Point3D randomIntPoint() {
        Random rand = new Random();
        return new Point3D(rand.nextInt(100), rand.nextInt(100), rand.nextInt(100));
    }

    /**
     * Returns new point with double components
     *
     * @return
     */
    public Point3D randomDoublePoint() {
        Random rand = new Random();
        return new Point3D(rand.nextDouble(), rand.nextDouble(), rand.nextDouble());
    }
}