package me.kahlil.geometry;

import static me.kahlil.geometry.LinearTransformation.scale;
import static me.kahlil.geometry.LinearTransformation.translate;

import me.kahlil.graphics.Color;
import me.kahlil.scene.ImmutableMaterial;
import me.kahlil.scene.PointLight;

/** A sphere which represents a light source in a scene (i.e. if scene in a reflection). */
public class LightSphere extends Sphere {

  public LightSphere(PointLight light) {
    super(
        ImmutableMaterial.builder()
            .setColor(new Color(1.0f, 1.0f, 1.0f))
            .setSpecularIntensity(1.0)
            .setHardness(240)
            .build());
    transform(scale(0.5).then(translate(light.getLocation())));
  }
}
