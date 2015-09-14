package edu.brandeis.cs.cosi155b.scene;

/**
 * Record properties of intersection of a ray with an object
 */
public class RayHit {
    // Ray that caused the rayHit
    private final Ray3D ray;
    // distance along ray to the first intersection
    private final double distance;
    // point at which the ray first intersects the object
    private final Vector iPoint;
    // Normal vector to object at the point that the ray intersects the object
    private final Vector normal;
    // object that the ray intersects
    private final Object3D obj;

    public RayHit(Ray3D ray, double distance, Vector iPoint, Vector normal, Object3D obj) {
        this.ray = ray;
        this.distance = distance;
        this.iPoint = iPoint;
        this.normal = normal.normalize();
        this.obj = obj;
    }

    public double getDistance() {
        return distance;
    }

    public Vector getPoint() {
        return iPoint;
    }

    public Object3D getObj() {
        return obj;
    }

    public String toString() {
        return String.format("RH[d = %.2f / p = %s / obj = %s]", distance, iPoint, obj.getClass().getName());
    }

    public Ray3D getRay() {
        return ray;
    }

    public Vector getNormal() {
        return normal;
    }
}
