package edu.brandeis.cs.cosi155b.scene;

import edu.brandeis.cs.cosi155b.graphics.Color;

/**
 * Represents a simple light source with a location and an intensity
 *
 * Created by kahliloppenheimer on 9/2/15.
 */
public class Light3D {

    // Percentage brightness that specular lighting should get. Lower numbers
    // yield a "duller" effect, while higher numbers yield a more artificial
    // "plastic-y" effect
    private static final double SPECULAR_INTENSITY = .25;

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
     *
     * @param point
     * @param normal normal vector to point on object
     * @return
     */
    public double diffuse(RayHit rh) {
        Point3D point = rh.getPoint();
        Point3D normal = rh.getObj().getNormal(rh.getRay());
        Point3D lightVec = getLocation().subtract(point);

        if(lightVec.length() != 1) {
            lightVec = lightVec.normalize();
        }

        if(normal.length() != 1) {
            normal = normal.normalize();
        }

        return Math.max(lightVec.dot(normal), 0);
    }

    /**
     * Returns the specular light at a given RayHit with the given light and eye positions
     *
     * @param lightPos
     * @param eyePos
     * @param rh
     * @return
     */
//    public double specular(Point3D lightPos, Point3D eyePos, RayHit rh) {
//        Point3D eyeVec = eyePos.subtract(rh.getPoint()).normalize();
//        Point3D incomingLight = rh.getPoint().subtract(lightPos);
//        Point3D normal = rh.getObj().getNormal(rh.getRay());
//        Point3D reflected = incomingLight.subtract(normal.scale(2).scale(normal.dot(incomingLight))).normalize();
//        int hardness = rh.getObj().getOutsideMaterial().getHardness();
//        return  Math.max(Math.pow(reflected.dot(eyeVec), hardness), 0);
//    }

    public double specular(Point3D lightPos, Point3D eyePos, RayHit rh) {
        Point3D lightVec = lightPos.subtract(rh.getPoint()).normalize();
        Point3D eyeVec = eyePos.subtract(rh.getPoint()).normalize();
        Point3D normal = rh.getObj().getNormal(rh.getRay());
        Point3D lProjectedOntoN = normal.scale(lightVec.dot(normal));
        Point3D lProjectedOntoPlane = lightVec.subtract(lProjectedOntoN);
        Point3D reflectedLight = lightVec.subtract(lProjectedOntoPlane.scale(2)).normalize();
        return Math.pow(Math.max(reflectedLight.dot(eyeVec), 0), rh.getObj().getOutsideMaterial().getHardness());
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
     * @param ambient Ambient light for the entire scene
     * @param diffuseCoefficient
     * @return
     */
    public Color phongIllumination(Color prevPixelColor, Color materialColor, double diffuseCoefficient, double specularCoefficient) {
        if(specularCoefficient * SPECULAR_INTENSITY > diffuseCoefficient) {
            diffuseCoefficient = 1 - SPECULAR_INTENSITY * specularCoefficient;
        }
//        } else {
//            specularCoefficient = Math.min(1 - diffuseCoefficient, specularCoefficient);
//        }
        return prevPixelColor.add( materialColor.multiply(getColor()).scaleFloat((float) diffuseCoefficient))
                             .add( getColor().scaleFloat((float) specularCoefficient).scaleFloat((float) SPECULAR_INTENSITY)); }
}
