package edu.brandeis.cs.cosi155b.shapes;

/**
 * A edu.brandeis.cs.cosi155b.Point3D is a triple of doubles that can represent a point or a vector.
 */

public class Point3D {
    // The 3d coordinates of this point object
    private final double x;
    private final double y;
    private final double z;

    public static void main(String[] args) {
        Point3D p1 = new Point3D(1, 2, 3);
        Point3D p2 = new Point3D(4, 5, 6);
        Point3D p3 = new Point3D(1, 2, 2);
        System.out.println("the sum of " + p1 + " and " + p2 + " is " + p1.add(p2) + " and should be (5,7,9)");
        System.out.println("subtracting " + p1 + " from " + p2 + " gives " + p2.subtract(p1) + " and should be (3,3,3) ");
        System.out.println("the length of " + p3 + " is " + p3.length() + " and should be 3");
    }

    public Point3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point3D(Point3D m) {
        this.x = m.getX();
        this.y = m.getY();
        this.z = m.getZ();
    }

    /**
     * return the vector obtained by subtracting q from this point
     *
     * @param q
     * @return
     */
    public Point3D subtract(Point3D q) {
        return this.add(q.scale(-1));
    }

    /**
     * return the point obtained by adding q to this point
     *
     * @param q
     * @return
     */
    public Point3D add(Point3D q) {
        return new Point3D (this.getX() + q.getX(), this.getY() + q.getY(), this.getZ() + q.getZ());
    }

    /**
     * return the point obtained by scaling this point by a
     *
     * @param a
     * @return
     */
    public Point3D scale(double a) {
        return new Point3D(a * getX(), a * getY(), a * getZ());
    }

    /**
     * return the point obtained by translating this point
     * by the specified distances
     *
     * @param xDist
     * @param yDist
     * @param zDist
     * @return
     */
    public Point3D translate(double xDist, double yDist, double zDist) {
        return this.add(new Point3D(xDist, yDist, zDist));
    }

    /**
     * return the dot product of this point and q
     *
     * @param q
     * @return
     */
    public double dot(Point3D q) {
        return getX() * q.getX() + getY() * q.getY() + getZ() * q.getZ();
    }

    /**
     * return the cross product of this point and q
     *
     * @param q
     * @return
     */
    public Point3D cross(Point3D q) {
        double x2 = q.getX();
        double y2 = q.getY();
        double z2 = q.getZ();
        return new Point3D(
                y * z2 - z * y2,
                z * x2 - x * z2,
                x * y2 - y * x2
        );
    }

    /**
     * return the normalization of this vector
     *
     * @return
     */
    public Point3D normalize() {
        double length = this.length();
        return new Point3D(x / length, y / length, z / length);
    }

    /**
     * return the length of this vector
     *
     * @return
     */
    public double length() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
    }

    /**
     * Returns x coordinate of point
     *
     * @return
     */
    public double getX() {
        return x;
    }

    /**
     * Returns y coordinate of point
     *
     * @return
     */
    public double getY() {
        return y;
    }

    /**
     * Returns z coordinate of point
     *
     * @return
     */
    public double getZ() {
        return z;
    }

    /**
     * Returns string formatted as (x, y, z) with two decimal places of accuracy
     *
     * @return formatted point
     */
    @Override
    public String toString() {
        return String.format("(%.2f, %.2f, %.2f)", x, y, z);
    }

    @Override
    public boolean equals(Object p) {
        if(!(p instanceof Point3D)) {
            return false;
        }
        Point3D other = (Point3D) p;
        return other.getX() == x && other.getY() == y && other.getZ() == z;
    }
}

   

