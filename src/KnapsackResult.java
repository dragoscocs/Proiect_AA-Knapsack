import java.util.List;
import java.util.Collections;

/**
 * Reprezintă rezultatul rezolvării problemei rucsacului.
 * Conține valoarea maximă obținută și lista de indecși ai obiectelor selectate.
 * 
 * @param maxValue        Valoarea maximă obținută
 * @param selectedIndices Lista de indecși (0-indexed) ai obiectelor selectate
 */
public record KnapsackResult(int maxValue, List<Integer> selectedIndices) {

    /**
     * Constructor care asigură imutabilitatea listei.
     */
    public KnapsackResult {
        selectedIndices = Collections.unmodifiableList(selectedIndices);
    }

    /**
     * Returnează numărul de obiecte selectate.
     */
    public int itemCount() {
        return selectedIndices.size();
    }

    @Override
    public String toString() {
        return String.format("KnapsackResult[maxValue=%d, items=%s]", maxValue, selectedIndices);
    }
}
