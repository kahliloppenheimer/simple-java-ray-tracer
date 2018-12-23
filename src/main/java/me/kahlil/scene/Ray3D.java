package me.kahlil.scene;

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
        this.start = new Vector(start.getX(), start.getY(), start.getZ(), 1);
        this.direction = new Vector(direction.getX(), direction.getY(), direction.getZ(), 0);
    }

    /**
     * Returns the point along the ray, t units from its origin p
     */
    Vector atTime(double t) {
        return start.add(direction.scale(t));
    }

    public Vector getStart() {
        return start;
    }

    public Vector getDirection() {
        return direction;
    }
}



