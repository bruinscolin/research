// visit https://users.csc.calpoly.edu/~kteo/pub/cgta21_1.pdf 
// to see the original paper

import java.awt.image.PixelGrabber;
import java.util.ArrayList;
import java.util.List;
// import static Helpers.*;

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
                new Segment(new Point(-300, -150), new Point(-250, -50)),

                // new Segment(new Point(10, -5), new Point(20, -20)), // sector not free
                // new Segment(new Point(20, -20), new Point(40, 0)), // sector, arc not free
                // new Segment(new Point(40, 0), new Point(40, -40)), // sector, arc not free

                // new Segment(new Point(0, 0), new Point(0, 0)) // works

        };
        DrawingCanvas dc = Setup.main(target, obstacles);
        algo(target, obstacles, dc);

    }

    public static void algo(Point t, Segment[] o, DrawingCanvas dc) {

        // setup before step 1
        List<Point> endpoints = new ArrayList<>(); // V in the context of the paper

        for (Segment s : o) { // populates endpoints
            endpoints.add(s.getP1());
            endpoints.add(s.getP2());
        }

        // Each ray originates at v and passes through a point u ∈ V \ {v}
        // for (int i = 0; i < endpoints.size(); i++) {

        // for (int j = 0; j < endpoints.size(); j++){

        // if (endpoints.get(i).equals(endpoints.get(j))){
        // continue; // if the u and v are the same, ignore
        // }

        int i = 2;
        int j = 0;

        Point u = endpoints.get(i);
        Point v = endpoints.get(j);

        u.print();
        v.print();

        // calculate ray from u -> v
        double x_dist = v.getX() - u.getX(); // both could be negative
        double y_dist = v.getY() - u.getY();

        // angle in radians with respect to x-axis
        double ray_angle = Math.atan2(y_dist, x_dist);

        // decreases/increases angle until equivalent angle
        // within -2pi <= θ <= 2pi is in range
        while (ray_angle >= 2 * Math.PI) {

            ray_angle = ray_angle - (2 * Math.PI);
        }
        while (ray_angle <= -2 * Math.PI) {

            ray_angle = ray_angle + (2 * Math.PI);

        }

        Ray ray = new Ray(u, ray_angle); // will pass through v

        System.out.println("Ray angle = " + ray_angle + " radians");

        // create circle C around target and touching ray
        Point c_center = new Point(t.getX(), t.getY());
        double c_radius = Helpers.foo(c_center, ray);
        Circle circle = new Circle(c_center, c_radius);

        dc.addCircle(circle);
        dc.waitForKey(' '); // or whatever key you want to use to continue
        // dc.addCircle(new Circle(new Point(0, 0), 60));

        // use circle radius and distance from ray origin to circle center
        // to find tangent point(b0)

        double o_c_distance = Helpers.pointDistance(u, c_center); // hypo
        double ray_distance = Math.sqrt((o_c_distance * o_c_distance) - (c_radius * c_radius));

        double tan_x = u.getX() + (ray_distance * Math.cos(ray_angle));
        double tan_y = u.getY() + (ray_distance * Math.sin(ray_angle));

        // b0
        Point tan_point = new Point(tan_x, tan_y);
        Point b0 = tan_point;
        b0.setLabel("b0");
        dc.addPoint(b0);

        Segment u_b0 = new Segment(u, b0);
        dc.addSegment(u_b0);

        System.out.print("Tangent point is: ");
        tan_point.print();

        // check ray reversal from b0
        Point back_boundary = new Point(tan_x + 5000 * Math.cos(ray_angle + Math.PI),
                tan_y + 5000 * Math.sin(ray_angle + Math.PI));
        Segment ray_reversal = new Segment(back_boundary, u);
        for (Segment q : o) {
            if (Helpers.segment_segment_intersect(q, ray_reversal)) {
                System.out.print("Ray reversal intersects with obstacles. Falure.");
                break;
            }
        }

        // find c0
        double c0_x = b0.getX() + (c_radius * Math.cos(ray_angle));
        double c0_y = b0.getY() + (c_radius * Math.sin(ray_angle));
        Point c0 = new Point(c0_x, c0_y);
        c0.setLabel("c0");

        System.out.print("c0: ");
        c0.print();

        // check if b0c0 intersects with any lines
        Segment b0c0 = new Segment(tan_point, c0);
        dc.addSegment(b0c0);
        dc.addPoint(c0);

        for (Segment q : o) {
            if (Helpers.segment_segment_intersect(q, b0c0)) {
                System.out.println("Line b0c0 intersects obstacle. Failure.");
                break;
            }
        }

        // do A1
        double b0c0_distance = Helpers.pointDistance(b0, c0);
        System.out.println("b0c0 distance: " + b0c0_distance);
        // check if quarter circle sector is empty
        Segment b0t = new Segment(b0, t);

        // start of A1
        for (Segment q : o) {
            if (Helpers.segment_segment_intersect(b0c0, q) || Helpers.segment_segment_intersect(b0t, q)) {
                System.out.println("Sector is not free");
                // break;
            }

            if (Helpers.arc_segment_intersect(b0c0, b0t, q)) {
                System.out.println("Arc line is not free");
                // break;
            }
        }

        for (Point p : endpoints) {
            if (!Helpers.isSectorEmpty(b0c0, b0t, p)) {
                System.out.println("Sector is not empty");
                p.print();
                // break;
            }
        }

        // draw sector

        // find angle between b0 and t
        double b0t_x_dist = t.getX() - b0.getX();
        double b0t_y_dist = t.getY() - b0.getY();
        double b0t_angle = Math.atan2(b0t_y_dist, b0t_x_dist);

        // find angle between b0 and c0
        double b0c0_x_dist = c0.getX() - b0.getX();
        double b0c0_y_dist = c0.getY() - b0.getY();
        double b0c0_angle = Math.atan2(b0c0_y_dist, b0c0_x_dist);

        b0t_angle = Math.toDegrees(b0t_angle);
        b0c0_angle = Math.toDegrees(b0c0_angle);

        // ensure angles are positive (0 to 360 degrees)
        if (b0t_angle < 0)
            b0t_angle += 360;
        if (b0c0_angle < 0)
            b0c0_angle += 360;

        dc.addSector(b0, b0c0_distance, b0c0_angle, b0t_angle);

        // end of A1
        // Start of A2

        // check if b0t intersects any obstacle
        for (Segment s : o) {
            if (Helpers.segment_segment_intersect(b0t, s)) {
                System.out.print("A2: b0t is intersected. No trajectory exists.");
                // stop program for this u and v
            }
        }

        // end of A2
        // start of A3

        // Find the closest point c0 ∈ b0c0 to c0 such that
        // b0c0 does not intersect any obstacle

        // note that 10,000 is used because it is larger than S
        // if S is increased, this value may need to be scaled
        Point closest_intersect_point = new Point(10000, 10000);

        for (Segment obstacle : o) {
            // if obstacle intersects b0c0
            if (Helpers.segment_segment_intersect(obstacle, b0c0)) {
                Point current_intersect_point = Helpers.getSegmentSegmentIntersectPoint(obstacle, b0c0);

                // find closest point to c0
                if (Helpers.pointDistance(c0, current_intersect_point) < Helpers.pointDistance(c0,
                        closest_intersect_point)) {
                    closest_intersect_point = new Point(current_intersect_point.getX(), current_intersect_point.getY());

                }

            }

        }

        // if no obstacles intersect b0c0, closest point (c') is c0
        if (closest_intersect_point.equals(new Point(10000, 10000))) {
            closest_intersect_point = c0;
        }
        System.out.print('\n');

    }

    // }
    // }

}
