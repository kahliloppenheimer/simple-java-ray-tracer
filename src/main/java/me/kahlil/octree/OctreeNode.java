package me.kahlil.octree;

import static com.google.common.base.Preconditions.checkState;
import static java.util.Comparator.comparingDouble;
import static me.kahlil.config.Counters.NUM_OCTREE_CHILD_INSERTIONS;
import static me.kahlil.config.Counters.NUM_OCTREE_INTERNAL_INSERTIONS;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.PriorityQueue;
import me.kahlil.geometry.Extents;
import me.kahlil.geometry.Intersectable;
import me.kahlil.geometry.Polygon;
import me.kahlil.geometry.Ray;
import me.kahlil.geometry.RayHit;
import me.kahlil.geometry.Triangle;
import me.kahlil.geometry.Vector;

/** A representation of a single node within an Octree. */
final class OctreeNode<T extends Polygon> implements Intersectable {

  // Array containing all original shapes stored in the octree. This way, each node need only
  // maintain indexes to shapes within the array.
  @VisibleForTesting T[] allPolygons;
  final int depth;

  final int maxObjectsPerLeaf;
  final int maxDepth;
  final Vector min;
  final Vector max;

  boolean isLeafNode = true;

  // This is a List, even though it's fixed size, because Java Arrays don't handle generic
  // type parameters well.
  @VisibleForTesting final OctreeNode<T>[] children = new OctreeNode[8];

  // This is a list, rather than an array, because it is dynamically sized.
  final List<Integer> boundPolygons;
  // Extents that bound the polygons stored in this node. Only present for nodes with triangles.
  Extents currExtents;
  // Extents that bound the polygons in this node and its children.
  Extents totalExtents;

  OctreeNode(
      T[] allPolygons, int maxObjectsPerLeaf, int maxDepth, Vector min, Vector max, int depth) {
    this.maxObjectsPerLeaf = maxObjectsPerLeaf;
    this.maxDepth = maxDepth;
    this.min = min;
    this.max = max;
    this.allPolygons = allPolygons;
    this.depth = depth;
    this.boundPolygons = new ArrayList<>(maxDepth);
  }

  @Override
  public Optional<RayHit> intersectWith(Ray ray) {
    // Return empty if ray does not intersect with net extents at all for this node.
    if (totalExtents.intersectWithBoundingVolume(ray) < 0) {
      return Optional.empty();
    }
    // Otherwise, see if this node stores any local polygons we need to check against.
    // This will be true for both leaf nodes and internal nodes which store polygons.
    Optional<RayHit> closest = Optional.empty();
    if (!boundPolygons.isEmpty()) {
      closest = currExtents.intersectWith(ray);
    }
    // Finally, intersections of all children. But, do so according in the order of closest-children
    // first by computing the intersection distance to each child extents, as the closer bounding
    // distances will be more likely to contain the correct triangle.
    double[] childExtentsIntersections = intersectWithChildExtents(ray);
    int numIntersections = 0;
    for (double t : childExtentsIntersections) {
      if (t > 0) {
        numIntersections++;
      }
    }
    if (numIntersections == 0) {
      return closest;
    }
    PriorityQueue<Integer> childrenQueue = new PriorityQueue<>(
        numIntersections,
        comparingDouble(childIndex -> childExtentsIntersections[childIndex]));

    for (int i = 0; i < children.length; i++) {
      if (children[i] != null) {
        childrenQueue.add(i);
      }
    }

    while (!childrenQueue.isEmpty()) {
      int childIndex = childrenQueue.remove();
      Optional<RayHit> shapeHit = children[childIndex].intersectWith(ray);
      closest = pickHitWithLowestTime(closest, shapeHit);
      if (closest.isPresent() && !childrenQueue.isEmpty() && closest.get().getTime() < childExtentsIntersections[childrenQueue.peek()]) {
        return closest;
      }
    }
    return closest;
  }

  /**
   * Returns a double[] where the value at index i is the time of intersection with the child at
   * index i in children.
   */
  private double[] intersectWithChildExtents(Ray ray) {
    double[] childExtentsIntersections = new double[children.length];
    for (int i = 0; i < children.length; i++) {
      if (children[i] == null) {
        continue;
      }
      childExtentsIntersections[i] = children[i].intersectWithExtents(ray);
    }
    return childExtentsIntersections;
  }

  double intersectWithExtents(Ray ray) {
    return totalExtents.intersectWithBoundingVolume(ray);
  }

  /**
   * Returns the ray hit with the lowest time if both are present, the ray hit that is present if
   * one is empty, or empty if both are empty.
   */
  private Optional<RayHit> pickHitWithLowestTime(Optional<RayHit> hit1, Optional<RayHit> hit2) {
    if (hit1.isEmpty()) {
      return hit2;
    }
    if (hit2.isEmpty()) {
      return hit1;
    }
    return hit1.get().getTime() < hit2.get().getTime() ? hit1 : hit2;
  }

  /**
   * Performs an insertion into this {@link OctreeNode}, recursively inserting into children nodes
   * if necessary.
   */
  void insert(int shapeIndex) {
    checkInBounds(allPolygons[shapeIndex]);

    // If this is an internal node, simply insert the shape into the correct child.
    if (!isLeafNode) {
      insertIntoCorrectChild(shapeIndex);
      return;
    }

    // Otherwise, insert the shape into this node.
    boundPolygons.add(shapeIndex);

    // If the node has now grown too large, and the octree is not yet too deep, reassign all of the
    // current node's shapes to the proper children.
    if (boundPolygons.size() > maxObjectsPerLeaf && depth < maxDepth) {
      // Keep track of this, because the insertion logic may re-insert at the end of this same
      // list if a particular shape spans multiple child cells.
      int initialSize = boundPolygons.size();
      for (int i = initialSize - 1; i >= 0; i--) {
        insertIntoCorrectChild(boundPolygons.remove(i));
      }
      isLeafNode = false;
    }
  }

  /**
   * Performs a bottom-up traversal of the {@link Octree} to initialize the extents at each node.
   *
   * <p>The {@link Extents} of a leaf node are simply the bounding extent of the shapes it holds.
   * The extents of an internal node are the union of the extents of its leaf nodes, along with any
   * shapes it holds.
   */
  Extents computeExtents() {
    // Check if value has already been computed and memoized.
    if (totalExtents != null && currExtents != null) {
      return totalExtents;
    }

    // Otherwise, recompute by first checking the shapes bound within this node.
    currExtents = Extents.empty();
    Triangle[] allBoundTriangles = getAllBoundTriangles();
    if (allBoundTriangles.length > 0) {
      currExtents = Extents.fromTriangles(allBoundTriangles);
    }

    totalExtents = currExtents;
    // Then, union that extent with any present children.
    if (!isLeafNode) {
      for (OctreeNode<T> child : children) {
        if (child == null) {
          continue;
        }
        totalExtents = totalExtents.union(child.computeExtents());
      }
    }

    // Finally, memoize the result so we need not do this again.
    return totalExtents;
  }

  /** Inserts the given shape index into the correct child, based on its index. */
  private void insertIntoCorrectChild(int shapeIndex) {
    int childIndex = computeChildIndex(allPolygons[shapeIndex].minBound());

    // Check if shape spans multiple child cells.
    if (childIndex != computeChildIndex(allPolygons[shapeIndex].maxBound())) {
      // If so, store it in this internal node and return.
      NUM_OCTREE_INTERNAL_INSERTIONS.getAndIncrement();
      boundPolygons.add(shapeIndex);
      return;
    }

    NUM_OCTREE_CHILD_INSERTIONS.getAndIncrement();

    // Otherwise, first check if the correct child exists.
    if (children[childIndex] == null) {
      Vector min = getMinBoundForChild(childIndex);
      Vector max = getMaxBoundForChild(childIndex);

      children[childIndex] =
          new OctreeNode<T>(allPolygons, maxObjectsPerLeaf, maxDepth, min, max, depth + 1);
    }

    // Then, recursively insert into the child.
    children[childIndex].insert(shapeIndex);
  }

  /** Computes min bounds for the child cell index. */
  private Vector getMinBoundForChild(int childIndex) {
    Vector centroid = min.average(max);
    return new Vector(
        isLeftCell(childIndex) ? min.getX() : centroid.getX(),
        isBottomCell(childIndex) ? min.getY() : centroid.getY(),
        isBackCell(childIndex) ? min.getZ() : centroid.getZ());
  }

  /** Computes max bounds for the child cell index. */
  private Vector getMaxBoundForChild(int childIndex) {
    Vector centroid = min.average(max);
    return new Vector(
        isLeftCell(childIndex) ? centroid.getX() : max.getX(),
        isBottomCell(childIndex) ? centroid.getY() : max.getY(),
        isBackCell(childIndex) ? centroid.getZ() : max.getZ());
  }

  /**
   * Use funky bitwise math to compute the child index of the point.
   *
   * <p>Form of index is comparable to how the linux `chmod` command works, e.g. `chmod 755 ...`.
   */
  private int computeChildIndex(Vector point) {
    Vector nodeCentroid = min.average(max);

    boolean isLeftCell = point.getX() <= nodeCentroid.getX();
    boolean isBottomCell = point.getY() <= nodeCentroid.getY();
    boolean isBackCell = point.getZ() <= nodeCentroid.getZ();

    return (isLeftCell ? 4 : 0) | (isBottomCell ? 2 : 0) | (isBackCell ? 1 : 0);
  }

  /** Uses funky bitwise math to check if child cell represents negtive X values. */
  private boolean isLeftCell(int childIndex) {
    // Check if third bit is set.
    return (childIndex & 4) == 4;
  }

  /** Uses funky bitwise math to check if child cell represents negative Y values. */
  private boolean isBottomCell(int childIndex) {
    // Check if second bit is set.
    return (childIndex & 2) == 2;
  }

  /** Uses funky bitwise math to check if child cell represents negative Z values. */
  private boolean isBackCell(int childIndex) {
    // Check if first bit is set.
    return (childIndex & 1) == 1;
  }

  /** Asserts that the given shape is within bounds of this node. */
  private void checkInBounds(T shape) {
    checkState(
        shapeIsInBounds(shape),
        "Shape has bounds outside of this octree node. shape_bounds= octree_node_bounds=%s",
        ImmutableList.of(min, max),
        ImmutableList.of(shape.minBound(), shape.maxBound()));
  }

  /** Returns whether or not the given shape is within the bounds defined by this cell. */
  private boolean shapeIsInBounds(T shape) {
    for (int i = 0; i < 3; i++) {
      if (shape.minBound().getComponent(i) < min.getComponent(i)) {
        return false;
      }
      if (shape.maxBound().getComponent(i) > max.getComponent(i)) {
        return false;
      }
    }
    return true;
  }

  /** Returns all triangles bound within the polygons contained in the current node. */
  private Triangle[] getAllBoundTriangles() {
    if (boundPolygons.isEmpty()) {
      return new Triangle[] {};
    }
    Triangle[][] allTriangles = new Triangle[boundPolygons.size()][];
    for (int i = 0; i < boundPolygons.size(); i++) {
      allTriangles[i] = allPolygons[boundPolygons.get(i)].getTriangles();
    }
    return concatenate(allTriangles);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OctreeNode<?> that = (OctreeNode<?>) o;
    return depth == that.depth &&
        isLeafNode == that.isLeafNode &&
        min.equals(that.min) &&
        max.equals(that.max) &&
        Arrays.equals(children, that.children) &&
        Objects.equals(boundPolygons, that.boundPolygons) &&
        Objects.equals(currExtents, that.currExtents) &&
        Objects.equals(totalExtents, that.totalExtents);
  }

  @Override
  public int hashCode() {
    int result = Objects
        .hash(depth, min, max, isLeafNode, boundPolygons, currExtents, totalExtents);
    result = 31 * result + Arrays.hashCode(children);
    return result;
  }

  /**
   * Concatenates multiple arrays.
   *
   * <p>From https://stackoverflow.com/questions/80476/how-can-i-concatenate-two-arrays-in-java
   */
  private static <T> T[] concatenate(T[]... arrays)
  {
    int finalLength = 0;
    for (T[] array : arrays) {
      finalLength += array.length;
    }

    T[] dest = null;
    int destPos = 0;

    for (T[] array : arrays)
    {
      if (dest == null) {
        dest = Arrays.copyOf(array, finalLength);
        destPos = array.length;
      } else {
        System.arraycopy(array, 0, dest, destPos, array.length);
        destPos += array.length;
      }
    }
    return dest;
  }
}
