package edu.brandeis.cs.cosi155b;

/**
 * Created by kahliloppenheimer on 9/1/15.
 */
public class Sphere3D implements Object3D {

    public Point3D center;
    public double radius;

    public Sphere3D (Point3D center, double radius) {
        this.center = center;
        this.radius = radius;
    }

    @Override
    public RayHit rayIntersect(Ray3D ray) {
        return null;
    }

}
