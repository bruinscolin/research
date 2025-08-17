public class PolarSegment{
    private PolarPoint p1;
    private PolarPoint p2;

    public PolarSegment(PolarPoint p1, PolarPoint p2) {
        this.p1 = p1;
        this.p2 = p2;



    }

    public void set(PolarPoint p1, PolarPoint p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    // get endPolarPoints
    public PolarPoint getP1() {
        return p1;
    }

    public PolarPoint getP2() {
        return p2;
    }


    public void setP1(PolarPoint p){
        this.p1 = p;
    }

    public void setP2(PolarPoint p){
        this.p2 = p;

    }

    // get coordinates of endPolarPoints
    public double getR1() {
        return p1.getR();
    }

    public double getR2() {
        return p2.getR();
    }

    public double getAngle1() {
        return p1.getAngle();
    }

    public double getAngle2() {
        return p2.getAngle();
    }


    // convert to cartesian
    public Segment toSegment(Point origin){
        double x1 = origin.getX() + p1.getR() * Math.cos(p1.getAngle());
        double y1 = origin.getY() + p1.getR() * Math.sin(p1.getAngle());

        // convert second polar point
        double x2 = origin.getX() + p2.getR() * Math.cos(p2.getAngle());
        double y2 = origin.getY() + p2.getR() * Math.sin(p2.getAngle());

        // create cartesian points
        Point c1 = new Point(x1, y1);
        Point c2 = new Point(x2, y2);

        // return as a cartesian Segment
        return new Segment(c1, c2);
    } 
}


