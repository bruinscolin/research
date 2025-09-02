import java.util.ArrayList;
import java.util.List;

public class MidpointDistanceMinHeap {
    private List<Integer> heap; // Store segment IDs
    private Segment[] obstacles;
    private Point origin;
    private double startAngle;
    private double endAngle;

    public MidpointDistanceMinHeap(Point origin, Segment[] obstacles) {
        this.heap = new ArrayList<>();
        this.obstacles = obstacles;
        this.origin = origin;
        this.startAngle = 0;
        this.endAngle = 0;
    }

    // Set the angle range for midpoint calculation
    public void setAngleRange(double startAngle, double endAngle) {
        this.startAngle = startAngle;
        this.endAngle = endAngle;
    }

    // Calculate midpoint angle between start and end angles
    private double getMidpointAngle() {
        double midAngle = (startAngle + endAngle) / 2.0;

        // Handle wraparound case (e.g., start=5.5, end=0.5 should give midpoint=6.0 or
        // 0.0)
        if (endAngle < startAngle) {
            midAngle = (startAngle + endAngle + 2 * Math.PI) / 2.0;
            if (midAngle > 2 * Math.PI) {
                midAngle -= 2 * Math.PI;
            }
        }

        return midAngle;
    }

    // Calculate distance from origin to segment at the midpoint angle
    // private double getDistanceToSegment(int segmentId) {
    // if (segmentId < 0 || segmentId >= obstacles.length) {
    // return Double.MAX_VALUE;
    // }

    // Segment segment = obstacles[segmentId];
    // double midAngle = getMidpointAngle();

    // // Create ray from origin at midpoint angle
    // double rayDx = Math.cos(midAngle);
    // double rayDy = Math.sin(midAngle);

    // // Calculate ray-segment intersection distance
    // return calculateRaySegmentDistance(rayDx, rayDy, segment);
    // }
    private double getDistanceToSegment(int segmentId) {
        if (segmentId < 0 || segmentId >= obstacles.length) {
            return Double.MAX_VALUE;
        }

        Segment segment = obstacles[segmentId];

        // Sample several angles within the range
        int numSamples = 5;
        double minDistance = Double.MAX_VALUE;

        for (int i = 0; i < numSamples; i++) {
            double t = (double) i / (numSamples - 1); // 0 to 1
            double angle;

            if (endAngle < startAngle) {
                // Wraparound case
                if (t < 0.5) {
                    // First half: interpolate from startAngle to 2π
                    angle = startAngle + t * 2 * (2 * Math.PI - startAngle);
                    if (angle >= 2 * Math.PI)
                        angle -= 2 * Math.PI;
                } else {
                    // Second half: interpolate from 0 to endAngle
                    angle = (t - 0.5) * 2 * endAngle;
                }
            } else {
                // Normal case
                angle = startAngle + t * (endAngle - startAngle);
            }

            double rayDx = Math.cos(angle);
            double rayDy = Math.sin(angle);
            double distance = calculateRaySegmentDistance(rayDx, rayDy, segment);

            if (distance < minDistance) {
                minDistance = distance;
            }
        }

        return minDistance;
    }

    // Calculate distance from origin to ray-segment intersection
    // private double calculateRaySegmentDistance(double rayDx, double rayDy,
    // Segment seg) {
    // Point p1 = seg.getP1();
    // Point p2 = seg.getP2();

    // // Segment direction vector
    // double segDx = p2.getX() - p1.getX();
    // double segDy = p2.getY() - p1.getY();

    // // Vector from ray origin to segment start
    // double toSegDx = p1.getX() - origin.getX();
    // double toSegDy = p1.getY() - origin.getY();

    // // Solve parametric equations:
    // // origin + t * ray_direction = p1 + s * segment_direction
    // double denominator = rayDx * segDy - rayDy * segDx;
    // if (Math.abs(denominator) < 1e-10) {
    // return Double.MAX_VALUE; // Ray and segment are parallel
    // }

    // double t = (toSegDx * segDy - toSegDy * segDx) / denominator;
    // double s = (toSegDx * rayDy - toSegDy * rayDx) / denominator;

    // // Check if intersection is valid
    // if (t >= 1e-10 && s >= -1e-10 && s <= 1 + 1e-10) {
    // return t; // Distance along ray
    // }

    // return Double.MAX_VALUE; // No valid intersection
    // }

    // private double calculateRaySegmentDistance(double rayDx, double rayDy,
    // Segment seg) {
    // Point p1 = seg.getP1();
    // Point p2 = seg.getP2();

    // // Segment direction vector
    // double segDx = p2.getX() - p1.getX();
    // double segDy = p2.getY() - p1.getY();

    // // Vector from ray origin to segment start
    // double toSegDx = p1.getX() - origin.getX();
    // double toSegDy = p1.getY() - origin.getY();

    // // Solve parametric equations:
    // // origin + t * ray_direction = p1 + s * segment_direction
    // double denominator = rayDx * segDy - rayDy * segDx;
    // if (Math.abs(denominator) < 1e-10) {
    // System.out.println("DEBUG: Ray and segment are parallel - seg: " +
    // seg.getP1() + " to " + seg.getP2());
    // return Double.MAX_VALUE; // Ray and segment are parallel
    // }

    // double t = (toSegDx * segDy - toSegDy * segDx) / denominator;
    // double s = (toSegDx * rayDy - toSegDy * rayDx) / denominator;

    // System.out.println("DEBUG: Segment " + seg.getP1() + " to " + seg.getP2());
    // System.out.println(" Ray direction: (" + rayDx + ", " + rayDy + ") at angle "
    // + getMidpointAngle());
    // System.out.println(" t = " + t + ", s = " + s);
    // System.out.println(" t >= 1e-10: " + (t >= 1e-10));
    // System.out.println(" s >= -1e-10: " + (s >= -1e-10));
    // System.out.println(" s <= 1 + 1e-10: " + (s <= 1 + 1e-10));

    // // Check if intersection is valid
    // if (t >= 1e-10 && s >= -1e-10 && s <= 1 + 1e-10) {
    // System.out.println(" Valid intersection, distance = " + t);
    // return t; // Distance along ray
    // }

    // System.out.println(" No valid intersection, returning MAX_VALUE");
    // return Double.MAX_VALUE; // No valid intersection
    // }
    private double calculateRaySegmentDistance(double rayDx, double rayDy, Segment seg) {
        Point p1 = seg.getP1();
        Point p2 = seg.getP2();

        // Segment direction vector
        double segDx = p2.getX() - p1.getX();
        double segDy = p2.getY() - p1.getY();

        // Vector from ray origin to segment start
        double toSegDx = p1.getX() - origin.getX();
        double toSegDy = p1.getY() - origin.getY();

        // Solve parametric equations:
        // origin + t * ray_direction = p1 + s * segment_direction
        double denominator = rayDx * segDy - rayDy * segDx;
        if (Math.abs(denominator) < 1e-10) {
            return Double.MAX_VALUE; // Ray and segment are parallel
        }

        double t = (toSegDx * segDy - toSegDy * segDx) / denominator;
        double s = (toSegDx * rayDy - toSegDy * rayDx) / denominator;

        // Check if intersection is valid - allow small numerical errors
        if (t >= 1e-10) {
            // More lenient check for s parameter
            if (s >= -1e-6 && s <= 1 + 1e-6) {
                return t; // Distance along ray
            }

            // Also check if intersection point is very close to segment endpoints
            double intersectionX = origin.getX() + t * rayDx;
            double intersectionY = origin.getY() + t * rayDy;

            double dist1 = Math.sqrt(Math.pow(intersectionX - p1.getX(), 2) + Math.pow(intersectionY - p1.getY(), 2));
            double dist2 = Math.sqrt(Math.pow(intersectionX - p2.getX(), 2) + Math.pow(intersectionY - p2.getY(), 2));

            // If intersection is very close to either endpoint, consider it valid
            if (dist1 < 1e-6 || dist2 < 1e-6) {
                return t;
            }
        }

        return Double.MAX_VALUE; // No valid intersection
    }

    // Insert segment ID - O(log n)
    public void add(int segmentId) {
        if (segmentId < 0 || segmentId >= obstacles.length) {
            throw new IndexOutOfBoundsException("Invalid segment ID: " + segmentId);
        }

        heap.add(segmentId);
        heapifyUp(heap.size() - 1);
    }

    // Remove and return closest segment ID - O(log n)
    public int poll() {
        if (isEmpty()) {
            return -1;
        }

        int min = heap.get(0);
        int last = heap.remove(heap.size() - 1);

        if (!isEmpty()) {
            heap.set(0, last);
            heapifyDown(0);
        }

        return min;
    }

    // Get closest segment ID without removing - O(1)
    public int peek() {
        return isEmpty() ? -1 : heap.get(0);
    }

    // Remove specific segment ID - O(n) to find + O(log n) to remove
    public boolean remove(int segmentId) {
        int index = heap.indexOf(segmentId);
        if (index == -1) {
            return false;
        }

        int last = heap.remove(heap.size() - 1);

        if (index < heap.size()) {
            heap.set(index, last);
            // Try both directions
            heapifyUp(index);
            heapifyDown(index);
        }

        return true;
    }

    // Check if heap is empty - O(1)
    public boolean isEmpty() {
        return heap.isEmpty();
    }

    // Get size - O(1)
    public int size() {
        return heap.size();
    }

    // Clear all elements - O(1)
    public void clear() {
        heap.clear();
    }

    // Compare two segments by distance - returns true if seg1 is closer than seg2
    private boolean isCloser(int segmentId1, int segmentId2) {
        double dist1 = getDistanceToSegment(segmentId1);
        double dist2 = getDistanceToSegment(segmentId2);
        return dist1 < dist2;
    }

    // Maintain heap property by moving element up
    private void heapifyUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;

            if (!isCloser(heap.get(index), heap.get(parentIndex))) {
                break;
            }

            swap(index, parentIndex);
            index = parentIndex;
        }
    }

    // Maintain heap property by moving element down
    private void heapifyDown(int index) {
        int size = heap.size();

        while (true) {
            int closest = index;
            int leftChild = 2 * index + 1;
            int rightChild = 2 * index + 2;

            if (leftChild < size && isCloser(heap.get(leftChild), heap.get(closest))) {
                closest = leftChild;
            }

            if (rightChild < size && isCloser(heap.get(rightChild), heap.get(closest))) {
                closest = rightChild;
            }

            if (closest == index) {
                break;
            }

            swap(index, closest);
            index = closest;
        }
    }

    // Swap two elements in the heap
    private void swap(int i, int j) {
        int temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    // Debug method to print heap contents
    public void printHeap() {
        System.out.println("Heap contents (size=" + size() + ", midpoint angle=" + getMidpointAngle() + "):");
        for (int i = 0; i < heap.size(); i++) {
            int segId = heap.get(i);
            double dist = getDistanceToSegment(segId);
            System.out.println("  [" + i + "] Segment " + segId + " with distance " + dist);
        }
    }
}

// Example usage:
/*
 * Point origin = new Point(0, 0);
 * Segment[] obstacles = {
 * new Segment(new Point(5, 3), new Point(2, 5)),
 * new Segment(new Point(-3, 5), new Point(-3, 1))
 * };
 * 
 * MidpointDistanceMinHeap heap = new MidpointDistanceMinHeap(origin,
 * obstacles);
 * 
 * // Set the angle range you're interested in
 * heap.setAngleRange(0.5, 1.2); // Radians
 * 
 * heap.add(0); // Add segment ID 0
 * heap.add(1); // Add segment ID 1
 * 
 * int closestSegment = heap.peek(); // Get closest segment ID
 * System.out.println("Closest segment: " + closestSegment);
 */
