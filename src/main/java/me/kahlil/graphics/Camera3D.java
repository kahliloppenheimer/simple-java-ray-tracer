package me.kahlil.graphics;

import me.kahlil.scene.Vector;
import org.immutables.value.Value.Immutable;

/** A representation of a camera positioned at a certain point in the scene. */
@Immutable
@JavaStyle
interface Camera3D {

    Vector getLocation();

}
