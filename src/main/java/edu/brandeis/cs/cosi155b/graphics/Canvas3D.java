package edu.brandeis.cs.cosi155b.graphics;

/**
 a canvas is any Java object that supports these methods
 one must be able to compute the height and width of the canvas
 and to draw a pixel of a given color on the canvas.
 Drawing sets the color on the backbuffer and it doesn't show up
 on the screen until you call the refresh method.
**/
 
public interface Canvas3D {
    public int getHeight();
    public int getWidth();
    public void drawPixel(int i, int j, java.awt.Color c);
    public void refresh();
}
