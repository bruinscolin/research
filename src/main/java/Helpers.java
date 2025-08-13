import java.util.ArrayList;
import java.util.List;
import java.util.*;

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

        return false;
    }

    // Helper method to check if collinear segments overlap (excluding just touching
    // at endpoints)

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

        // Check if either line is vertical
        boolean s1Vertical = Math.abs(s1x2 - s1x1) < 1e-10;
        boolean s2Vertical = Math.abs(s2x2 - s2x1) < 1e-10;

        if (s1Vertical && s2Vertical) {
            // if both are vertical
            // won't happen if only called with intersect function
            return null;
        } else if (s1Vertical) {
            // First line is vertical: x = s1x1
            double slope2 = (s2y2 - s2y1) / (s2x2 - s2x1);
            double y2_intercept = s2y1 - slope2 * s2x1;
            double x = s1x1;
            double y = slope2 * x + y2_intercept;
            return new Point(x, y);
        } else if (s2Vertical) {
            // Second line is vertical: x = s2x1
            double slope1 = (s1y2 - s1y1) / (s1x2 - s1x1);
            double y1_intercept = s1y1 - slope1 * s1x1;
            double x = s2x1;
            double y = slope1 * x + y1_intercept;
            return new Point(x, y);
        }

        double slope1 = (s1y2 - s1y1) / (s1x2 - s1x1);
        double slope2 = (s2y2 - s2y1) / (s2x2 - s2x1);

        double y1_intercept = s1y1 - slope1 * s1x1;
        double y2_intercept = s2y1 - slope2 * s2x1;

        // set equations equal to each other
        // m1x + b1 = m2x + b2

        double x = (y2_intercept - y1_intercept) / (slope1 - slope2);
        double y = (slope1 * x) + y1_intercept;

        return new Point(x, y);
    }

    public static boolean isPointInTriangle(Point p, Point a, Point b, Point c) {

        // a = v, b = t, c = bp

        double or1 = orientationTest(a, b, p);
        double or2 = orientationTest(c, a, p);
        double or3 = orientationTest(b, c, p);

        return (or1 >= 0 && or2 >= 0 && or3 >= 0) || (or1 <= 0 && or2 <= 0 && or3 <= 0);

    }

    //// pasted //////

    // Helper method to find the first tangency point as circle slides along ray
    public static Point findFirstTangencyAlongRay(Point u, Point v, Point t, Segment obstacle,
            double ray_angle, Point bp, Segment bpcp, Segment bpt) {

        Point p1 = obstacle.getP1();
        Point p2 = obstacle.getP2();

        // Ray direction vector
        double dx = v.getX() - u.getX();
        double dy = v.getY() - u.getY();
        double ray_length = Math.sqrt(dx * dx + dy * dy);
        dx /= ray_length;
        dy /= ray_length;

        // Find all potential tangency points and select the closest one to u (earliest
        // along ray)
        Point earliest_center = null;
        double earliest_t_param = Double.MAX_VALUE;

        // Case 1: Check if circle can pass through an endpoint and target t
        Point endpoint_center1 = findCenterPassingThroughTwoPoints(u, dx, dy, t, p1);
        Point endpoint_center2 = findCenterPassingThroughTwoPoints(u, dx, dy, t, p2);

        if (endpoint_center1 != null) {
            double t_param1 = getTParamOnRay(u, dx, dy, endpoint_center1);
            if (t_param1 > 0 && t_param1 < earliest_t_param) {
                // Verify the endpoint is actually in the sector when this circle is at this
                // position
                if (isValidTangencyConfiguration(endpoint_center1, t, p1, bpcp, bpt, obstacle)) {
                    earliest_t_param = t_param1;
                    earliest_center = endpoint_center1;
                }
            }
        }

        if (endpoint_center2 != null) {
            double t_param2 = getTParamOnRay(u, dx, dy, endpoint_center2);
            if (t_param2 > 0 && t_param2 < earliest_t_param) {
                // Verify the endpoint is actually in the sector when this circle is at this
                // position
                if (isValidTangencyConfiguration(endpoint_center2, t, p2, bpcp, bpt, obstacle)) {
                    earliest_t_param = t_param2;
                    earliest_center = endpoint_center2;
                }
            }
        }

        // Case 2: Check if circle can be tangent to the line segment
        Point tangent_center = findTangentCenterToSegment(u, dx, dy, t, obstacle);

        if (tangent_center != null) {
            double t_param_tangent = getTParamOnRay(u, dx, dy, tangent_center);
            if (t_param_tangent > 0 && t_param_tangent < earliest_t_param) {
                // Verify the tangency point is on the actual segment and configuration is valid
                if (isValidTangencyToSegment(tangent_center, t, obstacle, bpcp, bpt)) {
                    earliest_t_param = t_param_tangent;
                    earliest_center = tangent_center;
                }
            }
        }

        return earliest_center;
    }

    // Find center on ray such that circle passes through two given points
    public static Point findCenterPassingThroughTwoPoints(Point u, double dx, double dy, Point t, Point endpoint) {
        // Circle center is at u + t_param * (dx, dy)
        // Circle passes through both t and endpoint
        // So distance(center, t) = distance(center, endpoint)

        // Let center = (u.x + t*dx, u.y + t*dy)
        // distance²(center, t) = (u.x + t*dx - t.x)² + (u.y + t*dy - t.y)²
        // distance²(center, endpoint) = (u.x + t*dx - endpoint.x)² + (u.y + t*dy -
        // endpoint.y)²

        // Setting them equal and solving for t:
        double tx = t.getX() - u.getX();
        double ty = t.getY() - u.getY();
        double ex = endpoint.getX() - u.getX();
        double ey = endpoint.getY() - u.getY();

        // Expand the equation: (tx - t*dx)² + (ty - t*dy)² = (ex - t*dx)² + (ey -
        // t*dy)²
        // tx² - 2*tx*t*dx + t²*dx² + ty² - 2*ty*t*dy + t²*dy² = ex² - 2*ex*t*dx +
        // t²*dx² + ey² - 2*ey*t*dy + t²*dy²
        // tx² + ty² - 2*t*(tx*dx + ty*dy) = ex² + ey² - 2*t*(ex*dx + ey*dy)
        // tx² + ty² - ex² - ey² = 2*t*(tx*dx + ty*dy - ex*dx - ey*dy)
        // tx² + ty² - ex² - ey² = 2*t*((tx-ex)*dx + (ty-ey)*dy)

        double numerator = tx * tx + ty * ty - ex * ex - ey * ey;
        double denominator = 2 * ((tx - ex) * dx + (ty - ey) * dy);

        if (Math.abs(denominator) < 1e-10) {
            return null; // No solution or infinite solutions
        }

        double t_param = numerator / denominator;

        if (t_param <= 0) {
            return null; // Center would be behind or at the ray origin
        }

        double center_x = u.getX() + t_param * dx;
        double center_y = u.getY() + t_param * dy;

        return new Point(center_x, center_y);
    }

    // Find center on ray such that circle is tangent to line segment
    public static Point findTangentCenterToSegment(Point u, double dx, double dy, Point t, Segment obstacle) {
        // dx,dy expected normalized (unit vector)
        final double EPS = 1e-9;
        final double EPS_MU = 1e-9; // tolerance for projection inside segment
        final double EPS_S = 1e-9; // tolerance for ray parameter s > 0

        Point a = obstacle.getP1();
        Point b = obstacle.getP2();

        // segment vector w and squared length
        double wx = b.getX() - a.getX();
        double wy = b.getY() - a.getY();
        double w2 = wx * wx + wy * wy;
        if (w2 < EPS)
            return null; // degenerate segment -> skip (you handle endpoints elsewhere)

        // vectors E = u - a, F = u - t
        double Ex = u.getX() - a.getX();
        double Ey = u.getY() - a.getY();
        double Fx = u.getX() - t.getX();
        double Fy = u.getY() - t.getY();

        // useful dot products
        double alpha0 = Ex * wx + Ey * wy; // E·w
        double alpha1 = dx * wx + dy * wy; // d·w
        double D = (Fx * Fx + Fy * Fy) - (Ex * Ex + Ey * Ey); // F·F - E·E
        double L = dx * (Fx - Ex) + dy * (Fy - Ey); // d·(F - E)

        // Quadratic coefficients (A s^2 + B s + C = 0)
        double A = alpha1 * alpha1;
        double B = 2.0 * (w2 * L + alpha0 * alpha1);
        double C = w2 * D + alpha0 * alpha0;

        // Solve quadratic (or linear if A ~ 0)
        double bestS = Double.POSITIVE_INFINITY;
        boolean found = false;

        if (Math.abs(A) < EPS) {
            // linear B s + C = 0
            if (Math.abs(B) > EPS) {
                double s = -C / B;
                if (s > EPS_S) {
                    // check projection inside segment
                    double cx = u.getX() + s * dx;
                    double cy = u.getY() + s * dy;
                    double mu = ((cx - a.getX()) * wx + (cy - a.getY()) * wy) / w2;
                    if (mu >= -EPS_MU && mu <= 1.0 + EPS_MU) {
                        if (s < bestS) {
                            bestS = s;
                            found = true;
                        }
                    }
                }
            }
        } else {
            double disc = B * B - 4.0 * A * C;
            if (disc >= -EPS) {
                if (disc < 0)
                    disc = 0;
                double sqrtD = Math.sqrt(disc);
                double s1 = (-B + sqrtD) / (2.0 * A);
                double s2 = (-B - sqrtD) / (2.0 * A);
                // check both roots
                double[] roots = { s1, s2 };
                for (double s : roots) {
                    if (s > EPS_S) {
                        double cx = u.getX() + s * dx;
                        double cy = u.getY() + s * dy;
                        double mu = ((cx - a.getX()) * wx + (cy - a.getY()) * wy) / w2;
                        if (mu >= -EPS_MU && mu <= 1.0 + EPS_MU) {
                            if (s < bestS) {
                                bestS = s;
                                found = true;
                            }
                        }
                    }
                }
            }
        }

        if (!found)
            return null;

        // return center point for bestS
        double centerX = u.getX() + bestS * dx;
        double centerY = u.getY() + bestS * dy;
        return new Point(centerX, centerY);
    }

    // Get parameter t for a point on the ray
    public static double getTParamOnRay(Point u, double dx, double dy, Point point) {
        // point = u + t * (dx, dy)
        // Solve for t using the component with larger absolute value for numerical
        // stability
        if (Math.abs(dx) > Math.abs(dy)) {
            return (point.getX() - u.getX()) / dx;
        } else {
            return (point.getY() - u.getY()) / dy;
        }
    }

    // Check if center creates valid tangency configuration with endpoint
    public static boolean isValidTangencyConfiguration(Point center, Point t, Point endpoint,
            Segment bpcp, Segment bpt, Segment obstacle) {
        // The endpoint should be in the sector defined by the current b'c' and b't
        if (Helpers.isSectorEmpty(bpcp, bpt, endpoint)) {
            return false; // Endpoint not in sector
        }

        // Additional checks can be added here for other validity conditions
        return true;
    }

    // Check if center creates valid tangency to segment
    public static boolean isValidTangencyToSegment(Point center, Point t, Segment obstacle,
            Segment bpcp, Segment bpt) {
        // Find the tangent point on the segment
        Point tangent_point = findTangentPointOnSegment(center, obstacle);

        if (tangent_point == null)
            return false;

        // Check if tangent point is in the sector
        if (Helpers.isSectorEmpty(bpcp, bpt, tangent_point)) {
            return false; // Tangent point not in sector
        }

        return true;
    }

    // Check if circle centered at given point is tangent to segment
    public static boolean isTangentToSegment(Point center, Segment obstacle) {
        Point tangent_point = findTangentPointOnSegment(center, obstacle);
        if (tangent_point == null)
            return false;

        // Check if the tangent point is actually on the segment (not just the infinite
        // line)
        return isPointOnSegment(tangent_point, obstacle);
    }

    // Find the point of tangency between circle and segment
    public static Point findTangentPointOnSegment(Point center, Segment obstacle) {
        Point p1 = obstacle.getP1();
        Point p2 = obstacle.getP2();

        double seg_dx = p2.getX() - p1.getX();
        double seg_dy = p2.getY() - p1.getY();
        double seg_length_sq = seg_dx * seg_dx + seg_dy * seg_dy;

        if (seg_length_sq < 1e-10)
            return null;

        // Find closest point on segment to center
        double t = ((center.getX() - p1.getX()) * seg_dx + (center.getY() - p1.getY()) * seg_dy) / seg_length_sq;
        t = Math.max(0, Math.min(1, t)); // Clamp to segment

        double closest_x = p1.getX() + t * seg_dx;
        double closest_y = p1.getY() + t * seg_dy;

        return new Point(closest_x, closest_y);
    }

    // Check if point lies on segment
    public static boolean isPointOnSegment(Point point, Segment segment) {
        Point p1 = segment.getP1();
        Point p2 = segment.getP2();

        double seg_dx = p2.getX() - p1.getX();
        double seg_dy = p2.getY() - p1.getY();
        double seg_length_sq = seg_dx * seg_dx + seg_dy * seg_dy;

        if (seg_length_sq < 1e-10)
            return false;

        double t = ((point.getX() - p1.getX()) * seg_dx + (point.getY() - p1.getY()) * seg_dy) / seg_length_sq;

        return t >= -1e-10 && t <= 1.0 + 1e-10; // Allow small numerical tolerance
    }

    /// visibility polygon methods ///

    // takes in origin point, list of cartesian endpoints
    // returns polar endpoints in sorted CCW order
    public static List<PolarPoint> preProcessPolygon(Point origin, List<Point> endpoints) {

        // convert all to polars with respect to GIVEN origin
        List<PolarPoint> polars = new ArrayList<>();

        for (Point p : endpoints) {
            polars.add(p.toPolar(origin));

        }

        // find closest point
        PolarPoint closest_point = new PolarPoint(Double.POSITIVE_INFINITY, 0);

        for (PolarPoint p : polars) {
            if (p.getR() < closest_point.getR()) {
                closest_point = new PolarPoint(p.getR(), p.getAngle());
            }
        }

        // align closest point on X-axis

        double angle_difference = closest_point.getAngle();

        // adjust all points
        for (PolarPoint p : polars) {
            p.setAngle(p.getAngle() - angle_difference);
        }
        closest_point.setAngle(0);

        // sort them by CCW
        Collections.sort(polars, (p1, p2) -> Double.compare(p1.getAngle(), p2.getAngle()));
        return polars;
    }

    public static List<PolarPoint> ProcessPolygon(List<PolarPoint> polars) {
        List<PolarPoint> stack = new ArrayList<>();

        for (PolarPoint p : polars) {
            if (stack.isEmpty()) {
                stack.add(p);

            }
            PolarPoint top = stack.remove(stack.size() - 1);

            // if new point has same angle as top
            if (top.getAngle() == p.getAngle()) {
                if (top.getR() > p.getR()) {
                    stack.add(p);

                } else {
                    stack.add(top);

                }
            } else {
                // different angle, add to stack
                stack.add(top);
                stack.add(p);

            }
        }

        return stack;
    }

}
