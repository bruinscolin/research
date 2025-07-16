import java.awt.*; // adds the color and graphics class
import java.awt.geom.*; // adds shapes and paths
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;


public class DrawingCanvas extends JComponent {

    private int width;
    private int height;
    private Point t;
    private Segment[] obstacles;


    // for zoom function
    private double scale = 1.0; 
    private double offsetX = 0;
    private double offsetY = 0;
    private double mouseX;
    private double mouseY;


    // for dynamic image
    private List<Circle> circles = new ArrayList<>(); 
   
    private List<Segment> segments = new ArrayList<>(); 

    private List<Point> points = new ArrayList<>();


    // methods below add a shape to shapes array
    // could be condesed in the future
    public void addCircle(Circle c){
        circles.add(c);
        repaint();

    }

    public void addSegment(Segment s){
        segments.add(s);
        repaint();

    }

    public void addPoint(Point p){
        points.add(p);
        repaint();
    }


    public void waitForKey(char key) {
        CountDownLatch latch = new CountDownLatch(1);

        KeyStroke ks = KeyStroke.getKeyStroke(key);
        String actionKey = "waitForKey_" + key;

        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(ks, actionKey);
        getActionMap().put(actionKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                latch.countDown();
                // Remove listener after it's used once
                getInputMap(WHEN_IN_FOCUSED_WINDOW).remove(ks);
                getActionMap().remove(actionKey);
            }
        });

        try {
            latch.await(); // Blocks until the key is pressed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }




    public DrawingCanvas(int w, int h, Point t, Segment[] obstacles) {
        this.width = w;
        this.height = h;
        this.t = t;
        this.obstacles = obstacles;


        // adjusts the zoom when wheel scrolled, marks the X and Y of mouse
        this.addMouseWheelListener(e -> {
            double zoomFactor = e.getPreciseWheelRotation() < 0 ? 1.1 : 1 / 1.1;

            mouseX = e.getX();
            mouseY = e.getY();

            double prevScale = scale;
            scale *= zoomFactor;

            offsetX = mouseX - ((mouseX - offsetX) * (scale / prevScale));
            offsetY = mouseY - ((mouseY - offsetY) * (scale / prevScale));
            repaint();
        });

        // clicking gives coordinates
        this.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX() - width / 2; // convert to logical coords
            int y = height / 2 - e.getY(); 

            // coords can only be ints from the mouseevent get functions
            System.out.println("Mouse clicked at: (" + x + ", " + y + ")");
        }
        });

        // pressing R key resets zoom
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("R"), "resetZoom");
        getActionMap().put("resetZoom", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scale = 1.0;
                offsetX = 0;
                offsetY = 0;
                mouseX = 0;
                mouseY = 0;

                repaint();
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

        // draw circles
       for (Circle c : circles){
            Point center = c.getCenter();
            double draw_x = center.getDrX();
            double draw_y = center.getDrY();
            outlineEllipseFromCenter(draw_x, draw_y, 2 * c.getRadius(), 2 * c.getRadius(), g2d, Color.BLUE);

        } 
        
       // draw segments
       for (Segment s : segments){
           
           double x1 = s.getDrX1();
           double y1 = s.getDrY1();
           double x2 = s.getDrX2();
           double y2 = s.getDrY2();

           Line2D.Double line = new Line2D.Double(x1, y1, x2, y2);
           g2d.setColor(Color.BLACK);
           g2d.draw(line);

       }
    
       // draw points
       for (Point p : points){
          drawEllipseFromCenter(p.getDrX(), p.getDrY(), 10, 10, g2d, hot_pink);
          g2d.drawString(p.getLabel(),(int) p.getDrX() + 10, (int) p.getDrY() + 10);
       }


       
        // target and obstacles can be manually entered since that are static
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




    }


    // draws a circle from the center of the given coords, best use for a point
    public void drawEllipseFromCenter(double x, double y, double width, double height, Graphics2D g, Color c)
    {
        // double newX = x - width / 2.0;
        // double newY = y - height / 2.0;

        // Ellipse2D ellipse = new Ellipse2D.Double(newX, newY, width, height);
        double screenX = (x * scale) + offsetX;
        double screenY = (y * scale) + offsetY;

        double screenWidth = width * scale;
        double screenHeight = height * scale;

        double drawX = screenX - screenWidth / 2.0;
        double drawY = screenY - screenHeight / 2.0;

        Ellipse2D ellipse = new Ellipse2D.Double(drawX, drawY, screenWidth, screenHeight);


        g.setColor(c);
        g.fill(ellipse);
    }



    public void outlineEllipseFromCenter(double x, double y, double width, double height, Graphics2D g, Color c)
    // public void outlineEllipseFromCenter(Circle c, double width, double height, Graphics2D g, Color color)
    {
        // double x = c.getX();
        // double y = c.getY();

        double screenX = (x * scale) + offsetX;
        double screenY = (y * scale) + offsetY;

        double screenWidth = width * scale;
        double screenHeight = height * scale;

        double drawX = screenX - screenWidth / 2.0;
        double drawY = screenY - screenHeight / 2.0;

        Ellipse2D ellipse = new Ellipse2D.Double(drawX, drawY, screenWidth, screenHeight);

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
