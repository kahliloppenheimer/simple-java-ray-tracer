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
    public RayHit rayIntersect(Ray3D ray) {
        // Check to make sure ray is not parallel to plane
        /*
        if(normal.dot(ray.getDirection()) < .000001 && normal.dot(ray.getDirection()) > -.0000001) {
            return new RayHit(Double.POSITIVE_INFINITY, null, this);
        }
        */

        double t = normal.dot(ray.getStart().subtract(point)) / normal.dot(ray.getDirection());
        Point3D intersection = ray.atTime(t);
        double distance = ray.getStart().subtract(intersection).length();
        return new RayHit(distance,intersection,this);
    }

    @Override
    public Point3D getNormal(Point3D point) {
        return normal.scale(-1);
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
