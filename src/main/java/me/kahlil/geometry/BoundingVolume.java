package me.kahlil.geometry;

public interface BoundingVolume {

   /**
    * Returns time of intersection with bounding volume. -1 if no intersection occurs.
    */
   double intersectWithBoundingVolume(Ray ray);

}
