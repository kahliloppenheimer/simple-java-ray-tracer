package edu.brandeis.cs.cosi155b.scene;

/**
 * Created by kahliloppenheimer on 9/1/15.
 */
public class Sphere3D implements Object3D {

    private Point3D center;
    private final double radius;
    // Material of the inside of the sphere
    private final Material inside;
    // Material of the outside of the sphere
    private final Material outside;

    public Sphere3D(Point3D center, double radius, Material inside, Material outside) {
        this.center = center;
        this.radius = radius;
        this.inside = inside;
        this.outside = outside;
    }

    @Override
    public RayHit rayIntersect(Ray3D ray) {
        // coefficients for the quadratic equation we have to solve to find the intersection
        // ax^2 + bx + c = 0
        double a, b, c;
        a = Math.pow(ray.getDirection().length(), 2);
        b = ray.getDirection().scale(2).dot(ray.getStart().subtract(this.center));
        c = Math.pow(ray.getStart().subtract(this.center).length(), 2) - Math.pow(this.radius, 2);

        double determinant = Math.pow(b, 2) - 4 * a * c;
        double timeOfFirstIntersection = -1;

        // Potentially one intersection
        if (-.0000001 <= determinant && determinant <= .0000001) {
            timeOfFirstIntersection = -1 * b / (2 * a);
        } // Potentially two intersections
        else if (determinant > 0) {
            double t1 = (-1 * b - Math.sqrt(determinant)) / (2 * a);
            double t2 = (-1 * b + Math.sqrt(determinant)) / (2 * a);
            timeOfFirstIntersection = t1 > 0 && t2 > 0 ? t1 : t2;
        }

        if (timeOfFirstIntersection > 0) {
            Point3D intersection = ray.atTime(timeOfFirstIntersection);
            double distance = ray.getStart().subtract(intersection).length();
            return new RayHit(distance, intersection, this);
        } else {
            return new RayHit(Double.POSITIVE_INFINITY,
                    new Point3D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY),
                    this);
        }
    }

    public void translate(double x, double y, double z) {
        this.center.translate(x, y, z);
    }

    @Override
    public Material getInsideMaterial() {
        return this.inside;
    }

    @Override
    public Material getOutsideMaterial() {
        return this.outside;
    }

    public Point3D getNormal(Point3D point) {
        return point.subtract(center).normalize();
    }

    public Point3D getCenter() {
        return this.center;
    }

    public double getRadius() {
        return this.radius;
    }

    public void setLocation(double x, double y, double z) {
        this.center = new Point3D(x, y, z);
    }

}
