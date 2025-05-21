
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
        Setup.main(target, obstacles);

        
        
    }
}
