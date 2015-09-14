package edu.brandeis.cs.cosi155b.scene;

/**
 * A edu.brandeis.cs.cosi155b.Ray3D consists of a point and a (normalized) direction.
 */

public class Ray3D {
    private final Vector start;
    private final Vector direction;

    /**
     * This represents a 3D ray with a specified start and direction.
     * The direction of a ray is a normalized vector.
     */
    public Ray3D(Vector start, Vector direction) {
        if(direction.magnitude() != 1) {
            direction = direction.normalize();
        }
        this.start = start;
        this.direction = direction;
    }

    /**
     * This returns the point along the ray t units from its origin p
     */

    public Vector atTime(double t) {
        return start.add(direction.scale(t));
    }

    public static void main(String[] args) {
         /* put in some tests here */
    }

    public Vector getStart() {
        return start;
    }

    public Vector getDirection() {
        return direction;
    }
}



