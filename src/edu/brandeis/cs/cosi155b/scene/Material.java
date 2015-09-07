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
    private final double specularIntensity;

    public Material(Color color, int shininess, double specularIntensity) {
        this.color = color;
        this.shininess = shininess;
        this.specularIntensity = specularIntensity;
    }

    public Color getColor() {
        return color;
    }

    public int getShininess() {
        return shininess;
    }

    public double getSpecularIntensity() {
        return specularIntensity;
    }
}
