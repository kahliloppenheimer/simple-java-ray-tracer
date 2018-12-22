package edu.brandeis.cs.cosi155b.scene;

import edu.brandeis.cs.cosi155b.graphics.Color;
import org.junit.Test;

import java.util.Optional;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by kahliloppenheimer on 9/1/15.
 */
public class Sphere3DTest {

    private static final double DELTA = .000001;
    private static final Sphere3D unitSphere = new Sphere3D(new Vector(0, 0, 0), 1, new Material(Color.RED, 55, .25), new Material(Color.RED, 55, .25));

    @Test
    public void testInsiderMaterial() {
        assertEquals(unitSphere.getInsideMaterial().getColor(), Color.RED);
        assertEquals(unitSphere.getInsideMaterial().getHardness(), 55);
    }

    @Test
    public void testOutsideMaterial() {
        assertEquals(unitSphere.getOutsideMaterial().getColor(), Color.RED);
        assertEquals(unitSphere.getOutsideMaterial().getHardness(), 55);
    }

    @Test
    public void testRayIntersectFromInside() {
        Ray3D r = new Ray3D(new Vector(0, 0, 0), new Vector(1, 1, 1));
        Optional<RayHit> rh = unitSphere.untransformedIntersection(r);
        assertTrue(rh.isPresent());
        assertEquals(rh.get().getDistance(), 1, DELTA);
        assertEquals(rh.get().getPoint(), new Vector(1, 1, 1).normalize());
    }

    @Test
    public void testRayIntersectOnceFromOutside() {
        Ray3D r = new Ray3D(new Vector(-1, 1, 0), new Vector(1, 0, 0));
        Optional<RayHit> rh = unitSphere.untransformedIntersection(r);
        assertTrue(rh.isPresent());
        assertEquals(rh.get().getDistance(), 1, DELTA);
        assertEquals(rh.get().getPoint(), new Vector(0, 1, 0));
    }

    @Test
    public void testRayIntersectTwiceFromOutside() {
        for(int i  = 0; i < 100; ++i) {
            Vector p = getRandPointBiggerThan(1);
            Ray3D ray = new Ray3D(p, p.scale(-1));
            Optional<RayHit> rh = unitSphere.untransformedIntersection(ray);
            assertTrue(rh.isPresent());
            assertTrue(rh.get().getDistance() < p.magnitude());
        }
    }

    @Test
    public void testRayDoesNotIntersectSphere() {
        Ray3D r = new Ray3D(new Vector(0, -2, 0), new Vector(0, -1, 0));
        Optional<RayHit> rh = unitSphere.untransformedIntersection(r);
        assertFalse(rh.isPresent());
    }

    private static Vector getRandPointBiggerThan(int i) {
        Random rand = new Random();
        return new Vector(rand.nextInt(100) + i, rand.nextInt(100) + i, rand.nextInt(100) + i);
    }
}