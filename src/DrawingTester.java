import javax.swing.*;


public class DrawingTester {

    public static void main(String[] args) {
        int w = 1400;
        int h = 1000;
        JFrame f = new JFrame();
        DrawingCanvas dc = new DrawingCanvas(w, h);
        f.setSize(w, h);
        f.setTitle("drawing in java");
        f.add(dc);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

}
