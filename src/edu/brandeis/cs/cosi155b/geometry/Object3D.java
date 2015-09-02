package edu.brandeis.cs.cosi155b.geometry;

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
    RayHit rayIntersect(Ray3D ray);
}
