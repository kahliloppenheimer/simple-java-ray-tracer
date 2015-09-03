package edu.brandeis.cs.cosi155b.scene;

/**
 * Record properties of intersection of a ray with an object
 */
public class RayHit {
    // distance along ray to the first intersection
    private final double distance;
    // point at which the ray first intersects the object
    private final Point3D iPoint;
    // object that the ray intersects
    private final Object3D obj;

    public RayHit(double distance, Point3D iPoint, Object3D obj) {
        this.distance = distance;
        this.iPoint = iPoint;
        this.obj = obj;
    }

    public double getDistance() {
        return distance;
    }

    public Point3D getPoint() {
        return iPoint;
    }

    public Object3D getObj() {
        return obj;
    }

    public String toString() {
        return String.format("RH[d = %.2f / p = %s / obj = %s]", distance, iPoint, obj.getClass().getName());
    }
}
