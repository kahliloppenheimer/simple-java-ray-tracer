package edu.brandeis.cs.cosi155b.scene;

import java.awt.*;

/**
 * Represents the material of a given shape, including its color and how shiny it is
 *
 * Created by kahliloppenheimer on 9/2/15.
 */
public class Material {

    private final Color color;
    private final int shininess;

    public Material(Color color, int shininess) {
        this.color = color;
        this.shininess = shininess;
    }

    /**
     * TODO: Implement specular light
     *
     * @param lightVec
     * @param normal
     * @param eyeVec
     * @return
     */
    public double specular(Point3D lightVec, Point3D normal, Point3D eyeVec) {
        return 0.0;
    }

    /**
     * Returns the diffuse lighting value given a vector from the point on the object
     * to the light source, and the normal vector to that point.
     *
     * @param lightVec vector from point on object to light source
     * @param normal normal vector to point on object
     * @return
     */
    public double diffuse(Light3D light, Point3D normal) {
        Point3D lightVec = light.getLocation();
        if(lightVec.length() != 1) {
            lightVec = lightVec.normalize();
        }

        if(normal.length() != 1) {
            normal = normal.normalize();
        }

        double dotProd = lightVec.dot(normal);
        return light.getIntensity() * (dotProd > 0 ? dotProd : 0);
    }

    public Color getColor() {
        return color;
    }

    public int getShininess() {
        return shininess;
    }
}
