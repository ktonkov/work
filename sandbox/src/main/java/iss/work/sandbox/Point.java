package iss.work.sandbox;

public class Point {
    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int X() {
        return x;
    }

    public int Y() {
        return y;
    }

    public double distance(Point p) {
        return Math.sqrt(((x - p.X()) * (x - p.X())) + ((y - p.Y()) * (y - p.Y())));
    }
}
