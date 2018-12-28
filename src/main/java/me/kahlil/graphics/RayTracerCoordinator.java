package me.kahlil.graphics;

import static com.google.common.collect.ImmutableList.toImmutableList;

import com.google.common.collect.ImmutableList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

import me.kahlil.scene.Camera;
import me.kahlil.scene.Scene;
import me.kahlil.scene.Raster;

/** Coordinator for managing the ray tracer worker threads via a {@link ExecutorService}. */
public class RayTracerCoordinator {

  private final ExecutorService executor;
  private final int numThreads;

  private Raster raster;
  private Camera camera;
  private Scene scene;

  public RayTracerCoordinator(Raster raster, Camera camera, Scene scene) {
    this.raster = raster;
    this.camera = camera;
    this.scene = scene;
    this.numThreads = Runtime.getRuntime().availableProcessors();
    this.executor = Executors.newFixedThreadPool(this.numThreads);
  }

  public Raster render(boolean shadowsEnabled)
      throws InterruptedException, ExecutionException {

//    RayTracer rayTracer =
//        new SimpleAntiAliaser(
//            raster,
//            camera,
//            new ReflectiveRayTracer(
//                new PhongShading(scene, camera, shadowsEnabled),
//                scene,
//                raster,
//                camera,
//                1),
//            new GridAntiAliasingMethod(1));
    RayTracer rayTracer = new SimpleRayTracer(
        new PhongShading(scene, camera, shadowsEnabled),
        scene,
        raster,
        camera);

    // Construct individual worker threads
    ImmutableList<RayTracerWorker> rayTracerWorkers =
        IntStream.range(0, this.numThreads)
            .mapToObj(
                i ->
                    new RayTracerWorker(
                        rayTracer, raster, i, numThreads))
            .collect(toImmutableList());

    // Start all workers
    ImmutableList<Future<?>> futures =
        rayTracerWorkers.stream().map(executor::submit).collect(toImmutableList());

    // Wait for all workers to finish
    for (Future<?> future : futures) {
      future.get();
    }

    long totalNumTraces = rayTracerWorkers.stream().mapToLong(RayTracerWorker::getNumTraces).sum();

    System.out.printf("Total number of rays traced = %d\n", totalNumTraces);

    return raster;
  }
}
