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
}
