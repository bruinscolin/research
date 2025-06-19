import java.awt.*; // adds the color and graphics class
import java.awt.geom.*; // adds shapes and paths
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


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



        this.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX() - width / 2; // convert to logical coords
            int y = height / 2 - e.getY(); 

            // coords can only be ints from the mouseevent get functions
            System.out.println("Mouse clicked at: (" + x + ", " + y + ")");
        }
});

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

        outlineEllipseFromCenter( t.getDrX(), t.getDrY(), 78, 78, g2d, Color.BLUE );

        // outlineEllipseFromCenter( t.getDrX(), t.getDrY(), 40.36036763977876, 40.36036763977876, g2d, Color.BLUE );
        drawRayFromOrigin(400, 650, 0.343, g2d, Color.BLACK);

        // draw b0
        Point tan_point = new Point(13.5746606334842, -38.00904977375565);
        drawEllipseFromCenter(tan_point.getDrX(), tan_point.getDrY(), 10, 10, g2d, hot_pink);

        g2d.drawString("b0",(int) tan_point.getDrX() + 10, (int) tan_point.getDrY() + 10);

       

        // target point
        drawEllipseFromCenter(t.getDrX(), t.getDrY(), 10, 10, g2d, Color.RED);
        int target_x = (int) t.getDrX();
        int target_y = (int) t.getDrY();
        g2d.drawString("Target", target_x + 10, target_y + 10);

        // c0 
        Point c0 = new Point(51.58371040723986, -24.434389140271485);
        drawEllipseFromCenter(c0.getDrX(), c0.getDrY(), 10, 10, g2d, Color.GREEN);
        int c0_string_x = (int) c0.getDrX();
        int c0_string_y = (int) c0.getDrY();

        g2d.drawString("c0", c0_string_x + 10, c0_string_y + 10);

        


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

    // same as previous function, but only outlines
    public void outlineEllipseFromCenter(double x, double y, double width, double height, Graphics2D g, Color c)
    {
        double newX = x - width / 2.0;
        double newY = y - height / 2.0;

        Ellipse2D ellipse = new Ellipse2D.Double(newX, newY, width, height);

        g.setColor(c);
        g.draw(ellipse);
    }

    public void drawRayFromOrigin(double x, double y, double angle, Graphics2D g, Color c){
        
        Color original_color = g.getColor();
        g.setColor(c);
        double x_endpoint = x + 5000 * Math.cos(angle); // 5000 value just to put it offscreen
        double y_endpoint = y - 5000 * Math.sin(angle); // may need to be adjusted based on window dimensions

        Line2D.Double ray = new Line2D.Double(x, y, x_endpoint, y_endpoint);
        g.draw(ray);

        g.setColor(original_color);
    }
}
