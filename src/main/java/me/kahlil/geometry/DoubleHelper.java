package me.kahlil.geometry;

import static me.kahlil.geometry.Constants.EPSILON;

public class DoubleHelper {

  /**
   * Returns true iff d1 and d2 are basically equal (sans {@link EPSILON}).
   */
  public static boolean nearEquals(double d1, double d2) {
    return Math.abs(d2 - d1) < EPSILON;
  }

  /**
   * Returns true iff every passed double is equal to one another (sans {@link EPSILON}).
   */
  public static boolean nearEquals(double ... ds) {
    for (int i = 0; i < ds.length - 1; i++) {
      if (!nearEquals(ds[i], ds[i + 1])) {
        return false;
      }
    }
    return true;
  }

}
