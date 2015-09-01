package edu.brandeis.cs.cosi155b;

/**
 * Record properties of intersection of a ray with an object
 */
public class RayHit {
    // distance along ray to the first intersection
    public double distance;
    // point at which the ray first intersects the object
    public Point3D iPoint;
    // object that the ray intersects
    public Object3D obj;
}
