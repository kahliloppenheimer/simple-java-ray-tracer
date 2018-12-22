package edu.brandeis.cs.cosi155b.graphics;

import com.google.common.base.Preconditions;
import edu.brandeis.cs.cosi155b.scene.Light3D;
import edu.brandeis.cs.cosi155b.scene.Material;
import edu.brandeis.cs.cosi155b.scene.Object3D;
import edu.brandeis.cs.cosi155b.scene.Ray3D;
import edu.brandeis.cs.cosi155b.scene.RayHit;
import edu.brandeis.cs.cosi155b.scene.Scene3D;
import edu.brandeis.cs.cosi155b.scene.Vector;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RayTracerWorker implements Runnable {

  private static final float ADAPTIVE_ANTI_ALIAS_STDEV_THRESOLD = .05f;

  private final SimpleFrame3D frame;
  private final Camera3D camera;
  private Scene3D scene;
  private final boolean shadowsEnabled;
  private final int startingPixel;
  private final int pixelIncrement;
  private final int maxAntiAliasSamples;
  private final double widthDelta;
  private final double heightDelta;

  RayTracerWorker(
      SimpleFrame3D frame,
      Camera3D camera,
      Scene3D scene,
      boolean shadowsEnabled,
      int startingPixel,
      int pixelIncrement,
      int antiAliasSamples
  ) {
    this.frame = frame;
    this.camera = camera;
    this.scene = scene;
    this.shadowsEnabled = shadowsEnabled;
    this.startingPixel = startingPixel;
    this.pixelIncrement = pixelIncrement;
    this.maxAntiAliasSamples = antiAliasSamples;
    // Increments in coordinate space between actual pixels
    this.widthDelta = frame.getWidth() / frame.getWidthPx();
    this.heightDelta = frame.getHeight() / frame.getHeightPx();
  }

  @Override
  public void run() {
    for (int i = 0; i < frame.getWidthPx(); ++i) {
      for (int j = startingPixel; j < frame.getHeightPx(); j += pixelIncrement) {
        float[] runningColorSum = new float[3];
        float[] nextColorToAdd = new float[3];
        List<float[]> samples = new ArrayList<>();
        int numAntiAliasSamples = 2;
        for (int k = 0; k < numAntiAliasSamples; ++k) {
          // Create the ray from the camera to the pixel in the frame we are currently coloring
          double randWDelta = Math.random() * widthDelta;
          double randHDelta = Math.random() * heightDelta;
          Ray3D nextRay = new Ray3D(camera.getLocation(), frame.getBottomLeftCorner()
              .translate(i * widthDelta - randWDelta, j * heightDelta - randHDelta, 0)
              .subtract(camera.getLocation()));

          Color traced = traceRay(nextRay, shadowsEnabled);

          traced.getColorComponents(nextColorToAdd);

          samples.add(nextColorToAdd);

          for (int q = 0; q < 3; ++q) {
            runningColorSum[q] += nextColorToAdd[q];
          }

          if (!samplesAreVerySimilar(samples) && numAntiAliasSamples < maxAntiAliasSamples) {
            numAntiAliasSamples = Math.min(numAntiAliasSamples * 2, maxAntiAliasSamples);
          }
        }
        float[] colorAverage = {
            runningColorSum[0] / numAntiAliasSamples,
            runningColorSum[1] / numAntiAliasSamples,
            runningColorSum[2] / numAntiAliasSamples};
        frame.setPixel(i, j, new Color(colorAverage[0], colorAverage[1], colorAverage[2]));
      }
    }
    System.out.println("Thread " + startingPixel + " finished!");
  }

  private static boolean samplesAreVerySimilar(List<float[]> colorSamples) {
    // Always count at least 3 samples.
    if (colorSamples.size() < 3) {
      return false;
    }
    float[] colorStdevs = computeStdev(colorSamples);
    // If any one RGB component stdev is above the threshold, return false
    for (int i = 0; i < colorStdevs.length; i++) {
      if (colorStdevs[i] > ADAPTIVE_ANTI_ALIAS_STDEV_THRESOLD) {
        return false;
      }
    }
    return true;
  }

  private static float[] computeStdev(List<float[]> samples) {
    float[] averages = computeAverage(samples);
    float[] averageSquareDistancesFromMean = new float[samples.get(0).length];
    for (int i = 0; i < samples.size(); i++) {
      float[] nextSample = samples.get(i);
      for (int j = 0; j < nextSample.length; j++) {
        float distanceFromMean = nextSample[j] - averages[j];
        averageSquareDistancesFromMean[j] = (distanceFromMean * distanceFromMean) / samples.size();
      }
    }
    return computeSqrt(averageSquareDistancesFromMean);
  }

  private static float[] computeSqrt(float[] f) {
    Preconditions.checkArgument(f != null && f.length > 0,
        "f must be non-null and non-empty.");
    for (int i = 0; i < f.length; i++) {
      f[i] = (float) Math.sqrt(f[i]);
    }
    return f;
  }

  private static float[] computeAverage(List<float[]> samples) {
    Preconditions.checkArgument(samples != null && !samples.isEmpty(),
        "Samples must be non-null and non-empty.");
    float[] averageValues = new float[samples.get(0).length];
    for (int i = 0; i < samples.size(); i++) {
      for (int j = 0; j < samples.get(i).length; j++) {
        averageValues[j] += samples.get(i)[j] / samples.size();
      }
    }
    return averageValues;
  }

  /**
   * Returns the passed ray into the scene and returns what color the pixel that the ray
   * goes through should be
   *
   * @param nextRay
   * @param shadowsEnabled
   * @return
   */
  private Color traceRay(Ray3D nextRay, boolean shadowsEnabled) {
    Optional<RayHit> optClosest = findFirstIntersection(nextRay, scene);
    // Color the pixel depending on which object was hit
    if (optClosest.isPresent()) {
      RayHit closest = optClosest.get();
      Material material = closest.getObj().getOutsideMaterial();
      // Initialize color with ambient light
      Color lighted = scene.getAmbient().multiply(material.getColor());
      for (Light3D l : scene.getLights()) {
        // Check to see if shadow should be cast
        if(!shadowsEnabled || !isObjectBetweenLightAndPoint(l, closest.getPoint())) {
          lighted = lighted.add(l.phongIllumination(closest, camera.getLocation()));
        }
      }
      return lighted;
    } else {
      return scene.getBackgroundColor();
    }
  }

  /**
   * Returns true iff there is an object in the scene between the light and the given
   * point
   *
   * @param l
   * @param point
   * @return
   */
  private boolean isObjectBetweenLightAndPoint(Light3D l, Vector point) {
    Vector shadowVec = l.getLocation().subtract(point);
    Optional<RayHit> closestHit = findFirstIntersection(new Ray3D(point.add(shadowVec.scale(.0001)), shadowVec), scene);
    return closestHit.isPresent() && closestHit.get().getDistance() < shadowVec.magnitude();
  }

  /**
   * Returns the RayHit with the lowest distance from the visionVec to each obj in the scene.
   * Returns optional.empty() if no object is hit.
   *
   * @param visionVec
   * @param scene
   * @return
   */
  private Optional<RayHit> findFirstIntersection(Ray3D visionVec, Scene3D scene) {
    RayHit closest = null;
    double closestDistance = Double.POSITIVE_INFINITY;
    for (Object3D o : scene.getObjects()) {
      Optional<RayHit> intersection = o.findIntersection(visionVec);
      if (intersection.isPresent() && intersection.get().getDistance() < closestDistance) {
        closestDistance = intersection.get().getDistance();
        closest = intersection.get();
      }
    }
    return Optional.ofNullable(closest);
  }
}
