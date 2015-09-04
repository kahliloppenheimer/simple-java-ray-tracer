package edu.brandeis.cs.cosi155b.scene;

/**
 * Created by edenzik on 9/3/15.
 */
public class Plane3D implements Object3D {
    private final Point3D topLeft;
    private final Point3D topRight;
    private final Material front;
    private final Material back;
    public Plane3D(Point3D topLeft, Point3D topRight, Material front, Material back) {
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.front = front;
        this.back = back;
    }

    @Override
    public RayHit rayIntersect(Ray3D ray) {
        return null;
    }

    @Override
    public Point3D getNormal(Point3D point) {
        return null;
    }

    @Override
    public Material getInsideMaterial() {
        return front;
    }

    @Override
    public Material getOutsideMaterial() {
        return back;
    }
}
