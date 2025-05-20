import javax.swing.*;

public class Setup {
    public static  void main(Point target, Segment[] o) {
        // Example input values

        int w = 1400;
        int h = 1000;
        Point t = new Point(700, 500);
        Segment[] obstacles = o;

        JFrame f = new JFrame();
        DrawingCanvas dc = new DrawingCanvas(w, h, t, obstacles);

        f.setSize(w, h);
        f.setTitle("Visualization");
        f.add(dc);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}
