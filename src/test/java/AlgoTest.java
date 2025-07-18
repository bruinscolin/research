import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class SegmentIntersectTest {

    @Test
    void no_intersects() {

        Segment h = new Segment(new Point(-500, 0), new Point(500, 0));

        Segment[] do_not_intersect = {
                new Segment(new Point(-230, -125), new Point(-100, -250)),
                new Segment(new Point(50, 100), new Point(150, 200)),
                new Segment(new Point(-80, 75), new Point(-20, 120)),
                new Segment(new Point(200, 300), new Point(350, 250)),
                new Segment(new Point(-150, 180), new Point(-50, 160)),
                new Segment(new Point(10, 50), new Point(80, 90)),
                new Segment(new Point(-300, -80), new Point(-200, -150)),
                new Segment(new Point(120, 170), new Point(180, 140)),
                new Segment(new Point(-40, 220), new Point(30, 280)),
                new Segment(new Point(250, 400), new Point(320, 350))
        };

        // Segment a = new Segment(new Point(0, 100), new Point(0, -100));
        // Segment b = new Segment(new Point(100, 0), new Point(-100, 0));
        // Helpers Helpers = new Helpers();
        for (Segment s : do_not_intersect) {
            assertFalse(Helpers.segment_segment_intersect(h, s));

        }
        // assertTrue(Helpers.segment_segment_intersect(a, b));

    }

    @Test
    void only_intersects() {

        Segment h = new Segment(new Point(-500, 0), new Point(500, 0));

        Segment[] do_inersect = {
                new Segment(new Point(-200, 150), new Point(-50, -100)),
                new Segment(new Point(100, -80), new Point(250, 120)),
                new Segment(new Point(-150, -200), new Point(-30, 180)),
                new Segment(new Point(50, 300), new Point(180, -150)),
                new Segment(new Point(-300, 90), new Point(-100, -60)),
                new Segment(new Point(20, -250), new Point(120, 200)),
                new Segment(new Point(-80, 160), new Point(40, -80)),
                new Segment(new Point(200, -100), new Point(350, 250)),
                new Segment(new Point(-250, -300), new Point(-150, 100)),
                new Segment(new Point(80, 400), new Point(220, -200))

        };

        for (Segment s : do_inersect) {
            assertTrue(Helpers.segment_segment_intersect(h, s));

        }
    }

    @Test
    void endpoint_intersect() {

        Segment h = new Segment(new Point(-500, 0), new Point(500, 0));

        Segment[] one_endpoint_intersects = {
                new Segment(new Point(-150, 0), new Point(-50, 120)),
                new Segment(new Point(100, -80), new Point(200, 0)),
                new Segment(new Point(-80, 110), new Point(50, 0)),
                new Segment(new Point(0, 150), new Point(120, 0)),
                new Segment(new Point(-200, 0), new Point(-100, -100)),
        };

        Segment[] both_endpoint_intersect = {
                new Segment(new Point(-200, 0), new Point(-100, 0)),
                new Segment(new Point(50, 0), new Point(150, 0)),
                new Segment(new Point(-80, 0), new Point(20, 0)),
                new Segment(new Point(180, 0), new Point(300, 0)),
                new Segment(new Point(-350, 0), new Point(-250, 0)),

                new Segment(new Point(-550, 0), new Point(-400, 0))
        };

        for (Segment s : one_endpoint_intersects) {
            assertFalse(Helpers.segment_segment_intersect(s, h));

        }

        for (Segment s : both_endpoint_intersect) {
            assertTrue(Helpers.segment_segment_intersect(s, h));
        }

    }
}

// class CircleSegmentIntersect {
// @Test
// void fully_inside_circle() {

// Circle c = new Circle(new Point(0, 0), 100);

// Segment[] inside_circle = {
// new Segment(new Point(-50, 0), new Point(50, 0)),
// new Segment(new Point(0, -50), new Point(0, 50)),
// new Segment(new Point(-30, -30), new Point(30, 30)),
// new Segment(new Point(-30, 30), new Point(30, -30)),
// new Segment(new Point(-60, 20), new Point(-20, 60)),
// new Segment(new Point(20, -60), new Point(60, -20)),
// new Segment(new Point(-40, -50), new Point(-10, -70)),
// new Segment(new Point(25, 35), new Point(55, 65)),
// new Segment(new Point(-70, 10), new Point(-45, 40)),
// new Segment(new Point(15, -80), new Point(45, -55)),

// };

// for (Segment s : inside_circle) {
// // assertEmpty(Helpers.intersectSegmentCircle(s, c));
// }

// }

// }
