package edu.brandeis.cs.cosi155b.graphics;

import java.awt.*;

/**
 * Represents a single pixel in a frame
 *
 * Created by kahliloppenheimer on 9/2/15.
 */
public class Pixel {
    private final Color color;

    public Pixel(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public Pixel scale(double lightingVal) {
        if(lightingVal > 1.000001 || lightingVal < -.000001) {
            throw new IllegalArgumentException("Lighting val must be between 0.0 and 1.0");
        }
        float[] newComponents = new float[3];
        newComponents = color.getColorComponents(newComponents);
        for(int i = 0; i < newComponents.length; ++i) {
            newComponents[i] = (float) (newComponents[i] * lightingVal);
        }
        return new Pixel(new Color(newComponents[0], newComponents[1], newComponents[2]));
    }
}
