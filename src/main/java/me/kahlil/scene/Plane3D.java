package me.kahlil.scene;

import java.util.Optional;

/**
 * Representation of a plane in 3-dimensional space.
 */
public class Plane3D extends Object3D {
    private final Vector normal;
    private final Vector point;
    private final Material front;

    public Plane3D(Vector point, Vector normal, Material front) {
        this.normal = normal;
        this.point = point;
        this.front = front;
    }

    @Override
    public Optional<RayHit> untransformedIntersection(Ray3D ray) {
        double dn = (ray.getDirection()).dot(normal);
        double time = (point.subtract(ray.getStart())).dot(normal) / dn;
        if(dn == 0.0 || time <= 0) {
            return Optional.empty();
        } else {
            Vector normal = ray.getDirection().dot(this.normal) < 0.0 ? this.normal.scale(1) : this.normal.scale(-1);
            return Optional.of(ImmutableRayHit.builder()
                .setRay(ray)
                .setTime(time)
                .setDistance(ray.atTime(time).subtract(ray.getStart()).magnitude())
                .setIntersection(ray.atTime(time))
                .setNormal(normal)
                .setObject(this)
                .build());
        }
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
