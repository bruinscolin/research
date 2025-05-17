import javax.swing.*;

public class Setup {
    public static  void main(Point target) {
        // Example input values

        int w = 1400;
        int h = 1000;
        Point t = new Point(700, 500);
        Segment[] obstacles = { 
        new Segment(new Point(600, 600), new Point(700, 700)),
        new Segment(new Point(100, 100), new Point(240, 610)),
        new Segment(new Point(639, 367), new Point(673, 749))};

        JFrame f = new JFrame();
        DrawingCanvas dc = new DrawingCanvas(w, h, t, obstacles);
        f.setSize(w, h);
        f.setTitle("Visualization");
        f.add(dc);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}
