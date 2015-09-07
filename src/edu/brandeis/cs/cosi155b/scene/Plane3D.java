package edu.brandeis.cs.cosi155b.scene;

/**
 * Created by edenzik on 9/3/15.
 * If dn > 0
 */
public class Plane3D implements Object3D {
    private final Point3D normal;
    private final Point3D point;
    private final Material front;
    public Plane3D(Point3D normal, Point3D point, Material front) {
        this.normal = normal;
        this.point = point;
        this.front = front;
    }

    @Override
    public RayHit rayIntersect(Ray3D ray) {
        double t = normal.dot(ray.getStart().subtract(point)) / normal.dot(ray.getDirection());

        Point3D intersection = ray.atTime(t);
        double distance = ray.getStart().subtract(intersection).length();
        return new RayHit(distance,intersection,this);


    }

    @Override
    public Point3D getNormal(Point3D point) {
        return normal;
    }

    @Override
    public Material getInsideMaterial() {
        return front;
    }

    @Override
    public Material getOutsideMaterial() {
        return front;
    }
}
