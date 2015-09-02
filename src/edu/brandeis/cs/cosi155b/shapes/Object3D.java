package edu.brandeis.cs.cosi155b.shapes;

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
}
