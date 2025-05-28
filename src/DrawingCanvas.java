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

        Color hot_pink = new Color(255, 0, 195);

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
        Line2D.Double drawing_x_axis = new Line2D.Double(0, height / 2, 50, height / 2);
        g2d.setColor(Color.BLACK);
        g2d.draw(drawing_x_axis);


        String drawing_x_label = String.valueOf(height / 2);
        g2d.drawString(drawing_x_label, 50, height / 2 + 15);


        Line2D.Double logic_x_axis = new Line2D.Double(50, height / 2, 100, height / 2);
        g2d.setColor(hot_pink);
        g2d.draw(logic_x_axis);


        String logic_x_label = String.valueOf(0);
        g2d.drawString(logic_x_label, 100, height / 2 - 15);
        g2d.setColor(Color.BLACK);

        // Y axis
        Line2D.Double drawing_y_axis = new Line2D.Double(width / 2, 0, width / 2, 50);
        g2d.draw(drawing_y_axis);


        String drawing_y_label = String.valueOf(width / 2);
        g2d.drawString(drawing_y_label, width / 2 + 15, 50);



        Line2D.Double logic_y_axis = new Line2D.Double(width / 2, 50, width / 2, 100);
        g2d.setColor(hot_pink);
        g2d.draw(logic_y_axis);


        String logic_y_label = String.valueOf(0);
        g2d.drawString(logic_y_label, width / 2 - 15, 100);


        // circle test
        drawEllipseFromCenter( t.getDrX(), t.getDrY(), 78, 78, g2d, Color.GREEN );
        drawRayFromOrigin(400, 650, 0.343, g2d, Color.BLACK);



        // target point
        drawEllipseFromCenter(t.getDrX(), t.getDrY(), 10, 10, g2d, Color.RED);
        int target_x = (int) t.getDrX();
        int target_y = (int) t.getDrY();
        g2d.drawString("Target", target_x + 10, target_y + 10);


        // draw obstacles
        g2d.setStroke(new BasicStroke(2));
        for (Segment s : obstacles) {
            double x1 = s.getDrX1();
            double y1 = s.getDrY1();
            double x2 = s.getDrX2();
            double y2 = s.getDrY2();

            Line2D.Double line = new Line2D.Double(x1, y1, x2, y2);
            g2d.setColor(Color.BLUE);
            g2d.draw(line);
        }
        g2d.setStroke(new BasicStroke(1));

        // Circle, x and y coordniates where the rectagnluar border starts
        // X height and Y height of the circle
        // Ellipse2D.Double e = new Ellipse2D.Double(200, 75, 200, 200);
        // g2d.draw(e);

        // Point
        // Point2D.Double p = new Point2D.Double(200, 200);
        // g2d.setColor(hot_pink);
        // Ellipse2D.Double p = new Ellipse2D.Double(200, 200, 10, 10);
        // g2d.setColor(hot_pink);
        // g2d.fill(p);


        // Ellipse2D.Double t = new Ellipse2D.Double(700, 500, 900, 900);
        // g2d.setColor(hot_pink);
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

    public void drawRayFromOrigin(double x, double y, double angle, Graphics2D g, Color c){
        
        Color original_color = g.getColor();
        g.setColor(c);
        double x_endpoint = x + 1000 * Math.acos(angle);
        double y_endpoint = y + 1000 * Math.asin(angle);

        Line2D.Double ray = new Line2D.Double(x, y, x_endpoint, y_endpoint);
        g.draw(ray);

        g.setColor(original_color);
    }
}
