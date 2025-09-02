
public class PolarPoint {
    private double r;
    private double angle;
    private int index;

    public PolarPoint(double r, double angle) {
        this.r = r;
        this.angle = angle;
        this.index = -1;
    }

    public void set(double r, double angle) {

        this.r = r;
        this.angle = angle;
    }

    public double getR() {
        return this.r;
    }

    public double getAngle() {
        return this.angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public boolean equals(PolarPoint p2) {
        if (this.r == p2.getR() && this.angle == p2.getAngle()) {
            return true;

        }
        return false;
    }

    public void setIndex(int i) {
        this.index = i;
    }

    public int getIndex() {
        return this.index;
    }

    public void print() {
        System.out.println("R-length: " + this.r + "Angle: " + this.angle);

    }

    public Point toCartesian() {
        double x = this.r * Math.cos(this.angle);
        double y = this.r * Math.sin(this.angle);
        return new Point(x, y);
    }

}
