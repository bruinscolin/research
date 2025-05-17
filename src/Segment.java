public class Segment{
    private Point p1;
    private Point p2;

    public Segment(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public void set(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    // get endpoints
    public Point getP1() {
        return p1;
    }

    public Point getP2() {
        return p2;
    }

    // get coordinates of endpoints
    public double getX1() {
        return p1.getX();
    }

    public double getY1() {
        return p1.getY();
    }

    public double getX2() {
        return p2.getX();
    }

    public double getY2() {
        return p2.getY();
    }

}

