package me.kahlil.geometry;

import me.kahlil.graphics.Color;
import me.kahlil.scene.ImmutableMaterial;
import me.kahlil.scene.Light3D;

/** A sphere which represents a light source in a scene (i.e. if scene in a reflection). */
public class LightSphere extends Sphere3D {

  public LightSphere(Light3D light) {
    super(
        light.getLocation(),
        0.5,
        ImmutableMaterial.builder()
            .setColor(new Color(1.0f, 1.0f, 1.0f))
            .setSpecularIntensity(1.0)
            .setHardness(240)
            .build());
  }
}
