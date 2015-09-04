package edu.brandeis.cs.cosi155b.scene;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by edenzik on 9/3/15.
 */
public class Scene3D extends ArrayList<Object3D>{

    public Scene3D(Object3D... objects){
        super(Arrays.asList(objects));
    }
}
