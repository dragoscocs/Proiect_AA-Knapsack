import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Implementează trei algoritmi pentru rezolvarea problemei rucsacului (0/1
 * Knapsack):
 * 1. Backtracking cu pruning (soluție exactă)
 * 2. Programare Dinamică (soluție optimă)
 * 3. Greedy (soluție aproximativă)
 */
public class KnapsackSolver {

    private final int[] weights;
    private final int[] values;
    private final int capacity;
    private final int n;

    // Variabile pentru Backtracking
    private int maxValue;
    private List<Integer> bestCombination;
    private Item[] sortedItems;

    /**
     * Constructor.
     * 
     * @param weights  Array cu greutățile obiectelor
     * @param values   Array cu valorile obiectelor
     * @param capacity Capacitatea rucsacului
     */
    public KnapsackSolver(int[] weights, int[] values, int capacity) {
        this.weights = weights;
        this.values = values;
        this.capacity = capacity;
        this.n = weights.length;
    }

    // ==================== BACKTRACKING ====================

    /**
     * Rezolvă problema rucsacului folosind Backtracking cu pruning.
     * Soluție exactă, dar poate fi lentă pentru instanțe mari.
     * 
     * Complexitate timp: O(2^n) worst case, dar eficient cu pruning
     * Complexitate spațiu: O(n) pentru stiva de recursie
     * 
     * @return KnapsackResult cu valoarea maximă și obiectele selectate
     */
    public KnapsackResult solveBacktracking() {
        maxValue = 0;
        bestCombination = new ArrayList<>();

        // Sortăm obiectele după raportul value/weight (descrescător)
        sortedItems = new Item[n];
        for (int i = 0; i < n; i++) {
            sortedItems[i] = new Item(weights[i], values[i], i);
        }
        Arrays.sort(sortedItems);

        backtrack(0, 0, 0, new ArrayList<>());

        // Convertim înapoi la indicii originali
        List<Integer> result = new ArrayList<>();
        for (int idx : bestCombination) {
            result.add(sortedItems[idx].index());
        }
        Collections.sort(result);

        return new KnapsackResult(maxValue, result);
    }

    /**
     * Estimează valoarea maximă posibilă de la indexul curent (upper bound).
     * Folosit pentru pruning - dacă upper bound <= maxValue curent, tăiem ramura.
     */
    private double estimateMaxValue(int index, int currentWeight, int currentValue) {
        int remainingCapacity = capacity - currentWeight;
        double maxPossibleValue = currentValue;

        for (int i = index; i < n; i++) {
            int w = sortedItems[i].weight();
            int v = sortedItems[i].value();

            if (w <= remainingCapacity) {
                maxPossibleValue += v;
                remainingCapacity -= w;
            } else {
                // Adăugăm fracțional pentru estimare (relaxare continuă)
                maxPossibleValue += (double) v * remainingCapacity / w;
                break;
            }
        }
        return maxPossibleValue;
    }

    /**
     * Funcția recursivă de backtracking.
     */
    private void backtrack(int index, int currentWeight, int currentValue,
            List<Integer> currentCombination) {
        // Verificăm dacă am depășit capacitatea
        if (currentWeight > capacity) {
            return;
        }

        // Actualizăm cea mai bună soluție dacă e cazul
        if (currentValue > maxValue) {
            maxValue = currentValue;
            bestCombination = new ArrayList<>(currentCombination);
        }

        // Pruning: verificăm dacă merită să continuăm
        if (index >= n || estimateMaxValue(index, currentWeight, currentValue) <= maxValue) {
            return;
        }

        // Includem obiectul curent
        currentCombination.add(index);
        backtrack(index + 1,
                currentWeight + sortedItems[index].weight(),
                currentValue + sortedItems[index].value(),
                currentCombination);
        currentCombination.remove(currentCombination.size() - 1);

        // Nu includem obiectul curent
        backtrack(index + 1, currentWeight, currentValue, currentCombination);
    }

    // ==================== PROGRAMARE DINAMICĂ ====================

    /**
     * Rezolvă problema rucsacului folosind Programare Dinamică.
     * Soluție optimă garantată.
     * 
     * Complexitate timp: O(n × W)
     * Complexitate spațiu: O(n × W)
     * 
     * @return KnapsackResult cu valoarea maximă și obiectele selectate
     */
    public KnapsackResult solveDynamicProgramming() {
        // Tabelul DP: dp[i][w] = valoarea maximă folosind primele i obiecte cu
        // capacitate w
        int[][] dp = new int[n + 1][capacity + 1];

        // Construim tabelul DP
        for (int i = 1; i <= n; i++) {
            int w = weights[i - 1];
            int v = values[i - 1];

            for (int c = 0; c <= capacity; c++) {
                if (w <= c) {
                    dp[i][c] = Math.max(dp[i - 1][c], dp[i - 1][c - w] + v);
                } else {
                    dp[i][c] = dp[i - 1][c];
                }
            }
        }

        int maxVal = dp[n][capacity];

        // Reconstruim soluția (care obiecte au fost selectate)
        List<Integer> selectedItems = new ArrayList<>();
        int c = capacity;

        for (int i = n; i > 0; i--) {
            if (dp[i][c] != dp[i - 1][c]) {
                selectedItems.add(i - 1); // Indexul obiectului (0-indexed)
                c -= weights[i - 1];
            }
        }

        Collections.reverse(selectedItems);
        return new KnapsackResult(maxVal, selectedItems);
    }

    // ==================== GREEDY ====================

    /**
     * Rezolvă problema rucsacului folosind algoritmul Greedy.
     * Soluție aproximativă - selectează obiectele cu cel mai bun raport
     * value/weight.
     * 
     * Complexitate timp: O(n log n) pentru sortare
     * Complexitate spațiu: O(n)
     * 
     * @return KnapsackResult cu valoarea obținută și obiectele selectate
     */
    public KnapsackResult solveGreedy() {
        // Creăm și sortăm obiectele după raportul value/weight
        Item[] items = new Item[n];
        for (int i = 0; i < n; i++) {
            items[i] = new Item(weights[i], values[i], i);
        }
        Arrays.sort(items); // Sortare descrescătoare după ratio

        int totalValue = 0;
        int totalWeight = 0;
        List<Integer> selectedItems = new ArrayList<>();

        for (Item item : items) {
            if (totalWeight + item.weight() <= capacity) {
                selectedItems.add(item.index());
                totalWeight += item.weight();
                totalValue += item.value();
            }
        }

        Collections.sort(selectedItems);
        return new KnapsackResult(totalValue, selectedItems);
    }
}
