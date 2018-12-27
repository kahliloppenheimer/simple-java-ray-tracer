package me.kahlil.graphics;

/** A representation of a surface with which you can set pixel values. */
public interface Canvas3D {
  int getHeight();

  int getWidth();

  void drawPixel(int i, int j, java.awt.Color c);

  void refresh();
}
