package me.kahlil.scene;

import com.google.common.collect.ImmutableList;
import me.kahlil.config.JavaStyle;
import me.kahlil.geometry.Object3D;
import me.kahlil.graphics.Color;
import org.immutables.value.Value.Immutable;

/**
 * Represents all of hte lights and objects present in a scene to render.
 */
@Immutable
@JavaStyle
public interface Scene3D {

  // List of all objects in the scene
  ImmutableList<Object3D> getObjects();

  // List of all lights in the scene
  ImmutableList<Light3D> getLights();

  // Background color of the scene
  Color getBackgroundColor();

  // Ambient lighting of the scene
  Color getAmbient();

}
