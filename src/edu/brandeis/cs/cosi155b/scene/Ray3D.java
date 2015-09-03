package edu.brandeis.cs.cosi155b.scene;

/**
 * A edu.brandeis.cs.cosi155b.Ray3D consists of a point and a (normalized) direction.
 */

public class Ray3D {
    private final Point3D start;
    private final Point3D direction;

    /**
     * This represents a 3D ray with a specified start and direction.
     * The direction of a ray is a normalized vector.
     */
    public Ray3D(Point3D start, Point3D direction) {
        if(direction.length() != 1) {
            direction = direction.normalize();
        }
        this.start = start;
        this.direction = direction;
    }

    /**
     * This returns the point along the ray t units from its origin p
     */

    public Point3D atTime(double t) {
        return start.add(direction.scale(t));
    }

    public static void main(String[] args) {
         /* put in some tests here */
    }

    public Point3D getStart() {
        return start;
    }

    public Point3D getDirection() {
        return direction;
    }
}



