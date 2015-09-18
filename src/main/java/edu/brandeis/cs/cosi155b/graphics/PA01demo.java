package edu.brandeis.cs.cosi155b.graphics;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;

import java.awt.Color;


/**
 * This class tests out the methods in PA01
 * @author tim
 *
 */
public class PA01demo {
	
    private static MyCanvas3D mc = new MyCanvas3D(400,500);
    
    /**
     * this creates a window to demo the edu.brandeis.cs.cosi155b.Canvas3D object
     * @param args
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws InterruptedException {
    	/*
    	 * This is the preferred way to create a GUI.
    	 * It avoid thread problems by creating the GUI 
    	 * in the EventDispatch thread.
    	 */
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(); 
            }
        });
        
		System.out.println("sleeping!");
        Thread.sleep(2000L); // sleep for 2 seconds

        mc.drawStuff(); //draw on the offscreen canvas
		
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {mc.refresh();} // copy the offscreen canvas to the screen!
        });
        
        Thread.sleep(2500L); // sleep for 2.5 seconds

		drawCircle(200,200,50); // draw on the offscreen canvas
    	
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {mc.refresh();} // copy the offscreen canvas to the screen
        });

        System.out.println("drew a circle!");
    }

    /*
     * here we create a window, add the canvas,
     * set the window size and make it visible!
     */
    private static void createAndShowGUI() {
        
        JFrame f = new JFrame("PA01 Demo");
        
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        f.add(mc);
        f.setSize(500,500);
        f.setVisible(true);
    } 
	
	private static void drawCircle(int x0, int y0, int r){
    	for(int i=-r;i<=r;i++){
    		int x = x0 + i;
    		int y = y0 + (int)Math.sqrt(r*r-i*i);
    		mc.drawPixel(x,y,Color.BLUE);
    	}
    	for(int i=0;i<180;i++){
			double s = degToRad(i);
    		int x = (int) (x0 + r*Math.cos(s));
    		int y = (int) (y0 - r*Math.sin(s));
    		mc.drawPixel(x, y, Color.YELLOW);
    	}
	}
	
	private static double degToRad(double d){
		return (d/360)*2*Math.PI;
	}

}

