package me.kahlil.octree;

import static com.google.common.truth.Truth.assertThat;
import static me.kahlil.octree.BoundsHelper.computeGlobalMinAndMax;
import static me.kahlil.scene.Materials.DUMMY_MATERIAL;

import com.google.common.collect.ImmutableList;
import me.kahlil.geometry.Triangle;
import me.kahlil.geometry.Vector;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Unit tests for {@link Octree}. */
@RunWith(JUnit4.class)
public class OctreeTest {

  private static final Triangle IN_FIRST_QUADRANT = Triangle.withSurfaceNormals(
      DUMMY_MATERIAL,
      new Vector(0.5, 0.5, 0.5),
      new Vector(0.75, 0.75, 0.75),
      new Vector(1, 1, 1));
  private static final Triangle IN_SECOND_QUADRANT = Triangle.withSurfaceNormals(
      DUMMY_MATERIAL,
      new Vector(-0.5, 0.5, 0.5),
      new Vector(-0.75, 0.75, 0.75),
      new Vector(-1, 1, 1));
  private static final Triangle IN_THIRD_QUADRANT_BACK = Triangle.withSurfaceNormals(
      DUMMY_MATERIAL,
      new Vector(-0.5, -0.5, -0.5),
      new Vector(-0.75, -0.75, -0.75),
      new Vector(-1, -1, -1));
  private static final Triangle OVERLAPPING_FIRST_AND_SECOND = Triangle.withSurfaceNormals(
      DUMMY_MATERIAL,
      new Vector(-0.5, -0.5, 0.5),
      new Vector(0.5, 0.5, 1.0),
      new Vector(0.0, 0.0, 1.0));

  @Test
  public void minMaxBoundsComputedCorrectly() {
    Triangle[] triangles =
        new Triangle[] {
          Triangle.withSurfaceNormals(
              DUMMY_MATERIAL, new Vector(2, 0, 0), new Vector(1, 2, 1), new Vector(1, 1, 2)),
          Triangle.withSurfaceNormals(
              DUMMY_MATERIAL, new Vector(0, 0, -2), new Vector(-1, -2, -1), new Vector(-2, -1, -1))
        };

    assertThat(computeGlobalMinAndMax(triangles))
        .asList()
        .containsExactly(new Vector(-2, -2, -2), new Vector(2, 2, 2));
  }

  @Test
  public void singleNodeOctree_creationParamtersAreCorrect() {
    Triangle[] triangles = {
      Triangle.withSurfaceNormals(
          DUMMY_MATERIAL, new Vector(0, 0, 0), new Vector(1, 1, 1), new Vector(2, 2, 2))
    };
    Octree<Triangle> tree = new Octree<>(triangles, 2, 2);

    assertThat(tree.maxDepth).isEqualTo(2);
    assertThat(tree.maxObjectsPerLeaf).isEqualTo(2);

    assertThat(tree.root.maxDepth).isEqualTo(2);
    assertThat(tree.root.maxObjectsPerLeaf).isEqualTo(2);
    assertThat(tree.root.children).isEqualTo(new OctreeNode[8]);
    assertThat(tree.root.allPolygons).isEqualTo(triangles);
    assertThat(tree.root.depth).isEqualTo(0);

    assertThat(tree.root.min).isEqualTo(new Vector(0, 0, 0));
    assertThat(tree.root.max).isEqualTo(new Vector(2, 2, 2));
  }

  @Test
  public void octreeWithThreeNodes_commonFieldsSetCorrectly() {
    Triangle[] triangles = {IN_FIRST_QUADRANT, IN_SECOND_QUADRANT, IN_THIRD_QUADRANT_BACK};
    Octree<Triangle> tree = new Octree<>(triangles, 2, 2);

    assertThat(tree.root.isLeafNode).isFalse();
    assertThat(tree.root.max).isEqualTo(new Vector(1, 1, 1));
    assertThat(tree.root.min).isEqualTo(new Vector(-1, -1, -1));
    assertThat(tree.root.allPolygons).isEqualTo(triangles);
    assertThat(tree.root.depth).isEqualTo(0);
    // (+x, +y, +z)
    assertThat(tree.root.children[0]).isNotNull();
    // (-x, +y, +z)
    assertThat(tree.root.children[4]).isNotNull();
    // (-x, -y, -z)
    assertThat(tree.root.children[7]).isNotNull();

    OctreeNode<Triangle> firstQuadrantNode = tree.root.children[0];
    OctreeNode<Triangle> secondQuadrantNode = tree.root.children[4];
    OctreeNode<Triangle> thirdQuadrantNode = tree.root.children[7];

    // Common assertions over all children nodes.
    for (OctreeNode<Triangle> node :
        ImmutableList.of(firstQuadrantNode, secondQuadrantNode, thirdQuadrantNode)) {
      assertThat(node.depth).isEqualTo(1);
      assertThat(node.isLeafNode).isTrue();
      assertThat(node.maxObjectsPerLeaf).isEqualTo(2);
      assertThat(node.maxDepth).isEqualTo(2);
      assertThat(node.children).isEqualTo(new OctreeNode[8]);
      assertThat(node.allPolygons).isEqualTo(triangles);
    }
  }

  @Test
  public void octreeWithThreeNodes_quadrantSpecificFieldsSetCorrectly() {
    Triangle[] triangles = {IN_FIRST_QUADRANT, IN_SECOND_QUADRANT, IN_THIRD_QUADRANT_BACK};
    Octree<Triangle> tree = new Octree<>(triangles, 2, 2);

    OctreeNode<Triangle> firstQuadrantNode = tree.root.children[0];
    OctreeNode<Triangle> secondQuadrantNode = tree.root.children[4];
    OctreeNode<Triangle> thirdQuadrantNode = tree.root.children[7];

    assertThat(firstQuadrantNode.boundPolygons).hasSize(1);
    assertThat(triangles[firstQuadrantNode.boundPolygons.get(0)]).isEqualTo(IN_FIRST_QUADRANT);
    assertThat(firstQuadrantNode.min).isEqualTo(new Vector(0, 0, 0));
    assertThat(firstQuadrantNode.max).isEqualTo(new Vector(1, 1, 1));

    assertThat(secondQuadrantNode.boundPolygons).hasSize(1);
    assertThat(triangles[secondQuadrantNode.boundPolygons.get(0)]).isEqualTo(IN_SECOND_QUADRANT);
    assertThat(secondQuadrantNode.min).isEqualTo(new Vector(-1, 0, 0));
    assertThat(secondQuadrantNode.max).isEqualTo(new Vector(0, 1, 1));

    assertThat(thirdQuadrantNode.boundPolygons).hasSize(1);
    assertThat(triangles[thirdQuadrantNode.boundPolygons.get(0)]).isEqualTo(IN_THIRD_QUADRANT_BACK);
    assertThat(thirdQuadrantNode.min).isEqualTo(new Vector(-1, -1, -1));
    assertThat(thirdQuadrantNode.max).isEqualTo(new Vector(0, 0, 0));
  }

  @Test
  public void octreeWithFourNodes_oneOverlapping_fieldsCorrect() {
    Triangle[] triangles = {IN_FIRST_QUADRANT, IN_SECOND_QUADRANT, IN_THIRD_QUADRANT_BACK, OVERLAPPING_FIRST_AND_SECOND};
    Octree<Triangle> tree = new Octree<>(triangles, 2, 2);

    assertThat(tree.root.isLeafNode).isFalse();
    assertThat(tree.root.boundPolygons).hasSize(1);
    assertThat(triangles[tree.root.boundPolygons.get(0)]).isEqualTo(OVERLAPPING_FIRST_AND_SECOND);
  }
}
