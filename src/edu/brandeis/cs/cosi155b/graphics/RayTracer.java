package edu.brandeis.cs.cosi155b.graphics;

import edu.brandeis.cs.cosi155b.scene.*;

import java.awt.*;
import java.util.List;
import java.util.Optional;

/**
 * Created by kahliloppenheimer on 9/2/15.
 */
public class RayTracer {

    private SimpleFrame3D frame;
    private Camera3D camera;
    private Scene3D scene;
    private List<Light3D> lights;

    public RayTracer(SimpleFrame3D frame, Camera3D camera, Scene3D scene, List<Light3D> lights) {
        this.frame = frame;
        this.camera = camera;
        this.scene = scene;
        this.lights = lights;
    }

    public SimpleFrame3D render(int antiAliasFactor) {
        SimpleFrame3D cloned = new SimpleFrame3D(
                this.frame.getBottomLeftCorner(),
                this.frame.getWidth(),
                this.frame.getHeight(),
                this.frame.getWidthPx(),
                this.frame.getHeightPx());

        // Increments in coordinate space between actual pixels
        double wDelta = cloned.getWidth() / cloned.getWidthPx();
        double hDelta = cloned.getHeight() / cloned.getHeightPx();

        for (int i = 0; i < cloned.getWidthPx(); ++i) {
            for (int j = 0; j < cloned.getHeightPx(); ++j) {
                float[] runningColorSum = new float[3];
                float[] nextColorToAdd = new float[3];
                for(int k = 0; k < antiAliasFactor; ++k) {
                    // Create the ray from the camera to the pixel in the frame we are currently coloring
                    double randWDelta = Math.random() * wDelta;
                    double randHDelta = Math.random() * hDelta;
                    Ray3D visionVec = new Ray3D(camera.getLocation(), cloned.getBottomLeftCorner()
                            .translate(i * wDelta - randWDelta, j * hDelta - randHDelta, 0)
                            .subtract(camera.getLocation()));

                    Optional<RayHit> closest = findFirstIntersection(visionVec, scene);
                    Color lighted = computeColor(closest);
                    lighted.getColorComponents(nextColorToAdd);

                    for (int q = 0; q < 3; ++q) {
                        runningColorSum[q] += nextColorToAdd[q];
                    }
                }
                float[] colorAverage = {runningColorSum[0] / antiAliasFactor, runningColorSum[1] / antiAliasFactor, runningColorSum[2] / antiAliasFactor};
                cloned.setPixel(i, j, new Pixel(new Color(colorAverage[0], colorAverage[1], colorAverage[2])));
            }
        }
        return cloned;
    }

    /**
     * Takes in a given RayHit intersection and a collection of lights and returns a
     * pixel with the proper lighted color
     *
     * @param optClosest the RayHit with the shortest distance for the given pixel
     * @return
     */
    private Color computeColor(Optional<RayHit> optClosest) {
        // Color the pixel depending on which object was hit
        if (optClosest.isPresent()) {
            RayHit closest = optClosest.get();
            Point3D point = closest.getPoint();
            Point3D normal = closest.getObj().getNormal(point);
            Material material = closest.getObj().getOutsideMaterial();
            Color lighted = new Color(0, 0, 0);
            for (Light3D l : lights) {
                Point3D lightVec = point.subtract(l.getLocation());
                Point3D eyeVec = camera.getLocation().subtract(point);
                lighted = l.lightPixel(lighted,
                        material.getColor(),
                        l.diffuse(normal),
                        l.specular(lightVec, normal, eyeVec, material),
                        scene.getAmbient());
            }
            return lighted;
        } else {
            return scene.getBackgroundColor();
        }
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
        for (Object3D o : scene) {
            RayHit intersection = o.rayIntersect(visionVec);
            if (intersection.getDistance() < closestDistance) {
                closestDistance = intersection.getDistance();
                closest = intersection;
            }
        }
        return Optional.ofNullable(closest);
    }
}
