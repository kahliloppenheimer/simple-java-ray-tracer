package edu.brandeis.cs.cosi155b.graphics;

import edu.brandeis.cs.cosi155b.scene.Light3D;
import edu.brandeis.cs.cosi155b.scene.Scene3D;
import java.util.*;

/**
 * Created by kahliloppenheimer on 9/2/15.
 */
public class RayTracerCoordinator {

    private SimpleFrame3D frame;
    private Camera3D camera;
    private Scene3D scene;

    private static final int INIT_ANTI_ALIAS_SAMPLES = 4;

    public RayTracerCoordinator(SimpleFrame3D frame, Camera3D camera, Scene3D scene) {
        this.frame = frame;
        this.camera = camera;
        this.scene = scene;
    }

    public SimpleFrame3D render(boolean shadowsEnabled, int antiAliasSamples, int numThreads) throws InterruptedException {
        Stack<Thread> toJoin = new Stack<>();
        // Divide up the rendering into multiple threads
        for(int t = 0; t < numThreads; ++t) {
            Thread renderThread =
                new Thread(
                    new RayTracerWorker(
                        frame,
                        camera,
                        scene,
                        shadowsEnabled,
                        t,
                        numThreads,
                        antiAliasSamples));
            renderThread.start();
            toJoin.push(renderThread);
            System.out.println("Started " + renderThread);
        }

        while(!toJoin.empty()) {
            toJoin.pop().join();
        }

        return frame;
    }

}
