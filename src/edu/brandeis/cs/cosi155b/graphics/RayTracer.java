package edu.brandeis.cs.cosi155b.graphics;

import edu.brandeis.cs.cosi155b.scene.*;

import java.util.List;
import java.util.Optional;
import java.util.Stack;

/**
 * Created by kahliloppenheimer on 9/2/15.
 */
public class RayTracer {

    private SimpleFrame3D frame;
    private Camera3D camera;
    private Scene3D scene;
    private List<Light3D> lights;

    public RayTracer(SimpleFrame3D frame, Camera3D camera, Scene3D scene) {
        this.frame = frame;
        this.camera = camera;
        this.scene = scene;
        this.lights = scene.getLights();
    }

    public SimpleFrame3D render(int antiAliasFactor, int numThreads) throws InterruptedException {
        SimpleFrame3D cloned = new SimpleFrame3D(
                this.frame.getBottomLeftCorner(),
                this.frame.getWidth(),
                this.frame.getHeight(),
                this.frame.getWidthPx(),
                this.frame.getHeightPx());

        // Increments in coordinate space between actual pixels
        double wDelta = cloned.getWidth() / cloned.getWidthPx();
        double hDelta = cloned.getHeight() / cloned.getHeightPx();

        Stack<Thread> toJoin = new Stack<Thread>();
        // Divide up the rendering into multiple threads
        for(int t = 0; t < numThreads; ++t) {
            final int ithPixel = t;
            Thread renderThread = new Thread(() -> {
                for (int i = 0; i < cloned.getWidthPx(); ++i) {
                    for (int j = ithPixel; j < cloned.getHeightPx(); j += numThreads) {
                        float[] runningColorSum = new float[3];
                        float[] nextColorToAdd = new float[3];
                        for (int k = 0; k < antiAliasFactor; ++k) {
                            // Create the ray from the camera to the pixel in the frame we are currently coloring
                            double randWDelta = Math.random() * wDelta;
                            double randHDelta = Math.random() * hDelta;
                            Ray3D nextRay = new Ray3D(camera.getLocation(), cloned.getBottomLeftCorner()
                                    .translate(i * wDelta - randWDelta, j * hDelta - randHDelta, 0)
                                    .subtract(camera.getLocation()));

                            Color traced = traceRay(nextRay);
                            traced.getColorComponents(nextColorToAdd);

                            for (int q = 0; q < 3; ++q) {
                                runningColorSum[q] += nextColorToAdd[q];
                            }
                        }
                        float[] colorAverage = {runningColorSum[0] / antiAliasFactor, runningColorSum[1] / antiAliasFactor, runningColorSum[2] / antiAliasFactor};
                        cloned.setPixel(i, j, new Color(colorAverage[0], colorAverage[1], colorAverage[2]));
                    }
                }
                System.out.println("Thread " + ithPixel + " finished!");
            });
            renderThread.start();
            toJoin.push(renderThread);
            System.out.println("Started " + renderThread);
        }

        while(!toJoin.empty()) {
            toJoin.pop().join();
        }

        return cloned;
    }

    /**
     * Returns the passed ray into the scene and returns what color the pixel that the ray
     * goes through should be
     *
     * @param nextRay
     * @return
     */
    private Color traceRay(Ray3D nextRay) {
        Optional<RayHit> optClosest = findFirstIntersection(nextRay, scene);
        // Color the pixel depending on which object was hit
        if (optClosest.isPresent()) {
            RayHit closest = optClosest.get();
            Material material = closest.getObj().getOutsideMaterial();
            // Initialize color with ambient light
            Color lighted = scene.getAmbient().multiply(material.getColor());
            for (Light3D l : lights) {
                // Check to see if shadow should be cast
                if(!isObjectBetweenLightAndPoint(l, closest.getPoint())) {
                    lighted = lighted.add(l.phongIllumination(closest, camera.getLocation()));
                }
            }
            return lighted;
        } else {
            return scene.getBackgroundColor();
        }
    }

    /**
     * Returns true iff there is an object in the scene between the light and the given
     * point
     *
     * @param l
     * @param point
     * @return
     */
    private boolean isObjectBetweenLightAndPoint(Light3D l, Vector point) {
        Vector shadowVec = l.getLocation().subtract(point);
        Optional<RayHit> closestHit = findFirstIntersection(new Ray3D(point.add(shadowVec.scale(.00001)), shadowVec), scene);
        return closestHit.isPresent() && closestHit.get().getDistance() < shadowVec.magnitude();
    }

    /**
     * Returns the RayHit with the lowest distance from the visionVec to each obj in the scene.
     * Returns optional.empty() if no object is hit.
     *
     * @param visionVec
     * @param scene
     * @return
     */
    private Optional<RayHit> findFirstIntersection(Ray3D visionVec, Scene3D scene) {
        RayHit closest = null;
        double closestDistance = Double.POSITIVE_INFINITY;
        for (Object3D o : scene.getObjects()) {
            Optional<RayHit> intersection = o.rayIntersect(visionVec);
            if (intersection.isPresent() && intersection.get().getDistance() < closestDistance) {
                closestDistance = intersection.get().getDistance();
                closest = intersection.get();
            }
        }
        return Optional.ofNullable(closest);
    }
}
