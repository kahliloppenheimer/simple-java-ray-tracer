package edu.brandeis.cs.cosi155b.geometry;

/**
 * Created by kahliloppenheimer on 9/1/15.
 */
public class Sphere3D implements Object3D {

    public Point3D center;
    public double radius;

    public Sphere3D(Point3D center, double radius) {
        this.center = center;
        this.radius = radius;
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

        if(timeOfFirstIntersection > 0) {
            Point3D intersection = ray.atTime(timeOfFirstIntersection);
            double distance = ray.getStart().subtract(intersection).length();
            return new RayHit(distance, intersection, this);
        } else {
            return null;
        }
    }
}
