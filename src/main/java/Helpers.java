import java.util.ArrayList;
import java.util.List;

public class Helpers {
    // given center t and a line (or ray)
    // find the radius of a circle that is
    // tangent to the line
    public static double foo(Point circle_center, Ray ray) {
        double radius = Math.abs(
                ((-1 * Math.sin(ray.getAngle())) *
                        (circle_center.getX() - ray.getX()))
                        +
                        (Math.cos(ray.getAngle()) * (circle_center.getY() - ray.getY())));
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
    public static double orientationTest(Point s1, Point s2, Point q) {
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
    public static double pointDistance(Point p1, Point p2) {
        double x_dist = Math.abs(p1.getX() - p2.getX());
        double y_dist = Math.abs(p1.getY() - p2.getY());

        double dist = Math.sqrt((x_dist * x_dist) + (y_dist * y_dist));
        return dist;
    }

    ///// intersect methods //////
    // public static boolean segment_segment_intersect(Segment s1, Segment s2){
    // if (( orientationTest(s1.getP1(), s1.getP2(), s2.getP1()) >= 0 &&
    // orientationTest(s1.getP1(), s1.getP2(), s2.getP2()) >= 0 ) ||
    // ( orientationTest(s1.getP1(), s1.getP2(), s2.getP1()) <= 0 &&
    // orientationTest(s1.getP1(), s1.getP2(), s2.getP2()) <= 0 )){
    // return false;
    // }
    // return true;
    //
    // }

    public static boolean segment_segment_intersect(Segment s1, Segment s2) {
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
        if (o1 == 0 && o2 == 0 && o3 == 0 && o4 == 0) {
            return doCollinearSegmentsOverlap(s1, s2);
        }
        // Optionally: handle collinear + overlapping edge cases here if needed
        return false;
    }

    // Helper method to check if collinear segments overlap (excluding just touching
    // at endpoints)
    private static boolean doCollinearSegmentsOverlap(Segment s1, Segment s2) {
        Point p1 = s1.getP1();
        Point q1 = s1.getP2();
        Point p2 = s2.getP1();
        Point q2 = s2.getP2();

        // Check overlap in x-direction
        double s1_min_x = Math.min(p1.getX(), q1.getX());
        double s1_max_x = Math.max(p1.getX(), q1.getX());
        double s2_min_x = Math.min(p2.getX(), q2.getX());
        double s2_max_x = Math.max(p2.getX(), q2.getX());

        // Check overlap in y-direction
        double s1_min_y = Math.min(p1.getY(), q1.getY());
        double s1_max_y = Math.max(p1.getY(), q1.getY());
        double s2_min_y = Math.min(p2.getY(), q2.getY());
        double s2_max_y = Math.max(p2.getY(), q2.getY());

        // Segments overlap if they overlap in both x and y directions
        // Using > instead of >= to exclude just touching at endpoints
        boolean x_overlap = (s1_max_x > s2_min_x) && (s2_max_x > s1_min_x);
        boolean y_overlap = (s1_max_y > s2_min_y) && (s2_max_y > s1_min_y);

        // Special case: if both segments are on the same line (horizontal or vertical)
        // we only need to check the dimension that varies
        if (s1_min_y == s1_max_y && s2_min_y == s2_max_y && s1_min_y == s2_min_y) {
            // Both segments are horizontal on the same line
            return x_overlap;
        }
        if (s1_min_x == s1_max_x && s2_min_x == s2_max_x && s1_min_x == s2_min_x) {
            // Both segments are vertical on the same line
            return y_overlap;
        }

        return x_overlap && y_overlap;
    }

    // USE ORIENTATIONTEST FOR ARC CHECK
    public static boolean arc_segment_intersect(Segment a1, Segment a2, Segment q) {

        double radius = pointDistance(a1.getP1(), a1.getP2());

        Point center;
        // determine what the center is
        if (a1.getP1().equals(a2.getP1()) || a1.getP1().equals(a2.getP2())) {
            center = a1.getP1();
        } else {
            center = a1.getP2();
        }

        Circle c = new Circle(center, radius);

        // find intersection(s)
        List<Point> intersections = intersectSegmentCircle(q, c);

        for (Point p : intersections) {
            p.print();

            if (p.equals(q.getP1()) || p.equals(q.getP2())) {
                continue; // skip endpoints
            }
            Point p1 = a1.getP1().equals(center) ? a1.getP2() : a1.getP1();
            Point p2 = a2.getP1().equals(center) ? a2.getP2() : a2.getP1();

            double angle1 = Math.atan2(p1.getY() - center.getY(), p1.getX() - center.getX());
            double angle2 = Math.atan2(p2.getY() - center.getY(), p2.getX() - center.getX());
            double angleP = Math.atan2(p.getY() - center.getY(), p.getX() - center.getX());

            // Normalize to [0, 2π)
            angle1 = normalizeAngle(angle1);
            angle2 = normalizeAngle(angle2);
            angleP = normalizeAngle(angleP);

            // Step 4: Check if angleQ lies within the sector defined by angle1 -> angle2
            // (counter-clockwise)
            boolean inAngleRange = angleBetween(angle1, angle2, angleP);
            if (inAngleRange) {
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
        if (t1 > 0 && t1 < 1) {
            double ix1 = x1 + t1 * dx;
            double iy1 = y1 + t1 * dy;
            result.add(new Point(ix1, iy1));
        }

        if (t2 > 0 && t2 < 1 && discriminant > 0) {
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
    public static boolean isSectorEmpty(Segment s1, Segment s2, Point q) {

        Point center;
        // determine what the center is
        if (s1.getP1().equals(s2.getP1()) || s1.getP1().equals(s2.getP2())) {
            center = s1.getP1();
        } else {
            center = s1.getP2();
        }

        double radius = pointDistance(s1.getP1(), s1.getP2());

        if (pointDistance(center, q) > radius) {
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

        // Step 4: Check if angleQ lies within the sector defined by angle1 -> angle2
        // (counter-clockwise)
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

        return angle1 < angleQ && angleQ < angle2;
    }

    public static Point getSegmentSegmentIntersectPoint(Segment s1, Segment s2) {
        // segment 1 points
        double s1x1 = s1.getX1();
        double s1y1 = s1.getY1();
        double s1x2 = s1.getX2();
        double s1y2 = s1.getY2();

        // segment 2 points
        double s2x1 = s2.getX1();
        double s2y1 = s2.getY1();
        double s2x2 = s2.getX2();
        double s2y2 = s2.getY2();

        double slope1 = (s1y2 - s1y1) / (s1x2 - s1x1);
        double slope2 = (s2y2 - s2y1) / (s2x2 - s2x1);

        double y1_intercept = s1y1 - slope1 * s1x1;
        double y2_intercept = s2y1 - slope2 * s2x1;

        // set equations equal to each other
        // m1x + b1 = m2x + b2

        double x = (y2_intercept - y2_intercept) / (slope1 - slope2);
        double y = (slope1 * x) + y1_intercept;

        return new Point(x, y);
    }

}
