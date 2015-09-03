package edu.brandeis.cs.cosi155b.scene;

/**
 * Represents a simple light source with a location and an intensity
 *
 * Created by kahliloppenheimer on 9/2/15.
 */
public class Light3D {

    private Point3D location;
    private final double intensity;

    public Light3D(Point3D location, double intensity) {
        if(intensity < -.00001 || intensity > 1.000001) {
            throw new IllegalArgumentException("Lighting intensity must be between 0.0 and 1.0");
        }
        this.location = location;
        this.intensity = intensity;
    }

    public Point3D getLocation() {
        return location;
    }

    public void setLocation(Point3D p) {
        this.location = p;
    }

    public double getIntensity() {
        return intensity;
    }
}
