package edu.brandeis.cs.cosi155b.graphics;

import edu.brandeis.cs.cosi155b.scene.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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
                // Create the ray from the camera to the pixel in the frame we are currently coloring
                Ray3D visionVec = new Ray3D(camera.getLocation(), cloned.getBottomLeftCorner()
                        .translate(i * wDelta, j * hDelta, 0)
                        .subtract(camera.getLocation()));
                // Find the closest object that this ray intersects
                RayHit closest = null;
                double closestDistance = Double.POSITIVE_INFINITY;
                for (Object3D o : scene) {
                    RayHit intersection = o.rayIntersect(visionVec);
                    if (intersection.getDistance() < closestDistance) {
                        closestDistance = intersection.getDistance();
                        closest = intersection;
                    }
                }
                // Color the pixel depending on which object was hit
                if (closest != null) {
                    Point3D point = closest.getPoint();
                    Point3D normal = closest.getObj().getNormal(point);
                    double lightingSum = 0.0;
                    for(Light3D l : lights) {
                        lightingSum += closest.getObj().getOutsideMaterial().diffuse(l, normal);
                    }
                    double lightingVal = lightingSum;
                    Pixel lighted = new Pixel(closest.getObj().getOutsideMaterial().getColor()).scale(lightingVal);
                    cloned.setPixel(i, j, lighted);
                } else {
                    cloned.setPixel(i, j, new Pixel(Color.BLACK));
                }
            }
        }
        return cloned;
    }
}
