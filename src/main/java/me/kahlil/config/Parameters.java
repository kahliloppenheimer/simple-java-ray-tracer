package me.kahlil.config;

public final class Parameters {

  // File location for demo images.
  public static final String IMAGES_DEMO_PNG_PATH = "images/tmp/demo.png";

  // Output image pixel height/width (only square images for now).
  public static final int IMAGE_SIZE = 400;

  // Number of rays to sample for anti aliasing.
  public static final int NUM_ANTI_ALIASING_SAMPLES = 1;

  // Whether or not shadows are enabled.
  public static final boolean SHADOWS_ENABLED = true;

  // Maximum ray depth for reflections.
  public static final int MAX_RAY_DEPTH = 1;

  // Number of threads to use during computation.
//  public static final int NUM_THREADS = Runtime.getRuntime().availableProcessors();
  public static final int NUM_THREADS = 1;

  // Maximum number of shapes that can occur on a leaf.
  public static final int OCTREE_MAX_SHAPES_PER_LEAF = 100;

  // Maximum depth of the Octree.
  public static final int OCTREE_MAX_DEPTH = 100;
}
