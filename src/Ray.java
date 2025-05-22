public class Ray {
    private Point origin;
    private double angle;

    public Ray(Point origin, double angle) {
        this.origin = origin;
        this.angle = angle;
    }

    public void set(Point origin, double angle) {
        this.origin = origin;
        this.angle = angle;
    }

    public Point getOrigin() {
        return origin;
    }

    public double getAngle() {
        return angle;
    }

    public double getX() {
        return origin.getX();
    }

    public double getY() {
        return origin.getY();
    } 

    public boolean intersectsLines(Segment[] s){

        return true;
    }

}
