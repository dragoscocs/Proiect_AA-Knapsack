// Obiect pentru rucsac - greutate, valoare si index
public record Item(int weight, int value, int index) implements Comparable<Item> {

    // Raportul valoare/greutate pt greedy
    public double ratio() {
        return (double) value / weight;
    }

    // Sortam descrescator dupa ratio
    @Override
    public int compareTo(Item other) {
        return Double.compare(other.ratio(), this.ratio());
    }
}
