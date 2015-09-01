package edu.brandeis.cs.cosi155b;

/**   A edu.brandeis.cs.cosi155b.Point3D is a triple of doubles that can represent a point or a vector.
**/

public class Point3D 
{
    public double x=0.0, y=0.0, z=0.0;

    public Point3D(double x, double y, double z) {
	
    }

    public Point3D(Point3D m){
	 }

    public String toString() {
	return ""; }

    /**
     * return the vector obtained by subtracting q from this point
     * @param q
     * @return
     */
    public Point3D subtract(Point3D q) {
	return null;  }

    /**
     * return the point obtained by adding q to this point
     * @param q
     * @return
     */
    public Point3D add(Point3D q) {
	return null;   }

    /**
     * return the point obtained by scaling this point by a
     * @param a
     * @return
     */
    public Point3D scale(double a) {
	return null;   } 

    /**
     * return the point obtained by translating this point
     * by the specified distances
     * @param x_dist
     * @param y_dist
     * @param z_dist
     * @return
     */
    public Point3D translate(double x_dist, double y_dist, double z_dist) {
	return null; }	

    /**
     * return the dot product of this point and q
     * @param q
     * @return
     */
    public double dot(Point3D q){
	return  0; }

    /**
     * return the cross product of this point and q
     * @param q
     * @return
     */
    public Point3D cross(Point3D q){
	return null; }

    /**
     * return the normalization of this vector
     * @return
     */
    public Point3D normalize() {
	return null; 	}
	
    /**
     * return the length of this vector
     * @return
     */
    public double length() {
	return 0;	}

    public static void main(String[] args){
    	Point3D p1 = new Point3D(1,2,3);
    	Point3D p2 = new Point3D(4,5,6);
    	Point3D p3 = new Point3D(1,2,2);
    	System.out.println("the sum of "+p1+" and "+p2+" is "+p1.add(p2)+ " and should be (5,7,9)");
    	System.out.println("subtracting "+p1+" from "+p2+" gives "+ p2.subtract(p1)+" and should be (3,3,3) ");
    	System.out.println("the length of "+p3+" is "+p3.length()+" and should be 3");
    	/** add more tests below **/
    }
}

   

