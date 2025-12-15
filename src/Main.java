import java.io.IOException;

// Main - punct de intrare
// Comenzi: generate, run, benchmark
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
                    System.err.println("Comanda necunoscuta: " + command);
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

    private static void runGenerate() throws IOException {
        System.out.println("Generez teste...\n");
        TestGenerator generator = new TestGenerator(TEST_DIRECTORY);
        generator.generateAllTests();
    }

    private static void runBenchmark() throws IOException {
        KnapsackBenchmark benchmark = new KnapsackBenchmark(TEST_DIRECTORY);
        benchmark.runAllTests();
    }

    private static void printHelp() {
        System.out.println("""
                Proiect Knapsack - AA

                Utilizare: java Main <comanda>

                Comenzi:
                  generate  - Genereaza 20 de teste
                  run       - Ruleaza benchmark
                  help      - Afiseaza ajutor

                Exemplu:
                  java Main generate
                  java Main run
                """);
    }
}
