import java.util.List;
import java.util.Collections;

// Rezultatul rezolvarii - valoarea maxima + lista de obiecte alese
public record KnapsackResult(int maxValue, List<Integer> selectedIndices) {

    public KnapsackResult {
        selectedIndices = Collections.unmodifiableList(selectedIndices);
    }

    public int itemCount() {
        return selectedIndices.size();
    }
}
