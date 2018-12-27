package me.kahlil.scene;

import me.kahlil.config.JavaStyle;
import me.kahlil.geometry.Vector;
import org.immutables.value.Value;

/** Immutables implementation of {@link Camera}. */
@Value.Immutable
@JavaStyle
interface CameraImpl extends Camera {

  Vector getLocation();
}
