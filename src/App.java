// visit https://users.csc.calpoly.edu/~kteo/pub/cgta21_1.pdf 
// to see the original paper

import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) throws Exception {

        // Point target = new Point(700, 500);

        Point target = new Point(0, 0);

        // Segment[] obstacles = { 
        // new Segment(new Point(600, 600), new Point(700, 700)),
        // new Segment(new Point(100, 100), new Point(240, 610)),
        // new Segment(new Point(639, 367), new Point(673, 749))};

        Segment[] obstacles = { 
            new Segment(new Point(-230, -125), new Point(-100, -250)),
            new Segment(new Point(-300, -150), new Point(-250, -50))
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

            for (int j = 0; j < endpoints.size(); j++){
                if (i == j || (endpoints.get(i).equals(endpoints.get(j)))){
                    continue; // if the u and v are the same, ignore
                }
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
                Point c_center = new Point(t.getX(), t.getY());
                double c_radius = foo(c_center, ray);
                
                
                // do A1
                u.print(); 
                v.print(); 
                System.out.print('\n');

                
            }
            
        }
    }

    public static void A1(Point t, Segment[] o, DrawingCanvas dc) {

    }



    public static double foo(Point circle_center, Ray ray){
        double radius = Math.abs(
            (
                (-1 * Math.asin(ray.getAngle())) *
                (circle_center.getX() - ray.getX())
            )
            +
            (Math.acos(ray.getAngle()) * (circle_center.getY() - ray.getY()))
        );
        System.out.println("Circle Radius: " + radius);

        return radius;

    }
}
