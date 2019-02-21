package iss.work.sandbox;

public class MyFirstProgram {
	public static void main(String args[]) {
		System.out.println("Hello, world");

		Point p1 = new Point(1, 2);
        Point p2 = new Point(1, 4);

        System.out.println("Distance between p1 and p2 is " + Distance(p1, p2));
        System.out.println("Distance between p1 and p2 is " + p1.Distance(p2));
	}

	private static double Distance(Point p1, Point p2) {
	    return Math.sqrt(((p1.X() - p2.X()) * (p1.X() - p2.X())) + ((p1.Y() - p2.Y()) * (p1.Y() - p2.Y())));
	}
}