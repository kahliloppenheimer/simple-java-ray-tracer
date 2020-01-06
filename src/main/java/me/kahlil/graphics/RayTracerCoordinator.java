package me.kahlil.graphics;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static me.kahlil.config.Counters.NUM_BOUNDING_INTERSECTIONS;
import static me.kahlil.config.Counters.NUM_BOUNDING_INTERSECTION_TESTS;
import static me.kahlil.config.Counters.NUM_INTERSECTIONS;
import static me.kahlil.config.Counters.NUM_OCTREE_INTERNAL_INSERTIONS;
import static me.kahlil.config.Counters.NUM_OCTREE_CHILD_INSERTIONS;
import static me.kahlil.config.Counters.NUM_PRIMARY_RAYS;
import static me.kahlil.config.Counters.NUM_INTERSECTION_TESTS;
import static me.kahlil.config.Counters.NUM_TOTAL_RAYS;
import static me.kahlil.config.Counters.NUM_TRIANGLES;
import static me.kahlil.config.Counters.NUM_TRIANGLE_INTERSECTIONS;
import static me.kahlil.config.Counters.NUM_TRIANGLE_TESTS;
import static me.kahlil.config.Parameters.NUM_THREADS;

import com.google.common.collect.ImmutableList;
import java.text.NumberFormat;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;
import me.kahlil.scene.Camera;
import me.kahlil.scene.Raster;
import me.kahlil.scene.Scene;

/** Coordinator for managing the ray tracer worker threads via a {@link ExecutorService}. */
public class RayTracerCoordinator {

  private static final NumberFormat numberFormat = NumberFormat.getNumberInstance();

  private final ExecutorService executor;

  private final Raster raster;
  private final Camera camera;
  private final Scene scene;
  private final RayTracer rayTracer;

  public RayTracerCoordinator(Raster raster, Camera camera, Scene scene, RayTracer rayTracer) {
    this.raster = raster;
    this.camera = camera;
    this.scene = scene;
    this.rayTracer = rayTracer;
    this.executor = Executors.newFixedThreadPool(NUM_THREADS);
  }

  public Raster render() throws InterruptedException, ExecutionException {

    // Construct individual worker threads.
    ImmutableList<RayTracerWorker> rayTracerWorkers =
        IntStream.range(0, NUM_THREADS)
            .mapToObj(i -> new RayTracerWorker(rayTracer, raster, i, NUM_THREADS))
            .collect(toImmutableList());

    // Start all workers.
    ImmutableList<Future<?>> futures =
        rayTracerWorkers.stream().map(executor::submit).collect(toImmutableList());

    // Wait for all workers to finish.
    for (Future<?> future : futures) {
      future.get();
    }

    // Kill executor now that work is done.
    executor.shutdown();

    System.out.printf("# primary rays = %s\n", numberFormat.format(NUM_PRIMARY_RAYS.get()));
    System.out.printf("# total rays traced = %s\n", numberFormat.format(NUM_TOTAL_RAYS.get()));
    System.out.printf("# triangles = %s\n", numberFormat.format(NUM_TRIANGLES.get()));
    System.out.printf(
        "# ray-triangle intersection tests = %s\n", numberFormat.format(NUM_TRIANGLE_TESTS.get()));
    System.out.printf(
        "# ray-triangle actual intersections = %s (%s%%)\n",
        numberFormat.format(NUM_TRIANGLE_INTERSECTIONS.get()),
        numberFormat.format(100.0 * NUM_TRIANGLE_INTERSECTIONS.get() / NUM_TRIANGLE_TESTS.get()));
    System.out.printf(
        "# ray-bounding-volume intersection tests = %s\n", numberFormat.format(NUM_BOUNDING_INTERSECTION_TESTS.get()));
    System.out.printf(
        "# ray-bounding-volume actual intersections = %s (%s%%)\n",
        numberFormat.format(NUM_BOUNDING_INTERSECTIONS.get()),
        numberFormat.format(100.0 * NUM_BOUNDING_INTERSECTIONS.get() / NUM_BOUNDING_INTERSECTION_TESTS.get()));
    System.out.printf(
        "# ray-shape intersection tests = %s\n", numberFormat.format(NUM_INTERSECTION_TESTS.get()));
    System.out.printf(
        "# ray-shape actual intersections = %s (%s%%)\n",
        numberFormat.format(NUM_INTERSECTIONS.get()),
        numberFormat.format(100.0 * NUM_INTERSECTIONS.get() / NUM_INTERSECTION_TESTS.get()));
    System.out.printf(
        "# octree internal insertions = %s\n", numberFormat.format(NUM_OCTREE_INTERNAL_INSERTIONS.get()));
    System.out.printf(
        "# octree child insertions = %s\n", numberFormat.format(NUM_OCTREE_CHILD_INSERTIONS.get()));

    return raster;
  }
}
