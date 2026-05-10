public class DuckFactory {
    private static final int capacity = 100;
    private static int counter;

    public static Duck getNewDuck() {
        if (counter < capacity) {
            counter++;
            return new Duck();
        }
        return null;
    }
}
