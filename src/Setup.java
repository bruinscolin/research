import javax.swing.*;

public class Setup {
    public static DrawingCanvas main(Point target, Segment[] o) {
        // Example input values

        int w = 1400;
        int h = 1400;
        Point t = target;

        Segment[] obstacles = o;

        JFrame f = new JFrame();
        DrawingCanvas dc = new DrawingCanvas(w, h, t, obstacles);

        f.setSize(w, h);
        f.setTitle("Visualization");
        f.add(dc);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
        return dc;
    }
}
