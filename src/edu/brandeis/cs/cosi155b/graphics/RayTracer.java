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
    private List<Object3D> objects;
    private List<Light3D> lights;

    public static void main(String[] args) throws InterruptedException {
        SimpleFrame3D frame = new SimpleFrame3D(new Point3D(-1, -1, -1), 2, 2, 400, 400);
        Camera3D camera = new Camera3D(new Point3D(0, 0, 0), new Point3D(0, 0, -1));
        List<Object3D> objects = new ArrayList<>();
        objects.add(new Sphere3D(new Point3D(0, 0, -2), 1, null, null));
        RayTracer rt = new RayTracer(frame, camera, objects, null);
        SimpleFrame3D rendered = rt.render();
        display(rendered);
    }

    private static void display(SimpleFrame3D rendered) throws InterruptedException {

        System.out.println("WidthPx = " + rendered.getWidthPx() + "\theight = " + rendered.getHeightPx());
        MyCanvas3D canvas = new MyCanvas3D(rendered.getWidthPx(), rendered.getHeightPx());
        System.out.println("Width = " + canvas.getWidth() + "\theight = " + canvas.getHeight());


        SwingUtilities.invokeLater(() -> createAndShowGUI(canvas));

        System.out.println("Sleeping!");
        Thread.sleep(2000);

        for(int i = 0; i < rendered.getWidthPx(); ++i) {
            for(int j = 0; j < rendered.getHeightPx(); ++j) {
                canvas.drawPixel(i, j, rendered.getPixel(i, j).getColor());
            }
        }

        SwingUtilities.invokeLater( () -> canvas.refresh() );
    }

    /*
     * here we create a window, add the canvas,
     * set the window size and make it visible!
     */
    private static void createAndShowGUI(MyCanvas3D canvas) {

        JFrame f = new JFrame("PA01 Demo");

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(canvas);
        System.out.println("Width = " + canvas.getWidth() + "\theight = " + canvas.getHeight());
        f.setSize(400, 400);
        f.setVisible(true);
    }

    public RayTracer(SimpleFrame3D frame, Camera3D camera, List<Object3D> objects, List<Light3D> lights) {
        this.frame = frame;
        this.camera = camera;
        this.objects = objects;
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
                for (Object3D o : objects) {
                    RayHit intersection = o.rayIntersect(visionVec);
                    if (intersection.getDistance() < closestDistance) {
                        closestDistance = intersection.getDistance();
                        closest = intersection;
                    }
                }
                // Color the pixel depending on which object was hit
                if (closest != null) {
                    cloned.setPixel(i, j, new Pixel(Color.RED));
                } else {
                    cloned.setPixel(i, j, new Pixel(Color.BLUE));
                }
            }
        }
        return cloned;
    }
}
