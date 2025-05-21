public class Point {
    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean equals(Point p2){
        if (this.x == p2.getX() && this.y == p2.getY()){
            return true;

        }
        return false;
    }

    public void print(){
        System.out.println("X: " + this.x + " Y: " + this.y);

    }
}
