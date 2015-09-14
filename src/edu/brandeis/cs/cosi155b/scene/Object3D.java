package edu.brandeis.cs.cosi155b.scene;

import java.util.Optional;

/**
 * Represents a 3D object in the scene
 */
public interface Object3D {

    /**
     * returns the intersection of the object with the passed
     * Ray3D object, encoded as a RayHit
     *
     * @param ray
     * @return
     */
    Optional<RayHit> rayIntersect(Ray3D ray);

    /**
     * Returns the Material of the inside of the shape
     *
     * @return
     */
    Material getInsideMaterial();

    /**
     * Returns the Material of the outside of the shape
     *
     * @return
     */
    Material getOutsideMaterial();

    /**
     * Translates the given object by each of the specified units
     *
     * @param x
     * @param y
     * @param z
     */
    void translate(double x, double y, double z);

    /**
     * Sets the location of the given object to the following coordinates
     *
     * @param x
     * @param y
     * @param z
     */
    void setLocation(double x, double y, double z);
}
