package edu.brandeis.cs.cosi155b.scene;

import org.junit.Test;

import java.awt.*;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by kahliloppenheimer on 9/1/15.
 */
public class Sphere3DTest {

    private static final double DELTA = .000001;
    private static final Sphere3D unitSphere = new Sphere3D(new Point3D(0, 0, 0), 1, new Material(Color.RED, 5), new Material(Color.RED, 5));

    @Test
    public void testInsiderMaterial() {
        assertEquals(unitSphere.getInsideMaterial().getColor(), Color.RED);
        assertEquals(unitSphere.getInsideMaterial().getShininess(), 5);
    }

    @Test
    public void testOutsideMaterial() {
        assertEquals(unitSphere.getOutsideMaterial().getColor(), Color.RED);
        assertEquals(unitSphere.getOutsideMaterial().getShininess(), 5);
    }

    @Test
    public void testRayIntersectFromInside() {
        Ray3D r = new Ray3D(new Point3D(0, 0, 0), new Point3D(1, 1, 1));
        RayHit rh = unitSphere.rayIntersect(r);
        assertNotNull(rh);
        assertEquals(rh.getDistance(), 1, DELTA);
        assertEquals(rh.getPoint(), new Point3D(1, 1, 1).normalize());
    }

    @Test
    public void testRayIntersectOnceFromOutside() {
        Ray3D r = new Ray3D(new Point3D(-1, 1, 0), new Point3D(1, 0, 0));
        RayHit rh = unitSphere.rayIntersect(r);
        assertNotNull(rh);
        assertEquals(rh.getDistance(), 1, DELTA);
        assertEquals(rh.getPoint(), new Point3D(0, 1, 0));
    }

    @Test
    public void testRayIntersectTwiceFromOutside() {
        for(int i  = 0; i < 100; ++i) {
            Point3D p = getRandPointBiggerThan(1);
            Ray3D ray = new Ray3D(p, p.scale(-1));
            RayHit rh = unitSphere.rayIntersect(ray);
            assertNotNull(rh);
            assertTrue(rh.getDistance() < p.length());
        }
    }

    @Test
    public void testRayDoesNotIntersectSphere() {
        Ray3D r = new Ray3D(new Point3D(0, -2, 0), new Point3D(0, -1, 0));
        RayHit rh = unitSphere.rayIntersect(r);
        assertNull(rh);
    }

    private static Point3D getRandPointBiggerThan(int i) {
        Random rand = new Random();
        return new Point3D(rand.nextInt(100) + i, rand.nextInt(100) + i, rand.nextInt(100) + i);
    }
}