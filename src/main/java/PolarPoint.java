
public class PolarPoint {
    private double r;
    private double angle;

    public PolarPoint(double r, double angle) {
        this.r = r;
        this.angle = angle;

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

    public void print() {
        System.out.println("R-length: " + this.r + "Angle: " + this.angle);

    }

}
