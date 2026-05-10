public class Point implements Comparable {
    private double x, y;

    public Point(double newX, double newY) {
        this.x = newX;
        this.y = newY;
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean lessThan(Order other) {
        if (this.getClass() == other.getClass()) {
            return lessThan((Point) other);
        }
        return false;
    }

    public boolean lessThan(Point other) {
        if (this.x > other.getX()) {
            return false;
        }
        else if (this.y > other.getY()) {
            return false;
        }
        else if (this.equals(other)) {
            return false;
        }
        return true;
    }

    public boolean equals(Point other) {
        if (this.x == other.getX() && this.y == other.getY()) {
            return true;
        }
        else {
            return false;
        }
    }

    public int compareTo(Object other) {
        Point newP = (Point) other;
        if (lessThan(newP)) {
            return -1;
        }
        else if (equals(newP)) {
            return 0;
        }
        return 1;
    }

    public static void main(String[] args) {
        Order O1 = new Point(0, 0);
        Order O2 = new Point(1, 1);
        Order O3 = new Point(0, 1);
        Order O4 = new Point(0, 1);
        Point P4 = new Point(0, 1);
        Point P5 = new Point(0, 1);
        Comparable C1 = new Point(0, 1);
        Comparable C2 = new Point(0, 1);
        

        System.out.println("O1 less than O2: " + O1.lessThan(O2)); // true
        System.out.println("O1 less than O3: " + O1.lessThan(O3)); // true
        System.out.println("O2 less than O3: " + O2.lessThan(O3)); // false
        System.out.println("O3 less than O3: " + O3.lessThan(O3)); // false

        System.out.println("O3 == O4: " + O3.equals(O4));
        System.out.println("P4 == P5: " + P4.equals(P5));
        System.out.println("C1 == C2: " + C1.equals(C2));

        Comparable P1 = new Point(0, 0);
        Comparable P2 = new Point(1, 1);
        Comparable P3 = new Point(0, 1);

        System.out.println("P1 less than P2: " + P1.compareTo(P2)); // -1
        System.out.println("P1 less than P3: " + P1.compareTo(P3)); // -1
        System.out.println("P2 less than P3: " + P2.compareTo(P3)); // 1
        System.out.println("P3 less than P3: " + P3.compareTo(P3)); // 0
    }
}