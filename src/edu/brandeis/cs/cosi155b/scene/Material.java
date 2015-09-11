package edu.brandeis.cs.cosi155b.scene;

import edu.brandeis.cs.cosi155b.graphics.Color;

import java.util.IllegalFormatCodePointException;

/**
 * Represents the material of a given shape, including its color and how shiny it is
 *
 * Created by kahliloppenheimer on 9/2/15.
 */
public class Material {

    private final Color color;
    private final int hardness;
    private final double specularIntensity;

    public Material(Color color, int hardness, double specularIntensity) {
        if(hardness < 1 || hardness > 511) {
            throw new IllegalArgumentException("Hardness must be an integer between 1 and 511");
        } else if(specularIntensity < -.00001 || specularIntensity > 1.000001) {
            throw new IllegalArgumentException("Specular intensity must be between 0.0 and 1.0");
        }
        this.color = color;
        this.hardness = hardness;
        this.specularIntensity = specularIntensity;
    }

    public Color getColor() {
        return color;
    }

    public int getHardness() {
        return hardness;
    }

}
