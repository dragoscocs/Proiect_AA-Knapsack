import java.io.IOException;

/**
 * Punct de intrare principal pentru proiectul Knapsack.
 * 
 * Utilizare:
 * java Main generate - Generează testele
 * java Main run - Rulează algoritmii pe teste
 * java Main benchmark - Rulează benchmark complet
 * java Main - Echivalent cu 'benchmark'
 */
public class Main {

    private static final String TEST_DIRECTORY = "knapsack_tests";

    public static void main(String[] args) {
        String command = args.length > 0 ? args[0].toLowerCase() : "benchmark";

        try {
            switch (command) {
                case "generate" -> runGenerate();
                case "run", "benchmark" -> runBenchmark();
                case "help", "-h", "--help" -> printHelp();
                default -> {
                    System.err.println("Comandă necunoscută: " + command);
                    printHelp();
                    System.exit(1);
                }
            }
        } catch (IOException e) {
            System.err.println("Eroare: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Generează setul de teste.
     */
    private static void runGenerate() throws IOException {
        System.out.println("Generare teste în directorul '" + TEST_DIRECTORY + "'...\n");
        TestGenerator generator = new TestGenerator(TEST_DIRECTORY);
        generator.generateAllTests();
    }

    /**
     * Rulează benchmark-ul pe toate testele.
     */
    private static void runBenchmark() throws IOException {
        KnapsackBenchmark benchmark = new KnapsackBenchmark(TEST_DIRECTORY);
        benchmark.runAllTests();
    }

    /**
     * Afișează informații de ajutor.
     */
    private static void printHelp() {
        System.out.println("""
                ╔════════════════════════════════════════════════════════════╗
                ║         Proiect Knapsack - Analiza Algoritmilor            ║
                ╚════════════════════════════════════════════════════════════╝

                Utilizare: java Main <comandă>

                Comenzi disponibile:
                  generate    Generează 20 de teste în directorul 'knapsack_tests'
                  run         Rulează algoritmii pe testele generate
                  benchmark   Același cu 'run' (implicit)
                  help        Afișează acest mesaj

                Algoritmi implementați:
                  1. Backtracking cu pruning (soluție exactă)
                  2. Programare Dinamică (soluție optimă)
                  3. Greedy (soluție aproximativă)

                Exemplu:
                  java Main generate   # Generează testele
                  java Main run        # Rulează benchmark-ul
                """);
    }
}
