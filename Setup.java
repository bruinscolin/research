public class Setup {
    public static void main(int b, Point t) {
        // Example input values
        int boundary = b;
        Point target = t;
        runSetup(boundary, target.getX(), target.getY());
    }

    public static void runSetup(int boundary, double x, double y) {
        Point target = new Point(x, y);
        System.out.println("Boundary: " + boundary);
        System.out.println("Target position: " + target.getX() + ", " + target.getY());
    }
}
