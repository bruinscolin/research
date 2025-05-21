import java.awt.*; // adds the color and graphics class
import java.awt.geom.*; // adds shapes and paths
import javax.swing.*;


public class DrawingCanvas extends JComponent {

    private int width;
    private int height;
    private Point t;
    private Segment[] obstacles;


    public DrawingCanvas(int w, int h, Point t, Segment[] obstacles) {
        this.width = w;
        this.height = h;
        this.t = t;
        this.obstacles = obstacles;
    }
    
    // sets color, draws lines, curves, and shapes
    protected  void paintComponent(Graphics g) { 
        Graphics2D g2d = (Graphics2D) g; // cast to Graphics2D

        // smooth edges for diagonal lines
        RenderingHints rh = new RenderingHints(
            RenderingHints.KEY_ANTIALIASING, 
            RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setRenderingHints(rh);
        // Line2D.Double line = new Line2D.Double(0, 0, width, height); // create a line
        Rectangle2D.Double r = new Rectangle2D.Double(0, 0, width, height); // create a rectangle
        g2d.setColor(new Color(162, 169, 186));
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


        // target point
        drawEllipseFromCenter(t.getX(), t.getY(), 10, 10, g2d, Color.RED);
        int target_x = (int) t.getX();
        int target_y = (int) t.getY();
        g2d.drawString("Target", target_x + 10, target_y + 10);


        // draw obstacles
        g2d.setStroke(new BasicStroke(2));
        for (Segment s : obstacles) {
            double x1 = s.getX1();
            double y1 = s.getY1();
            double x2 = s.getX2();
            double y2 = s.getY2();

            Line2D.Double line = new Line2D.Double(x1, y1, x2, y2);
            g2d.setColor(Color.BLUE);
            g2d.draw(line);
        }

        // Circle, x and y coordniates where the rectagnluar border starts
        // X height and Y height of the circle
        // Ellipse2D.Double e = new Ellipse2D.Double(200, 75, 200, 200);
        // g2d.draw(e);

        // Point
        // Point2D.Double p = new Point2D.Double(200, 200);
        // g2d.setColor(Color.RED);
        // Ellipse2D.Double p = new Ellipse2D.Double(200, 200, 10, 10);
        // g2d.setColor(Color.RED);
        // g2d.fill(p);


        // Ellipse2D.Double t = new Ellipse2D.Double(700, 500, 900, 900);
        // g2d.setColor(Color.RED);
        // g2d.fill(t);
        // drawEllipseFromCenter(700, 500, 300, 300, g2d, Color.BLUE);

    }


    // draws a circle from the center of the given coords, best use for a point
    public void drawEllipseFromCenter(double x, double y, double width, double height, Graphics2D g, Color c)
    {
        double newX = x - width / 2.0;
        double newY = y - height / 2.0;

        Ellipse2D ellipse = new Ellipse2D.Double(newX, newY, width, height);

        g.setColor(c);
        g.fill(ellipse);
    }
}
