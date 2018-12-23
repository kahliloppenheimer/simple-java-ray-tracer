package me.kahlil.graphics;

import me.kahlil.scene.Vector;

/**
 * Created by kahliloppenheimer on 9/2/15.
 */
public class Camera3D {

    private final Vector location;

    public Camera3D(Vector location) {
        this.location = location;
    }

    public Vector getLocation() {
        return location;
    }

}
