// visit https://users.csc.calpoly.edu/~kteo/pub/cgta21_1.pdf 
// to see the original paper

import java.util.ArrayList;
import java.util.List;
// import static Helpers.*;

public class App {
    public static void main(String[] args) throws Exception {

        // Point target = new Point(700, 500);

        Point target = new Point(0, 0);

        Segment[] obstacles = {
                new Segment(new Point(-230, -125), new Point(-100, -250)),
                new Segment(new Point(-300, -150), new Point(-250, -50)),

                new Segment(new Point(-200, -125), new Point(-50, -250)),
                new Segment(new Point(200, 100), new Point(200, -100)),
                new Segment(new Point(-200, 100), new Point(-200, -200)),

                new Segment(new Point(20, 0), new Point(50, -30)),
                new Segment(new Point(15, 0), new Point(50, -30)),
                // new Segment(new Point(45, -20 ), new Point(45, -50)),

                // new Segment(new Point(10, -5), new Point(20, -20)), // sector not free
                // new Segment(new Point(20, -20), new Point(40, 0)), // sector, arc not free
                // new Segment(new Point(40, 0), new Point(40, -40)), // sector, arc not free

                // new Segment(new Point(0, 0), new Point(0, 0)) // works

                new Segment(new Point(-30, -40), new Point(-40, -50)),

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

        int i = 2; // 2
        int j = 0; // 0

        Point u = endpoints.get(i);
        Point v = endpoints.get(j);

        u.print();
        v.print();

        List<Segment> polygon = Helpers.preProcessPolygon(u, o);

        List<Segment> polygon_segments = new ArrayList<>();

        for (Segment seg : polygon) {
            // Segment s = seg.toSegment(u);
            polygon_segments.add(seg);

            dc.addSegment(seg);

        }

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

        // System.out.print("Tangent point is: ");
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

        // System.out.print("c0: ");
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

        if (Helpers.orientationTest(b0, c0, t) > 0) {

            dc.addSector(b0, b0c0_distance, b0c0_angle, b0t_angle, -1);
        } else {
            dc.addSector(b0, b0c0_distance, b0t_angle, b0c0_angle, -1);
        }
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

        dc.waitForKey(' '); // or whatever key you want to use to continue

        // Find the closest point c0 ∈ b0c0 to c0 such that
        // b0c0 does not intersect any obstacle

        Point closest_intersect_point = c0;

        for (Segment obstacle : o) {
            // if obstacle intersects b0c0
            if (Helpers.segment_segment_intersect(obstacle, b0c0)) {
                Point current_intersect_point = Helpers.getSegmentSegmentIntersectPoint(obstacle, b0c0);

                // find closest point to b0
                if (Helpers.pointDistance(b0, current_intersect_point) < Helpers.pointDistance(b0,
                        closest_intersect_point)) {

                    closest_intersect_point = new Point(current_intersect_point.getX(), current_intersect_point.getY());

                }
            }

        }

        if (closest_intersect_point.equals(c0)) {
            closest_intersect_point.setLabel("c0 & c'");

        } else {
            closest_intersect_point.setLabel("c'");
        }

        closest_intersect_point.setColor("red");

        dc.addPoint(closest_intersect_point);
        Point cp = closest_intersect_point;
        cp.print();
        // find b'
        // circle through c' and t, cetnered at b'
        Point bp;

        if (cp.equals(c0)) {
            bp = b0;
            bp.setLabel("b0 & b'");

        } else {

            double dx = v.getX() - u.getX();
            double dy = v.getY() - u.getY();

            // vectors from u to each point
            double A1 = cp.getX() - u.getX();
            double B1 = cp.getY() - u.getY();
            double A2 = t.getX() - u.getX();
            double B2 = t.getY() - u.getY();

            // A1 = x1 - ux
            // A2 = x2 - ux
            // B1 = y1 - uy
            // B2 = y2 - uy

            // xc = ux + t * dx
            // yc = uy + t * dy

            // t = (A1² - A2² + B1² - B2²) / 2(A1dx - A2dx + B1dy - B2dy)
            double numerator = A1 * A1 + B1 * B1 - A2 * A2 - B2 * B2;
            double denominator = 2 * ((A1 - A2) * dx + (B1 - B2) * dy);
            double t_param = numerator / denominator;

            // calculate b'
            double bp_x = u.getX() + t_param * dx;

            double bp_y = u.getY() + t_param * dy;
            bp = new Point(bp_x, bp_y);

            System.out.print("b'c' distance: " + Helpers.pointDistance(bp, cp) + '\n');
            System.out.print("b't distance: " + Helpers.pointDistance(bp, t));
            bp.setLabel("b'");
        }
        // bp.setLabel("b'");
        bp.setColor("red");

        dc.addPoint(bp);

        double radius = Helpers.pointDistance(bp, cp);

        // draw b'c' sector
        // dc.addSector(b0, b0c0_distance, b0c0_angle, b0t_angle);
        double bp_t_angle = Math.atan2(t.getY() - bp.getY(), t.getX() - bp.getX());
        double bp_cp_angle = Math.atan2(cp.getY() - bp.getY(), cp.getX() - bp.getX());

        bp_t_angle = Math.toDegrees(bp_t_angle);
        bp_cp_angle = Math.toDegrees(bp_cp_angle);

        if (bp_t_angle < 0)
            bp_t_angle += 360;
        if (bp_cp_angle < 0)
            bp_cp_angle += 360;
        if (Helpers.orientationTest(bp, cp, t) > 0) {
            dc.addSector(bp, radius, bp_cp_angle, bp_t_angle, 0);
        } else {
            dc.addSector(bp, radius, bp_t_angle, bp_cp_angle, 0);
        }
        // end of A3
        // start of A4

        // check if b't intersects obstacles
        Segment bpt = new Segment(bp, t);

        for (Segment s : o) {
            if (Helpers.segment_segment_intersect(s, bpt)) {
                System.out.print("b't intersects with obstacle. Failure.");
                break;
            }
        }

        dc.waitForKey(' ');
        // end of A4
        // start of A5

        Point bpp = bp;
        Point closest = v;
        Segment vbp = new Segment(v, bp);

        for (Point p : endpoints) {

            // check if p is on the same side of ray as t
            if (Helpers.orientationTest(v, bp, p) * Helpers.orientationTest(v, bp, t) < 0) {
                continue;

            }
            // create ray from t to endpoint
            double t_endpoint_angle = Math.atan2(p.getY() - t.getY(), p.getX() - t.getX());

            // for creating a segment to check for intersect with vb'
            // 5000 may have to change depending on S

            Point ray_endpoint = new Point(t.getX() + 10000 * Math.cos(t_endpoint_angle),
                    t.getY() + 10000 * Math.sin(t_endpoint_angle));

            Segment t_ray = new Segment(t, ray_endpoint);
            // dc.addSegment(t_ray);

            if (Helpers.segment_segment_intersect(t_ray, vbp)) {
                Point current_point = Helpers.getSegmentSegmentIntersectPoint(t_ray, vbp);

                // un-comment below to see rays
                // dc.addSegment(t_ray);

                if (Helpers.pointDistance(current_point, bp) < Helpers.pointDistance(closest, bp)) {

                    closest = new Point(current_point.getX(), current_point.getY());
                }
            }

        }

        bpp = closest;
        if (bpp.equals(v)) {
            bpp = bp;

        }
        if (bpp.equals(bp)) {
            bpp.setLabel("b' & b''");

        } else {
            bpp.setLabel("b''");
        }
        bpp.setColor("green");
        dc.addPoint(bpp);

        // find c''
        double bppt_radius = Helpers.pointDistance(bpp, t);

        double cpp_x = bpp.getX() + bppt_radius * Math.cos(ray_angle);
        double cpp_y = bpp.getY() + bppt_radius * Math.sin(ray_angle);

        Point cpp = new Point(cpp_x, cpp_y);
        cpp.setColor("green");
        cpp.setLabel("c''");
        dc.addPoint(cpp);

        // start of adding b'' sector
        // drawing logic only
        double bpp_t_angle = Math.atan2(t.getY() - bpp.getY(), t.getX() - bpp.getX());
        double bpp_cpp_angle = Math.atan2(cpp.getY() - bpp.getY(), cpp.getX() - bpp.getX());

        bpp_t_angle = Math.toDegrees(bpp_t_angle);
        bpp_cpp_angle = Math.toDegrees(bpp_cpp_angle);

        if (bpp_t_angle < 0)
            bpp_t_angle += 360;
        if (bpp_cpp_angle < 0)
            bpp_cpp_angle += 360;

        double double_sector_radius = Helpers.pointDistance(bpp, cpp);

        if (Helpers.orientationTest(bpp, cpp, t) > 0) {
            dc.addSector(bpp, double_sector_radius, bpp_cpp_angle, bpp_t_angle, 1);
        } else {
            dc.addSector(bpp, double_sector_radius, bpp_t_angle, bpp_cpp_angle, 1);
        }
        // end of adding b'' sector

        // end of A5
        // start of A6

        Segment bpcpp = new Segment(bp, cpp);

        for (Segment q : o) {
            if (Helpers.segment_segment_intersect(bpcpp, q) || Helpers.segment_segment_intersect(bpt, q)) {
                System.out.println("Sector is not free");
                // break;
            }

            if (Helpers.arc_segment_intersect(bpcpp, bpt, q)) {
                System.out.println("Arc line is not free");
                // break;
            }
        }

        for (Point p : endpoints) {
            if (!Helpers.isSectorEmpty(bpcpp, bpt, p)) {
                System.out.println("Sector is not empty");
                p.print();
                // break;
            }
        }
        dc.waitForKey(' ');
        // end of A6
        // start of A7

        Point b3p = bp;

        Point closest_b3p = bp;
        double b3p_sector_radius = 0;

        Segment bpcp = new Segment(bp, cp);

        for (Point p : endpoints) {
            if (Helpers.isSectorEmpty(bpcp, bpt, p)) {
                continue;

            }

            double dx = v.getX() - u.getX();
            double dy = v.getY() - u.getY();

            // vectors from u to each point
            double A1 = p.getX() - u.getX();
            double B1 = p.getY() - u.getY();
            double A2 = t.getX() - u.getX();
            double B2 = t.getY() - u.getY();

            // A1 = x1 - ux
            // A2 = x2 - ux
            // B1 = y1 - uy
            // B2 = y2 - uy

            // xc = ux + t * dx
            // yc = uy + t * dy

            // t = (A1² - A2² + B1² - B2²) / 2(A1dx - A2dx + B1dy - B2dy)
            double numerator = A1 * A1 + B1 * B1 - A2 * A2 - B2 * B2;
            double denominator = 2 * ((A1 - A2) * dx + (B1 - B2) * dy);
            double t_param = numerator / denominator;

            // calculate b'
            double b3p_x = u.getX() + t_param * dx;

            double b3p_y = u.getY() + t_param * dy;
            closest_b3p = new Point(b3p_x, b3p_y);

            // System.out.print("b'''c' distance: " + Helpers.pointDistance(b3p, p) + '\n');
            // System.out.print("b'''t distance: " + Helpers.pointDistance(b3p, t));

            if (Helpers.pointDistance(closest_b3p, bp) > Helpers.pointDistance(b3p, bp)) {
                b3p = closest_b3p;
                b3p_sector_radius = Helpers.pointDistance(b3p, t);

            }

        }

        // now check for tangencies

        for (Segment obstacle : o) {
            Point tangent_b3p = Helpers.findFirstTangencyAlongRay(u, v, t, obstacle, ray_angle, bp, bpcp, bpt);

            if (tangent_b3p != null) {

                // we want the point FURTHEST from bp (closest to u) - first tangency
                if (Helpers.pointDistance(tangent_b3p, bp) > Helpers.pointDistance(b3p, bp)) {
                    b3p = tangent_b3p;
                    b3p_sector_radius = Helpers.pointDistance(b3p, t);
                }
            }
        }

        // check if b''' is b' or b''
        if (b3p.equals(bpp)) {
            bpp.setLabel("b'' & b'''");
        } else if (b3p.equals(bp)) {
            bp.setLabel("b' & b'''");
        } else {
            b3p.setLabel("b'''");
        }
        b3p.setColor("blue");
        dc.addPoint(b3p);

        // find correct c'''
        double c3p_x = b3p.getX() + b3p_sector_radius * Math.cos(ray_angle);
        double c3p_y = b3p.getY() + b3p_sector_radius * Math.sin(ray_angle);

        Point c3p = new Point(c3p_x, c3p_y);
        c3p.setLabel("c'''");
        c3p.setColor("blue");
        dc.addPoint(c3p);

        System.out.println("b''' c''' distance: " + Helpers.pointDistance(b3p, c3p));
        System.out.println("b''' t distance: " + Helpers.pointDistance(b3p, t));

        // sector drawing logic

        double b3c3_distance = Helpers.pointDistance(b3p, c3p);

        double b3t_x_dist = t.getX() - b3p.getX();
        double b3t_y_dist = t.getY() - b3p.getY();
        double b3t_angle = Math.atan2(b3t_y_dist, b3t_x_dist);

        // find angle between b''' and c'''
        double b3c3_x_dist = c3p.getX() - b3p.getX();
        double b3c3_y_dist = c3p.getY() - b3p.getY();
        double b3c3_angle = Math.atan2(b3c3_y_dist, b3c3_x_dist);

        b3t_angle = Math.toDegrees(b3t_angle);
        b3c3_angle = Math.toDegrees(b3c3_angle);

        // ensure angles are positive (0 to 360 degrees)
        if (b3t_angle < 0)
            b3t_angle += 360;
        if (b3c3_angle < 0)
            b3c3_angle += 360;

        if (Helpers.orientationTest(b3p, c3p, t) > 0) {
            dc.addSector(b3p, b3c3_distance, b3c3_angle, b3t_angle, 2);
        } else {
            dc.addSector(b3p, b3c3_distance, b3t_angle, b3c3_angle, 2);
        }

        System.out.println('\n');

    }

    // }
    // }

}
