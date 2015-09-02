package edu.brandeis.cs.cosi155b;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by kahliloppenheimer on 9/1/15.
 */
public class Sphere3DTest {

    private static final double DELTA = .000001;
    private static final Sphere3D unitSphere = new Sphere3D(new Point3D(0, 0, 0), 1);

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
        Ray3D r = new Ray3D(new Point3D(0, -2, 0), new Point3D(0, 1, 0));
        RayHit rh = unitSphere.rayIntersect(r);
        assertNotNull(rh);
        assertEquals(rh.getDistance(), 1, DELTA);
        assertEquals(rh.getPoint(), new Point3D(0, -1, 0));
    }
}