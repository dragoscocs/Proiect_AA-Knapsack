/**
 * Reprezintă un obiect pentru problema rucsacului.
 * Folosim record (Java 16+) pentru imutabilitate și cod concis.
 * 
 * @param weight Greutatea obiectului
 * @param value Valoarea obiectului
 * @param index Indexul original al obiectului în lista de intrare
 */
public record Item(int weight, int value, int index) implements Comparable<Item> {
    
    /**
     * Calculează raportul valoare/greutate (folosit pentru Greedy și pruning).
     * @return Raportul value/weight
     */
    public double ratio() {
        return (double) value / weight;
    }
    
    /**
     * Comparare descrescătoare după raportul value/weight.
     * Folosit pentru sortarea obiectelor în algoritmul Greedy și Backtracking.
     */
    @Override
    public int compareTo(Item other) {
        return Double.compare(other.ratio(), this.ratio()); // Descrescător
    }
    
    @Override
    public String toString() {
        return String.format("Item[index=%d, weight=%d, value=%d, ratio=%.2f]", 
                             index, weight, value, ratio());
    }
}
