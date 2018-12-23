package me.kahlil.scene;

import me.kahlil.graphics.Color;

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

    private Vector location;
    private final Color color;

    public Light3D(Vector location, Color color) {
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
        Vector point = rh.getPoint();
        Vector normal = rh.getNormal();
        Vector lightVec = getLocation().subtract(point);

        if(lightVec.magnitude() != 1) {
            lightVec = lightVec.normalize();
        }

        if(normal.magnitude() != 1) {
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
//    public double specular(Vector lightPos, Vector eyePos, RayHit rh) {
//        Vector eyeVec = eyePos.subtract(rh.getPoint()).normalize();
//        Vector incomingLight = rh.getPoint().subtract(lightPos);
//        Vector normal = rh.getObj().getNormal(rh.getRay());
//        Vector reflected = incomingLight.subtract(normal.scale(2).scale(normal.dot(incomingLight))).normalize();
//        int hardness = rh.getObj().getOutsideMaterial().getHardness();
//        return  Math.max(Math.pow(reflected.dot(eyeVec), hardness), 0);
//    }

    public double specular(Vector eyePos, RayHit rh) {
        Vector lightVec = getLocation().subtract(rh.getPoint()).normalize();
        Vector eyeVec = eyePos.subtract(rh.getPoint()).normalize();
        Vector normal = rh.getNormal();
        Vector lProjectedOntoN = normal.scale(lightVec.dot(normal));
        Vector lProjectedOntoPlane = lightVec.subtract(lProjectedOntoN);
        Vector reflectedLight = lightVec.subtract(lProjectedOntoPlane.scale(2)).normalize();
        return Math.pow(Math.max(reflectedLight.dot(eyeVec), 0), rh.getObj().getOutsideMaterial().getHardness());
    }

    public Vector getLocation() {
        return location;
    }

    public void setLocation(Vector newLoc) {
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
    public Color phongIllumination(RayHit rh, Vector cameraPos) {
        double diffuseCoefficient = diffuse(rh);
        double specularCoefficient = specular(cameraPos, rh);
        if(specularCoefficient * SPECULAR_INTENSITY > diffuseCoefficient) {
            diffuseCoefficient = 1 - SPECULAR_INTENSITY * specularCoefficient;
        }
        Material m = rh.getObj().getOutsideMaterial();
        return m.getColor().multiply(getColor()).scaleFloat((float) diffuseCoefficient)
                             .add( getColor().scaleFloat((float) specularCoefficient).scaleFloat((float) m.getSpecularIntensity())); }
}
