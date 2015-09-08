package edu.brandeis.cs.cosi155b.scene;

/**
 * Created by edenzik on 9/3/15.
 * If dn > 0
 */
public class Plane3D implements Object3D {
    private final Point3D normal;
    private final Point3D point;
    private final Material front;

    public Plane3D(Point3D point, Point3D normal, Material front) {
        this.normal = normal;
        this.point = point;
        this.front = front;
    }

    @Override
    public RayHit rayIntersect(Ray3D r) {
        double dn = (r.getDirection()).dot(normal);
        double t = (point.subtract(r.getStart())).dot(normal) / dn;
        if(dn == 0.0 || t <= 0) {
            return new RayHit(r, Double.POSITIVE_INFINITY, null, this);
        } else {
            return new RayHit(r, r.atTime(t).subtract(r.getStart()).length(), r.atTime(t), this);
        }
    }

    @Override
    public Point3D getNormal(Ray3D ray) {
        return ray.getDirection().dot(normal) < 0.0 ? normal.scale(1).normalize() : normal.scale(-1).normalize();
//        return normal.dot(point) > 0 ? normal : normal.scale(-1);
    }

    @Override
    public Material getInsideMaterial() {
        return front;
    }

    @Override
    public Material getOutsideMaterial() {
        return front;
    }

    @Override
    public void translate(double x, double y, double z) {

    }

    @Override
    public void setLocation(double x, double y, double z) {

    }
}
