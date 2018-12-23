package me.kahlil.scene;

import java.util.Optional;

/**
 * Represents a 3D object in the scene
 */
public abstract class Object3D implements Cloneable {

    private LinearTransformation transformation = LinearTransformation.IDENTITY;

    /**
     * Finds the intersection of the given ray with this potentially transformed object
     *
     * @param ray
     * @return
     */
    public Optional<RayHit> findIntersection(Ray3D ray) {
        // Used to calculate the inverse-transformed ray, but also later to calculate
        // the time that ray must travel to the intersection
        Vector inverseRayDirection = transformation.inverse().apply(ray.getDirection());
        Ray3D inverseTransformed = new Ray3D(transformation.inverse().apply(ray.getStart()), inverseRayDirection);
        Optional<RayHit> optInverseIntersection = untransformedIntersection(inverseTransformed);
        if(!optInverseIntersection.isPresent()) {
            return Optional.empty();
        }
        RayHit inverseIntersection = optInverseIntersection.get();
        Vector point = transformation.apply(inverseIntersection.getPoint());
        Vector normal = transformation.inverseTranspose().apply(inverseIntersection.getNormal());
        // We need to divide the time by the length of the inverse ray direction because
        // we found the intersection of the normalized direction ray
        double time = inverseIntersection.getTime() / inverseRayDirection.magnitude();
        double distance = ray.atTime(time).magnitude();
        return Optional.of(new RayHit(ray, time, distance, point, normal, this));
    }

    /**
     * returns the intersection of the object with the passed
     * Ray3D object, encoded as a RayHit
     *
     * @param ray
     * @return
     */
    public abstract Optional<RayHit> untransformedIntersection(Ray3D ray);

    /**
     * Returns the Material of the inside of the shape
     *
     * @return
     */
    public abstract Material getInsideMaterial();

    /**
     * Returns the Material of the outside of the shape
     *
     * @return
     */
    public abstract Material getOutsideMaterial();

    /**
     * Transforms the object by the given linear transformation
     *
     * @param lt
     */
    public <V extends Object3D> V transform(LinearTransformation lt) {
        Object3D cloned = null;
        try {
            cloned = (Object3D) this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        cloned.transformation = lt.compose(transformation);
        return (V) cloned;
    }
}
