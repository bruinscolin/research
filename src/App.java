// visit https://users.csc.calpoly.edu/~kteo/pub/cgta21_1.pdf 
// to see the original paper

import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) throws Exception {

        Point target = new Point(700, 500);

        // Segment[] obstacles = { 
        // new Segment(new Point(600, 600), new Point(700, 700)),
        // new Segment(new Point(100, 100), new Point(240, 610)),
        // new Segment(new Point(639, 367), new Point(673, 749))};

        Segment[] obstacles = { 
            new Segment(new Point(470, 625), new Point(600, 750)),
            new Segment(new Point(400, 650), new Point(450, 550))
        };
        DrawingCanvas dc = Setup.main(target, obstacles);
        algo(target, obstacles, dc);
        
        
    }

    public static void algo(Point t, Segment[] o, DrawingCanvas dc) {

        // setup before step 1
        List<Point> endpoints = new ArrayList<>(); // V in the context of the paper

        for (Segment s: o) { // populates endpoints
            endpoints.add(s.getP1());
            endpoints.add(s.getP2());
        }

        // Each ray originates at v and passes through a point u ∈ V \ {v}
        for (int i = 0; i < endpoints.size(); i++) {

            for (int j = i + 1; j < endpoints.size(); j++){
                Point u = endpoints.get(i);
                Point v = endpoints.get(j);
                
                // calculate ray from u -> v
                double x_dist = v.getX() - u.getX(); // both could be negative
                double y_dist = v.getY() - u.getY();

                // angle in radians with respect to x-axis
                double ray_angle = Math.atan2(y_dist, x_dist); 
                Ray ray = new Ray(u, ray_angle); // will pass through v

                System.out.println("Ray angle = " + ray_angle + " radians");


                // create circle C around target and touching ray

                
                // do A1
                u.print(); 
                v.print(); 
                
            }
            
        }
    }

    public static void A1(Point t, Segment[] o, DrawingCanvas dc) {

    }
}
