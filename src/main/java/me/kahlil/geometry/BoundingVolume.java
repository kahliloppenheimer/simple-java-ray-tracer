package me.kahlil.geometry;

import java.util.Optional;

public interface BoundingVolume {

   Optional<RayHit> intersectWithBoundingVolume(Ray ray);

}
