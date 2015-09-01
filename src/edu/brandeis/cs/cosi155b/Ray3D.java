package edu.brandeis.cs.cosi155b;

/**
 * A edu.brandeis.cs.cosi155b.Ray3D consists of a point and a (normalized) direction.
 */

public class Ray3D {
    public Point3D start;
    public Point3D direction;


    /**
     * This represents a 3D ray with a specified start and direction.
     * The direction of a ray is a normalized vector.
     */
    public Ray3D(Point3D start, Point3D direction) {
        if(direction.length() != 1) {
            throw new IllegalArgumentException("Direction vector must be normalized unit vector!");
        }

    }

    /**
     * This returns the point along the ray t units from its origin p
     */

    public Point3D atTime(double t) {
        return null;
    }

    public static void main(String[] args) {
         /* put in some tests here */
    }
}



