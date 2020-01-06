package me.kahlil.octree;

import static com.google.common.base.Preconditions.checkState;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import me.kahlil.geometry.BoundingVolume;
import me.kahlil.geometry.Extents;
import me.kahlil.geometry.Polygon;
import me.kahlil.geometry.Ray;
import me.kahlil.geometry.Triangle;
import me.kahlil.geometry.Vector;

/**
 * A representation of a single node within an Octree.
 */
final class OctreeNode<T extends Polygon> implements BoundingVolume {

  // Array containing all original shapes stored in the octree. This way, each node need only
  // maintain indexes to shapes within the array.
  @VisibleForTesting
  T[] allPolygons;
  final int depth;

  final int maxObjectsPerLeaf;
  final int maxDepth;
  final Vector min;
  final Vector max;

  boolean isLeafNode = true;

  // This is a List, even though it's fixed size, because Java Arrays don't handle generic
  // type parameters well.
  @VisibleForTesting
  final OctreeNode<T>[] children = new OctreeNode[8];

  // This is a list, rather than an array, because it is dynamically sized.
  final List<Integer> boundPolygons;
  private Extents extents;

  OctreeNode(T[] allPolygons, int maxObjectsPerLeaf, int maxDepth, Vector min, Vector max, int depth) {
    this.maxObjectsPerLeaf = maxObjectsPerLeaf;
    this.maxDepth = maxDepth;
    this.min = min;
    this.max = max;
    this.allPolygons = allPolygons;
    this.depth = depth;
    this.boundPolygons = new ArrayList<>(maxDepth);
  }

  @Override
  public boolean intersectsWith(Ray ray) {
    return extents.intersectsWith(ray);
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
   * The {@link Extents} of a leaf node are simply the bounding extent of the shapes it holds. The
   * extents of an internal node are the union of the extents of its leaf nodes, along with any
   * shapes it holds.
   */
  Extents computeExtents() {
    // Check if value has already been computed and memoized.
    if (extents != null) {
      return extents;
    }

    // Otherwise, recompute by first checking the shapes bound within this node.
    Extents extents = Extents.fromTriangles(getAllBoundTriangles());

    // Then, union that extent with any present children.
    if (!isLeafNode) {
      for (OctreeNode<T> child : children) {
        if (child == null) {
          continue;
        }
        extents = extents.union(child.computeExtents());
      }
    }

    // Finally, memoize the result so we need not do this again.
    this.extents = extents;
    return extents;
  }


  /**
   * Inserts the given shape index into the correct child, based on its index.
   */
  private void insertIntoCorrectChild(int shapeIndex) {
    int childIndex = computeChildIndex(allPolygons[shapeIndex].minBound());

    // Check if shape spans multiple child cells.
    if (childIndex != computeChildIndex(allPolygons[shapeIndex].maxBound())) {
      // If so, store it in this internal node and return.
      boundPolygons.add(shapeIndex);
      return;
    }

    // Otherwise, first check if the correct child exists.
    if (children[childIndex] == null) {
      Vector min = getMinBoundForChild(childIndex);
      Vector max = getMaxBoundForChild(childIndex);

      children[childIndex] = new OctreeNode<T>(allPolygons, maxObjectsPerLeaf, maxDepth, min, max, depth + 1);
    }

    // Then, recursively insert into the child.
    children[childIndex].insert(shapeIndex);
  }

  /**
   * Computes min bounds for the child cell index.
   */
  private Vector getMinBoundForChild(int childIndex) {
    Vector centroid = min.average(max);
    return new Vector(
        isLeftCell(childIndex) ? min.getX() : centroid.getX(),
        isBottomCell(childIndex) ? min.getY() : centroid.getY(),
        isBackCell(childIndex) ? min.getZ() : centroid.getZ());
  }

  /**
   * Computes max bounds for the child cell index.
   */
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
   * Form of index is comparable to how the linux `chmod` command works, e.g. `chmod 755 ...`.
   */
  private int computeChildIndex(Vector point) {
    Vector nodeCentroid = min.average(max);

    boolean isLeftCell = point.getX() <= nodeCentroid.getX();
    boolean isBottomCell = point.getY() <= nodeCentroid.getY();
    boolean isBackCell = point.getZ() <= nodeCentroid.getZ();

    return (isLeftCell ? 4 : 0) | (isBottomCell ? 2 : 0) | (isBackCell ? 1 : 0);
  }

  /**
   * Uses funky bitwise math to check if child cell represents negtive X values.
   */
  private boolean isLeftCell(int childIndex) {
    // Check if third bit is set.
    return (childIndex & 4) == 4;
  }

  /**
   * Uses funky bitwise math to check if child cell represents negative Y values.
   */
  private boolean isBottomCell(int childIndex) {
    // Check if second bit is set.
    return (childIndex & 2) == 2;
  }

  /**
   * Uses funky bitwise math to check if child cell represents negative Z values.
   */
  private boolean isBackCell(int childIndex) {
    // Check if first bit is set.
    return (childIndex & 1) == 1;
  }

  /**
   * Asserts that the given shape is within bounds of this node.
   */
  private void checkInBounds(T shape) {
    checkState(
        shapeIsInBounds(shape),
        "Shape has bounds outside of this octree node. shape_bounds= octree_node_bounds=%s",
        ImmutableList.of(min, max),
        ImmutableList.of(shape.minBound(), shape.maxBound()));
  }

  /**
   * Returns whether or not the given shape is within the bounds defined by this cell.
   */
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

  /**
   * Returns all triangles bound within the polygons contained in the current node.
   */
  private Triangle[] getAllBoundTriangles() {
    if (boundPolygons.isEmpty()) {
      return new Triangle[]{};
    }
    Triangle[] firstSet = allPolygons[boundPolygons.get(0)].getTriangles();
    Triangle[][] remainingTriangles = new Triangle[boundPolygons.size() - 1][];
    for (int i = 1; i < boundPolygons.size(); i++) {
      remainingTriangles[i] = allPolygons[boundPolygons.get(i)].getTriangles();
    }
    return concatAll(firstSet, remainingTriangles);
  }

  /**
   * Concatenates multiple arrays.
   *
   * From https://stackoverflow.com/questions/80476/how-can-i-concatenate-two-arrays-in-java
   */
  private static <T> T[] concatAll(T[] first, T[]... rest) {
    int totalLength = 0;
    for (T[] array : rest) {
      totalLength += array.length;
    }
    T[] result = Arrays.copyOf(first, totalLength);
    int offset = first.length;
    for (T[] array : rest) {
      System.arraycopy(array, 0, result, offset, array.length);
      offset += array.length;
    }
    return result;
  }
}
