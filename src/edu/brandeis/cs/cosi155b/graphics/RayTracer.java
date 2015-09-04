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

    private static final int ANTI_ALIASING_FACTOR = 32;


    public RayTracer(SimpleFrame3D frame, Camera3D camera, Scene3D scene, List<Light3D> lights) {
        this.frame = frame;
        this.camera = camera;
        this.scene = scene;
        this.lights = lights;
    }

    public SimpleFrame3D render() {
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
                float[] colorSum = new float[3];
                float[] componentsToSum = new float[3];
                for(int k = 0; k < ANTI_ALIASING_FACTOR; ++k) {
                    // Create the ray from the camera to the pixel in the frame we are currently coloring
                    double randWDelta = Math.random() * wDelta;
                    double randHDelta = Math.random() * hDelta;
                    Ray3D visionVec = new Ray3D(camera.getLocation(), cloned.getBottomLeftCorner()
                            .translate(i * wDelta - randWDelta, j * hDelta - randHDelta, 0)
                            .subtract(camera.getLocation()));

                    Optional<RayHit> closest = findFirstIntersection(visionVec, scene);
                    if(closest.isPresent()) {
                        Pixel lighted = lightPixel(closest, lights);
                        lighted.getColor().getColorComponents(componentsToSum);
                    }

                    for (int q = 0; q < 3; ++q) {
                        colorSum[q] += componentsToSum[q];
                    }
                }
                float[] colorAverage = {colorSum[0] / ANTI_ALIASING_FACTOR, colorSum[1] / ANTI_ALIASING_FACTOR, colorSum[2] / ANTI_ALIASING_FACTOR};
                cloned.setPixel(i, j, new Pixel(new Color(colorAverage[0], colorAverage[1], colorAverage[2])));
            }
        }
        return cloned;
    }

    /**
     * Takes in a given RayHit intersection and a collection of lights and returns a
     * pixel with the proper lighted color
     *
     * @param closest
     * @param lights
     * @return
     */
    private Pixel lightPixel(Optional<RayHit> optClosest, List<Light3D> lights) {
        // Color the pixel depending on which object was hit
        if (optClosest.isPresent()) {
            RayHit closest = optClosest.get();
            Point3D point = closest.getPoint();
            Point3D normal = closest.getObj().getNormal(point);
            double lightingSum = 0.0;
            for (Light3D l : lights) {
                lightingSum += closest.getObj().getOutsideMaterial().diffuse(l, normal);
            }
            double lightingVal = lightingSum;
            Pixel lighted = new Pixel(closest.getObj().getOutsideMaterial().getColor()).scale(lightingVal);
            // Add each pixel value to our running sum for ANIT_Aliasing to average after
            return lighted;
        } else {
            return new Pixel(Color.BLACK);
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
