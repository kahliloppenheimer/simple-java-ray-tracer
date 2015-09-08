package edu.brandeis.cs.cosi155b.scene;

import edu.brandeis.cs.cosi155b.graphics.Color;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by edenzik on 9/3/15.
 */
public class Scene3D extends ArrayList<Object3D>{
    // Background color of the scene
    private Color backgroundColor;
    // Ambient lighting of the scene
    private Color ambient;

    public Scene3D(Color backgroundColor, Color ambient, Object3D... objects){
        super(Arrays.asList(objects));
        this.ambient = ambient;
        this.backgroundColor = backgroundColor;
    }

    public Color getBackgroundColor() {
        return this.backgroundColor;
    }

    public Color getAmbient() {
        return this.ambient;
    }
}
