package edu.brandeis.cs.cosi155b.graphics;

import edu.brandeis.cs.cosi155b.scene.Point3D;

/**
 * Created by kahliloppenheimer on 9/2/15.
 */
public class Camera3D {

    private final Point3D location;

    public Camera3D(Point3D location) {
        this.location = location;
    }

    public Point3D getLocation() {
        return location;
    }

}
