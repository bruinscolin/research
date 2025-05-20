import java.awt.*; // adds the color and graphics class
import java.awt.geom.*; // adds shapes and paths
import javax.swing.*;


public class App {
    public static void main(String[] args) throws Exception {

        Point target = new Point(700, 500);

        Segment[] obstacles = { 
            new Segment(new Point(600, 600), new Point(700, 700)),
            new Segment(new Point(100, 100), new Point(240, 610)),
            new Segment(new Point(639, 367), new Point(673, 749))
        };

        Setup.main(target, obstacles);
        // algo(target, obstacles);
       
        
    }

    public static void algo(Point t, Segment[] o){
        // Graphics
        // Graphics g;
        // Graphics2D g2d = (Graphics2D) g; // cast to Graphics2D

        // Line2D.Double line = new Line2D.Double(0, 0, width, height); // create a line
        // Rectangle2D.Double r = new Rectangle2D.Double(0, 0, 100, 100); // create a rectangle
        // g2d.setColor(new Color(162, 169, 186));
        // g2d.fill(r);
    }
}
