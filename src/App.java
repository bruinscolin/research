
public class App {
    public static void main(String[] args) throws Exception {
        Setup setup = new Setup();
        Point target = new Point(1, 2);
        setup.main(500, target);
        Line line = new Line(new Point(1, 2), new Point(3, 4));
        System.out.println("Hello, World!");
    }
}
