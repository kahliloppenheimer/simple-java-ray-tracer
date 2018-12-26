package me.kahlil.geometry;

import static me.kahlil.graphics.Color.WHITE;

import me.kahlil.scene.ImmutableMaterial;
import me.kahlil.scene.Light3D;

/**
 * A sphere which represents a light source in a scene (i.e. if scene in a reflection).
 */
public class LightSphere extends Sphere3D {

  public LightSphere(Light3D light) {
    super(light.getLocation(),
        0.1,
        ImmutableMaterial.builder()
            .setColor(WHITE)
            .setSpecularIntensity(1.0)
            .setHardness(240)
            .build());
  }

}
