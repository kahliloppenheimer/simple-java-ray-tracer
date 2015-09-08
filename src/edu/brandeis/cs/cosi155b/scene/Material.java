package edu.brandeis.cs.cosi155b.scene;

import edu.brandeis.cs.cosi155b.graphics.Color;

/**
 * Represents the material of a given shape, including its color and how shiny it is
 *
 * Created by kahliloppenheimer on 9/2/15.
 */
public class Material {

    private final Color color;
    private final int hardness;

    public Material(Color color, int hardness) {
        if(hardness < 1 || hardness > 511) {
            throw new IllegalArgumentException("Hardness must be an integer between 1 and 511");
        }
        this.color = color;
        this.hardness = hardness;
    }

    public Color getColor() {
        return color;
    }

    public int getHardness() {
        return hardness;
    }

}
