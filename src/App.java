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
            new Segment(new Point(-300, -150), new Point(-250, -50)),
            new Segment(new Point(-1000, -999), new Point(-999, -998)),
            new Segment(new Point(5, 5), new Point(-10, -10)),
            // new Segment(new Point(0, 0), new Point(100, 100)) // breaks when it shouldn't
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
        // for (int i = 0; i < endpoints.size(); i++) {

        //     for (int j = 0; j < endpoints.size(); j++){

                // if (endpoints.get(i).equals(endpoints.get(j))){
                //     continue; // if the u and v are the same, ignore
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
                while (ray_angle >= 2 * Math.PI){

                    ray_angle = ray_angle - (2 * Math.PI);
                }
                while (ray_angle <= -2 * Math.PI){

                    ray_angle = ray_angle + (2 * Math.PI);

                }

                Ray ray = new Ray(u, ray_angle); // will pass through v

                System.out.println("Ray angle = " + ray_angle + " radians");


                // create circle C around target and touching ray
                Point c_center = new Point(t.getX(), t.getY());
                double c_radius = foo(c_center, ray);
                Circle circle = new Circle(c_center, c_radius);
                
                // use circle radius and distance from ray origin to circle center
                // to find tangent point(b0)

                double o_c_distance = pointDistance(u, c_center); // hypo
                double ray_distance = Math.sqrt((o_c_distance * o_c_distance) - (c_radius * c_radius));
                
                double tan_x = u.getX() + (ray_distance * Math.cos(ray_angle));
                double tan_y = u.getY() + (ray_distance * Math.sin(ray_angle));

                // b0
                Point tan_point = new Point(tan_x, tan_y);
                Point b0 = tan_point;

                System.out.print("Tangent point is: ");
                tan_point.print();
                

                // check ray reversal from b0 
                Point back_boundary = new Point(tan_x + 5000 * Math.cos(ray_angle + Math.PI), tan_y + 5000 * Math.sin(ray_angle + Math.PI));
                Segment ray_reversal = new Segment(back_boundary, u);
                for (Segment q : o){
                    if (segment_segment_intersect(q, ray_reversal)){
                        System.out.print("Ray reversal intersects with obstacles. Falure.");
                        break;
                    }
                }

                // find c0
                double c0_x = b0.getX() + ( c_radius * Math.cos(ray_angle) );
                double c0_y = b0.getY() + ( c_radius * Math.sin(ray_angle) );
                Point c0 = new Point(c0_x, c0_y);

                System.out.print("c0: ");
                c0.print();

                // check if b0c0 intersects with any lines
                Segment b0c0 = new Segment(tan_point, c0);
                for (Segment q : o){
                if (segment_segment_intersect(q, b0c0)){
                    System.out.println("Line b0c0 intersects obstacle. Failure.");
                    break;
                    }
                }


                // do A1
                double b0c0_distance = pointDistance(b0, c0);
                System.out.println("b0c0 distance: " + b0c0_distance);
                // check if quarter circle sector is empty
                Segment b0t = new Segment(b0, t);


                // start of A1
                for (Segment q: o){
                    if (segment_segment_intersect(b0c0, q) || segment_segment_intersect(b0t, q)){
                        System.out.println("Sector is not free");
                        // break;
                    }

                    if (arc_segment_intersect(b0c0, b0t, q)){
                        System.out.println("Arc line is not free");
                        // break;
                    }
                }

                for (Point p: endpoints){
                    if (!isSectorEmpty(b0c0, b0t, p)){
                        System.out.println("Sector is not empty");
                        p.print();
                        // break;
                    }
                }
                // end of A1 


                System.out.print('\n');
                
            }
            
    //     }
    // }

    
    // given a circle and a ray, find the point that is 
    // tanget between them
    public static Point circleRayTangent(Circle c, Ray r){
        return new Point(0,0);
    }

    // given center t and a line (or ray)
    // find the radius of a circle that is
    // tangent to the line
    public static double foo(Point circle_center, Ray ray){
        double radius = Math.abs(
            (
                (-1 * Math.sin(ray.getAngle())) *
                (circle_center.getX() - ray.getX())
            )
            +
            (Math.cos(ray.getAngle()) * (circle_center.getY() - ray.getY()))
        ); 
        // r =|(-sinθ)(x0 - x1) + cosθ(y0 - y1)|
        // x0, y0 = circle center
        // x1, y1 = ray origin
        System.out.println("Circle Radius: " + radius);

        return radius;

    }

    // orientation testing to see if point lies above, below or on
    // a line segment
    // s1, s2 are lines segment endpoints
    // q is query point
    public static double orientationTest(Point s1, Point s2, Point q){
        double a1 = s1.getX();
        double a2 = s1.getY();

        double b1 = s2.getX();
        double b2 = s2.getY();

        double c1 = q.getX();
        double c2 = q.getY();

        double det = a1 * (b2 - c2) - a2 * (b1 - c1) + ((b1 * c2) - (b2 * c1));
        // if det > 0: cc
        // if det < 0: clockwise
        // if det = 0: collinear
        return det;
    }


    // return distance between two points
    public static double pointDistance(Point p1, Point p2){
        double x_dist = Math.abs(p1.getX() - p2.getX());
        double y_dist = Math.abs(p1.getY() - p2.getY());

        double dist = Math.sqrt(( x_dist * x_dist ) + ( y_dist * y_dist ));
        return dist;
    }



    ///// intersect methods //////
    // public static boolean segment_segment_intersect(Segment s1, Segment s2){
    //     if (( orientationTest(s1.getP1(), s1.getP2(), s2.getP1()) >= 0 &&
    //     orientationTest(s1.getP1(), s1.getP2(), s2.getP2()) >= 0 ) ||
    //     ( orientationTest(s1.getP1(), s1.getP2(), s2.getP1()) <= 0 &&
    //     orientationTest(s1.getP1(), s1.getP2(), s2.getP2()) <= 0 )){
    //         return false;
    //     }
    //     return true;
    //  
    // }

    public static boolean segment_segment_intersect(Segment s1, Segment s2){
        Point p1 = s1.getP1();
        Point q1 = s1.getP2();
        Point p2 = s2.getP1();
        Point q2 = s2.getP2();

        double o1 = orientationTest(p1, q1, p2);
        double o2 = orientationTest(p1, q1, q2);
        double o3 = orientationTest(p2, q2, p1);
        double o4 = orientationTest(p2, q2, q1);

        // General case: segments intersect if they straddle each other
        if ((o1 * o2 < 0) && (o3 * o4 < 0)) {
        return true;
        }

        // Optionally: handle collinear + overlapping edge cases here if needed
        return false;
    }

    // USE ORIENTATIONTEST FOR ARC CHECK
    public static boolean arc_segment_intersect(Segment a1, Segment a2, Segment q){

        double radius = pointDistance(a1.getP1(), a1.getP2());

        Point center;
        // determine what the center is
        if (a1.getP1().equals(a2.getP1()) || a1.getP1().equals(a2.getP2())){
            center = a1.getP1();
        } else{
            center = a1.getP2();
        }

        Circle c = new Circle(center, radius);

        // find intersection(s)
        List<Point> intersections = intersectSegmentCircle(q, c);

        for (Point p: intersections){
            Point p1 = a1.getP1().equals(center) ? a1.getP2() : a1.getP1();
            Point p2 = a2.getP1().equals(center) ? a2.getP2() : a2.getP1();

            double angle1 = Math.atan2(p1.getY() - center.getY(), p1.getX() - center.getX());
            double angle2 = Math.atan2(p2.getY() - center.getY(), p2.getX() - center.getX());
            double angleP = Math.atan2(p.getY() - center.getY(), p.getX() - center.getX());

            // Normalize to [0, 2π)
            angle1 = normalizeAngle(angle1);
            angle2 = normalizeAngle(angle2);
            angleP = normalizeAngle(angleP);

            // Step 4: Check if angleQ lies within the sector defined by angle1 -> angle2 (counter-clockwise)
            boolean inAngleRange = angleBetween(angle1, angle2, angleP);
            if (inAngleRange){
                return true;
            }
        }
        return false;
    }



    public static List<Point> intersectSegmentCircle(Segment s, Circle c) {
        Point s1 = s.getP1();
        Point s2 = s.getP2();

        Point center = c.getCenter();
        double radius = c.getRadius();

        double x1 = s1.getX(), y1 = s1.getY();
        double x2 = s2.getX(), y2 = s2.getY();
        double cx = center.getX(), cy = center.getY();

        // Translate to circle-centered coordinates
        double dx = x2 - x1;
        double dy = y2 - y1;
        double fx = x1 - cx;
        double fy = y1 - cy;

        double A = dx * dx + dy * dy;
        double B = 2 * (fx * dx + fy * dy);
        double C = fx * fx + fy * fy - radius * radius;

        double discriminant = B * B - 4 * A * C;
        List<Point> result = new ArrayList<>();

        if (discriminant < 0) {
            return result; // no intersection
        }

        double sqrtDiscriminant = Math.sqrt(discriminant);
        double t1 = (-B - sqrtDiscriminant) / (2 * A);
        double t2 = (-B + sqrtDiscriminant) / (2 * A);

        // Only keep points within the segment [0,1]
        if (t1 >= 0 && t1 <= 1) {
            double ix1 = x1 + t1 * dx;
            double iy1 = y1 + t1 * dy;
            result.add(new Point(ix1, iy1));
        }

        if (t2 >= 0 && t2 <= 1 && discriminant > 0) {
            double ix2 = x1 + t2 * dx;
            double iy2 = y1 + t2 * dy;
            result.add(new Point(ix2, iy2));
        }

        return result;
    }

    ///// end of intersect methods //////

    // given two segments, check if any other segments intersect
    // the segments that is formed betyween them
    // note that s1 and s2 share a common segment, that is the center


    // only check for sector emptiness, so line intersections
    // change q to a point

    // change inputs
    public static boolean isSectorEmpty(Segment s1, Segment s2, Point q){

        Point center;
        // determine what the center is
        if (s1.getP1().equals(s2.getP1()) || s1.getP1().equals(s2.getP2())){
            center = s1.getP1();
        } else{
            center = s1.getP2();
        }

       double radius = pointDistance(s1.getP1(), s1.getP2());
        
       if (pointDistance(center, q) > radius){
            return true;
        }

       // check if point is in angle range

        Point p1 = s1.getP1().equals(center) ? s1.getP2() : s1.getP1();
        Point p2 = s2.getP1().equals(center) ? s2.getP2() : s2.getP1();

        double angle1 = Math.atan2(p1.getY() - center.getY(), p1.getX() - center.getX());
        double angle2 = Math.atan2(p2.getY() - center.getY(), p2.getX() - center.getX());
        double angleQ = Math.atan2(q.getY() - center.getY(), q.getX() - center.getX());

        // normalize to [0, 2π)
        angle1 = normalizeAngle(angle1);
        angle2 = normalizeAngle(angle2);
        angleQ = normalizeAngle(angleQ);

        // Step 4: Check if angleQ lies within the sector defined by angle1 -> angle2 (counter-clockwise)
        boolean inAngleRange = angleBetween(angle1, angle2, angleQ);

        return !inAngleRange;
    }

    // Normalize to [0, 2π)
    public static double normalizeAngle(double angle) {
        return (angle + 2 * Math.PI) % (2 * Math.PI);
    }

    // Check if angleQ lies in counter-clockwise interval from angle1 to angle2
    public static boolean angleBetween(double angle1, double angle2, double angleQ) {
        // If angle2 is "before" angle1, wrap it around
        if (angle2 < angle1) {
            angle2 += 2 * Math.PI;
        }
        if (angleQ < angle1) {
            angleQ += 2 * Math.PI;
        }

        return angle1 <= angleQ && angleQ <= angle2;
    }

}
