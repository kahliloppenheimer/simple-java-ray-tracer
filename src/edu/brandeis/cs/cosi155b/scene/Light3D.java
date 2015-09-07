package edu.brandeis.cs.cosi155b.scene;

import edu.brandeis.cs.cosi155b.graphics.Pixel;

import java.awt.*;

/**
 * Represents a simple light source with a location and an intensity
 *
 * Created by kahliloppenheimer on 9/2/15.
 */
public class Light3D {

    private Point3D location;
    private final Color color;

    public Light3D(Point3D location, Color color) {
        this.location = location;
        this.color = color;
    }

    /**
     * Returns the diffuse lighting value given a vector from the point on the object
     * to the light source and the normal vector to that point from the object.
     *
     * @param normal normal vector to point on object
     * @return
     */
    public double diffuse(Point3D normal) {
        Point3D lightVec = getLocation();
        if(lightVec.length() != 1) {
            lightVec = lightVec.normalize();
        }

        if(normal.length() != 1) {
            normal = normal.normalize();
        }

        return Math.max(lightVec.dot(normal), 0);
    }

    /**
     * Returns the specular light from a point with the given normal vector, vector from the viewing
     * point to the point, vector from the light to the point, and hardness value (small int)
     *
     * @param lightVec
     * @param normal
     * @param eyeVec
     * @param hardness
     * @return
     */
    public double specular(Point3D lightVec, Point3D normal, Point3D eyeVec, Material m) {
        Point3D reflected = lightVec.subtract(normal.scale(2).scale(normal.dot(lightVec)));
        Point3D v = normal.scale(2).scale(normal.scale(-1).dot(lightVec));
        return Math.max(m.getSpecularIntensity() * Math.pow(reflected.dot(v), m.getShininess()), 0);
    }

    public Point3D getLocation() {
        return location;
    }

    public void setLocation(Point3D newLoc) {
        this.location = newLoc;
    }

    public Color getColor() {
        return color;
    }

    /**
     * Returns the new color of a pixel given the color of the pixel that this light
     * hits and the diffuseCoefficient of that collision.
     *
     * @param pixelColor
     * @param diffuseCoefficient
     * @param ambient Ambient light for the entire scene
     * @return
     */
    public Color lightPixel(Color prevPixelColor, Color materialColor, double diffuseCoefficient, double specularCoefficient, Color ambient) {
        float[] materialRgb = new float[3];
        float[] lightRgb = new float[3];
        float[] ambientRgb = new float[3];
        float[] pixelRgb = new float[3];
        materialColor.getColorComponents(materialRgb);
        getColor().getColorComponents(lightRgb);
        ambient.getColorComponents(ambientRgb);
        prevPixelColor.getColorComponents(pixelRgb);
        for(int i = 0; i < 3; ++i) {
            // Add ambient if it hasn't already been accounted for
            if(pixelRgb[i] < .000001) {
                pixelRgb[i] += ambientRgb[i];
            }
            pixelRgb[i] = (float) Math.min(pixelRgb[i] + (materialRgb[i] * lightRgb[i] * diffuseCoefficient)
                    + (materialRgb[i] * lightRgb[i] * specularCoefficient), .999999999);
        }
        return new Color(pixelRgb[0], pixelRgb[1], pixelRgb[2]);
    }
}
