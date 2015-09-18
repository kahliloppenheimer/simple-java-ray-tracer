package edu.brandeis.cs.cosi155b.scene;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A edu.brandeis.cs.cosi155b.Vector is a triple of doubles that can represent a point or a vector.
 */

public class Vector {

    // Used for double equality checks
    private static final double EPSILON = .00000001;

    // Coordinates of vector in 3D space
    private final double x;
    private final double y;
    private final double z;

    // 4th-dimensional coordinate used for matrix transforms
    private final double w;

    public Vector(double x, double y, double z) {
        this(x, y, z, 0);
    }

    public Vector(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    /**
     * return the vector obtained by subtracting q from this point
     *
     * @param q
     * @return
     */
    public Vector subtract(Vector q) {
        return this.add(q.scale(-1));
    }

    /**
     * return the point obtained by adding q to this point
     *
     * @param q
     * @return
     */
    public Vector add(Vector q) {
        return new Vector(getX() + q.getX(), getY() + q.getY(), getZ() + q.getZ(), Math.min(w + q.w, 1));
    }

    /**
     * Translates this 3-entry vector by the specified units
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    public Vector translate(double x, double y, double z) {
        return new Vector(getX() + x, getY() + y, getZ() + z, w);
    }

    /**
     * return the point obtained by scaling this point by a
     *
     * @param a
     * @return
     */
    public Vector scale(double a) {
        return new Vector(getX() * a, getY() * a, getZ() * a, w);
    }

    /**
     * return the dot product of this point and q, ignoring the 4th component
     *
     * @param q
     * @return
     */
    public double dot(Vector q) {
        return getX() * q.getX() + getY() * q.getY() + getZ() * q.getZ();
    }

    /**
     * Returns the dot product of this vector and q, taking the 4th component
     * into account
     *
     * @param q
     * @return
     */
    public double dot4D(Vector q) {
        return getX() * q.getX() + getY() * q.getY() + getZ() * q.getZ() + w * q.w;
    }

    /**
     * return the cross product of this point and q
     *
     * @param q
     * @return
     */
    public Vector cross(Vector q) {
        return new Vector(
                getY() * q.getZ() - getZ() * q.getY(),
                getZ() * q.getX() - getX() * q.getZ(),
                getX() * q.getY() - getY() * q.getX()
        );
    }

    /**
     * return the normalization of this vector
     *
     * @return
     */
    public Vector normalize() {
        double length = this.magnitude();
        return new Vector(getX() / length, getY() / length, getZ() / length, w);
    }

    /**
     * return the magnitude of this vector
     *
     * @return
     */
    public double magnitude() {
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

    @Override
    public String toString() {
        return String.format("(%.2f, %.2f, %.2f, %.2f)", x, y, z, w);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector vector = (Vector) o;

        if (Math.abs(vector.x - x) > EPSILON) return false;
        if (Math.abs(vector.y - y) > EPSILON) return false;
        return Math.abs(vector.z - z) < EPSILON;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(z);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}

