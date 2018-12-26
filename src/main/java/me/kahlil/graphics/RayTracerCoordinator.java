package me.kahlil.graphics;

import static com.google.common.collect.ImmutableList.toImmutableList;

import com.google.common.collect.ImmutableList;
import me.kahlil.scene.Camera3D;
import me.kahlil.scene.Scene3D;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;
import me.kahlil.scene.SimpleFrame;

/**
 * Coordinator for managing the ray tracer worker threads via a {@link ExecutorService}.
 */
public class RayTracerCoordinator {

  private final ExecutorService executor;

  private SimpleFrame frame;
  private Camera3D camera;
  private Scene3D scene;

  public RayTracerCoordinator(SimpleFrame frame, Camera3D camera, Scene3D scene) {
    this.frame = frame;
    this.camera = camera;
    this.scene = scene;
    this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
  }

  public SimpleFrame render(boolean shadowsEnabled, int numThreads)
      throws InterruptedException, ExecutionException {

    RayTracer rayTracer = new SimpleAntiAliaser(
        frame,
        camera,
        new SimpleRayTracer(scene, frame, camera, shadowsEnabled),
        new GridAntiAliasingMethod(2));

    // Construct individual worker threads
    ImmutableList<RayTracerWorker> rayTracerWorkers = IntStream.range(0, numThreads)
        .mapToObj(i ->  new RayTracerWorker(
            rayTracer,
            frame,
            camera,
            scene,
            shadowsEnabled,
            i,
            numThreads))
        .collect(toImmutableList());

    // Start all workers
    ImmutableList<Future<?>> futures = rayTracerWorkers.stream()
        .map(executor::submit)
        .collect(toImmutableList());

    // Wait for all workers to finish
    for (Future<?> future : futures) {
      future.get();
    }

    int totalNumTraces = rayTracerWorkers.stream()
        .mapToInt(RayTracerWorker::getNumTraces)
        .sum();

    System.out.printf("Total number of rays traced = %d\n", totalNumTraces);

    return frame;
  }

}
