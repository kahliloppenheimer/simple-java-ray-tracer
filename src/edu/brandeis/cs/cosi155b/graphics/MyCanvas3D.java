package edu.brandeis.cs.cosi155b.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * this creates a JPanel with a backbuffer Image which the user can draw on.
 * When the system calls the paintComponent method to repaint the jPanel the
 * backbuffer Image is copied to the screen.
 * 
 * @author tim
 * 
 */
public class MyCanvas3D extends JPanel implements Canvas3D {

	/*
	 * buffer is the image that will be drawn on the JPanel
	 */
	private Image buffer;
	private Graphics bufferg; // this is a convenience variable holding the
								// Graphics element for the buffer

	/* the width, height, and size of the buffer */
	private int bufferwidth;
	private int bufferheight;

	/** returns the width of the image **/
	public int getWidth() {
		return bufferwidth;
	}

	/** return the height of the image **/
	public int getHeight() {
		return bufferheight;
	}

	/** draw a single pixel in the image **/
	public void drawPixel(int i, int j, java.awt.Color c) {
		bufferg.setColor(c);
		bufferg.fillRect(i, j, 1, 1);
	}

	/** draw the image onto the screen **/
	public void refresh() {

		if (buffer != null) {
			this.getGraphics().drawImage(buffer, 0, 0, this); // copy offscreen
																// buffer to
																// screen
		}
		this.invalidate();
	}

	/**
	 * create a canvas3D with a buffer of the specified size
	 * 
	 * @param w
	 * @param h
	 */
	public MyCanvas3D(int w, int h) {
		setBorder(BorderFactory.createLineBorder(Color.black));
		bufferwidth = (w > 0) ? w : 1; // require width to be positive
		bufferheight = (h > 0) ? h : 1; // require height to be positive
		buffer = this.createImage(bufferwidth, bufferheight);
	}

	/** set the preferred size of the component **/
	public Dimension getPreferredSize() {
		return new Dimension(400, 600);
	}

	/** draw the image on the screen **/
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.GREEN);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		if (buffer == null)
			buffer = this.createImage(bufferwidth, bufferheight);
		if (bufferg == null)
			bufferg = buffer.getGraphics();
		if (buffer != null) {
			g.drawImage(buffer, 0, 0, this);
		}
	}

	/** draw some text and shapes on the screen **/
	public void drawStuff() {
		paintSquare();
		bufferg.drawImage(buffer, 0, 0, this);
		bufferg.drawString("This is my PA01 demo!", 100, 200);
	}

	/* draw some other shapes on the screen */
	private void paintSquare() {
		bufferg.setColor(Color.RED);
		bufferg.fillRect(0, 0, this.getWidth(), this.getHeight());
		bufferg.setColor(Color.BLACK);
		bufferg.drawRect(25, 25, 50, 50);
		for (int i = 0; i < 100; i++) {
			drawPixel(i, 100 - i, Color.YELLOW);
		}
	}

}
