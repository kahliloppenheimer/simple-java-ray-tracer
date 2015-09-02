package edu.brandeis.cs.cosi155b.shapes;

/**
 * Represents a simple light source with a location and an intensity
 *
 * Created by kahliloppenheimer on 9/2/15.
 */
public class Light3D {

    private final Point3D location;
    private final double intensity;

    public Light3D(Point3D location, double intensity) {
        this.location = location;
        this.intensity = intensity;
    }

    public Point3D getLocation() {
        return location;
    }

    public double getIntensity() {
        return intensity;
    }
}
