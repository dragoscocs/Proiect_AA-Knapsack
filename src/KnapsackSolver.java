import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

// Clasa principala cu algoritmii pentru problema rucsacului
public class KnapsackSolver {

    private final int[] weights;
    private final int[] values;
    private final int capacity;
    private final int n;

    // Pt backtracking
    private int maxValue;
    private List<Integer> bestCombination;
    private Item[] sortedItems;

    public KnapsackSolver(int[] weights, int[] values, int capacity) {
        this.weights = weights;
        this.values = values;
        this.capacity = capacity;
        this.n = weights.length;
    }

    // ========== BACKTRACKING ==========
    // Solutie exacta cu pruning, O(2^n) worst case

    public KnapsackResult solveBacktracking() {
        maxValue = 0;
        bestCombination = new ArrayList<>();

        // Sortam dupa ratio pt pruning mai bun
        sortedItems = new Item[n];
        for (int i = 0; i < n; i++) {
            sortedItems[i] = new Item(weights[i], values[i], i);
        }
        Arrays.sort(sortedItems);

        backtrack(0, 0, 0, new ArrayList<>());

        // Convertim inapoi la indicii originali
        List<Integer> result = new ArrayList<>();
        for (int idx : bestCombination) {
            result.add(sortedItems[idx].index());
        }
        Collections.sort(result);

        return new KnapsackResult(maxValue, result);
    }

    // Upper bound pt pruning - relaxare fractionala
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
                // Fractional pt upper bound
                maxPossibleValue += (double) v * remainingCapacity / w;
                break;
            }
        }
        return maxPossibleValue;
    }

    private void backtrack(int index, int currentWeight, int currentValue,
            List<Integer> currentCombination) {

        if (currentWeight > capacity)
            return;

        if (currentValue > maxValue) {
            maxValue = currentValue;
            bestCombination = new ArrayList<>(currentCombination);
        }

        // Pruning
        if (index >= n || estimateMaxValue(index, currentWeight, currentValue) <= maxValue) {
            return;
        }

        // Include obiectul
        currentCombination.add(index);
        backtrack(index + 1,
                currentWeight + sortedItems[index].weight(),
                currentValue + sortedItems[index].value(),
                currentCombination);
        currentCombination.remove(currentCombination.size() - 1);

        // Nu include
        backtrack(index + 1, currentWeight, currentValue, currentCombination);
    }

    // ========== PROGRAMARE DINAMICA ==========
    // O(n * W) timp si spatiu

    public KnapsackResult solveDynamicProgramming() {
        // dp[i][w] = val max cu primele i obiecte si capacitate w
        int[][] dp = new int[n + 1][capacity + 1];

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

        // Reconstituire solutie
        List<Integer> selectedItems = new ArrayList<>();
        int c = capacity;

        for (int i = n; i > 0; i--) {
            if (dp[i][c] != dp[i - 1][c]) {
                selectedItems.add(i - 1);
                c -= weights[i - 1];
            }
        }

        Collections.reverse(selectedItems);
        return new KnapsackResult(maxVal, selectedItems);
    }

    // ========== GREEDY ==========
    // O(n log n), solutie aproximativa

    public KnapsackResult solveGreedy() {
        Item[] items = new Item[n];
        for (int i = 0; i < n; i++) {
            items[i] = new Item(weights[i], values[i], i);
        }
        Arrays.sort(items); // Dupa ratio desc

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
