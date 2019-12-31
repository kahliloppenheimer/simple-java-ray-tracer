package me.kahlil.graphics;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static me.kahlil.config.Counters.NUM_TOTAL_INTERSECTIONS;
import static me.kahlil.config.Counters.NUM_TRACES;
import static me.kahlil.config.Counters.NUM_TRIANGLE_INTERSECTIONS;
import static me.kahlil.config.Parameters.NUM_THREADS;

import com.google.common.collect.ImmutableList;
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

    System.out.printf("Total number of rays traced = %d\n", NUM_TRACES.get());
    System.out.printf(
        "Total number of ray-shape intersections computed = %d\n", NUM_TOTAL_INTERSECTIONS.get());
    System.out.printf(
        "Total number of ray-triangle intersections computed = %d\n",
        NUM_TRIANGLE_INTERSECTIONS.get());

    return raster;
  }
}
