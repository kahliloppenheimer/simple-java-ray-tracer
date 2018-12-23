package me.kahlil.scene;

import me.kahlil.graphics.Color;
import java.util.List;

/**
 * Created by edenzik on 9/3/15.
 */
public class Scene3D {
    // List of all objects in the scene
    private List<Object3D> objects;
    // List of all lights in the scene
    private List<Light3D> lights;
    // Background color of the scene
    private Color backgroundColor;
    // Ambient lighting of the scene
    private Color ambient;

    public Scene3D(List<Object3D> objects, List<Light3D> lights, Color backgroundColor, Color ambient){
        this.objects = objects;
        this.lights = lights;
        this.ambient = ambient;
        this.backgroundColor = backgroundColor;
    }

    public Color getBackgroundColor() {
        return this.backgroundColor;
    }

    public Color getAmbient() {
        return this.ambient;
    }

    public List<Object3D> getObjects() {
        return objects;
    }

    public List<Light3D> getLights() {
        return lights;
    }
}
