package edu.brandeis.cs.cosi155b.scene;

/**
 * Represents the material of a given shape, including its color and how shiny it is
 *
 * Created by kahliloppenheimer on 9/2/15.
 */
public class Material {

    private final double color;
    private final int shininess;

    public Material(double color, int shininess) {
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
    public double diffuse(Point3D lightVec, Point3D normal) {
        if(lightVec.length() != 1) {
            lightVec = lightVec.normalize();
        }

        if(normal.length() != 1) {
            normal = normal.normalize();
        }

        double dotProd = lightVec.dot(normal);
        return dotProd > 0 ? dotProd : 0;
    }

    /**
     * Returns the diffuse lighting at a given point, with a specified center of the object it belongs to,
     * and a location of the light source illuminating it
     *
     * @param point point being illuminated
     * @param center center of object that point belongs to
     * @param light light source illuminating the object
     * @return
     */
    public double diffuse(Point3D point, Point3D center, Point3D light) {
        Point3D lightVec = light.subtract(point).normalize();
        Point3D normal = point.subtract(center).normalize();
        return diffuse(lightVec, normal);
    }

    public double getColor() {
        return color;
    }

    public int getShininess() {
        return shininess;
    }
}
