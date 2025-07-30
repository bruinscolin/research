// Points have two sets of coordinates
// One set for algo logic
// One set for the visualization

public class Point {
    private double x;
    private double y;
    private double drx;
    private double dry;
    private String label;
    private String color;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;

        this.drx = x + 700; // both 500 and 700 values must change if
        this.dry = 500 - y; // width or height of drawing scene changes

        this.label = "";
        this.color = "black";

    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;

        this.drx = x + 700; // both 500 and 700 values must change if
        this.dry = 500 - y; // width or height of drawing scene changes
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getDrX() {
        return this.drx;

    }

    public double getDrY() {
        return this.dry;
    }

    public boolean equals(Point p2) {
        if (this.x == p2.getX() && this.y == p2.getY()) {
            return true;

        }
        return false;
    }

    public void print() {
        System.out.println("X: " + this.x + " Y: " + this.y);

    }

    public void setLabel(String s) {
        this.label = s;

    }

    public String getLabel() {
        return this.label;
    }

    public void setColor(String s) {
        this.color = s;
    }

    public String getColor() {
        return this.color;
    }

}
