import java.awt.*; // adds the color and graphics class
import java.awt.geom.*; // adds shapes and paths
import javax.swing.*;


public class DrawingCanvas extends JComponent {

    private int width;
    private int height;

    public DrawingCanvas(int w, int h) {
        width = w;
        height = h;
    }
    
    // sets color, draws lines, curves, and shapes
    protected  void paintComponent(Graphics g) { 
        Graphics2D g2d = (Graphics2D) g; // cast to Graphics2D

        // Line2D.Double line = new Line2D.Double(0, 0, width, height); // create a line
        Rectangle2D.Double r = new Rectangle2D.Double(50, 75, 100, 250); // create a rectangle
        g2d.setColor(new Color(100, 149, 237));
        g2d.fill(r);

        // X axis
        Line2D.Double x_axis = new Line2D.Double(0, height / 2, 50, height / 2);
        g2d.setColor(Color.BLACK);
        g2d.draw(x_axis);


        String x_label = String.valueOf(height / 2);
        g2d.drawString(x_label, 50, height / 2 + 15);


        // Y axis
        Line2D.Double y_axis = new Line2D.Double(width / 2, 0, width / 2, 50);
        g2d.draw(y_axis);


        String y_label = String.valueOf(width / 2);
        g2d.drawString(y_label, width / 2 + 15, 50);


        // Circle, x and y coordniates where the rectagnluar border starts
        // X height and Y height of the circle
        Ellipse2D.Double e = new Ellipse2D.Double(200, 75, 200, 200);
        g2d.draw(e);

        // Point
        // Point2D.Double p = new Point2D.Double(200, 200);
        // g2d.setColor(Color.RED);
        Ellipse2D.Double p = new Ellipse2D.Double(200, 200, 10, 10);
        g2d.setColor(Color.RED);
        g2d.fill(p);

    }
}
