package me.kahlil.scene;

import com.google.common.collect.ImmutableList;
import me.kahlil.config.JavaStyle;
import me.kahlil.geometry.Shape;
import me.kahlil.graphics.Color;
import org.immutables.value.Value.Immutable;

/** Represents all of hte lights and objects present in a scene to render. */
@Immutable
@JavaStyle
public interface Scene {

  // List of all objects in the scene
  ImmutableList<Shape> getShapes();

  // List of all lights in the scene
  ImmutableList<PointLight> getLights();

  // Background color of the scene
  Color getBackgroundColor();

  // Ambient lighting of the scene
  Color getAmbient();
}
