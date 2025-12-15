import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// Ruleaza benchmark pe toate testele si exporta CSV pt grafice
public class KnapsackBenchmark {

    private final String testDirectory;
    private static final String CSV_FILE = "rezultate.csv";

    // Limite pt a nu dura prea mult
    private static final int MAX_ITEMS_FOR_BACKTRACKING = 8000;
    private static final int MAX_CAPACITY_FOR_DP = 100_000_000;

    private final List<BenchmarkResult> results = new ArrayList<>();

    public KnapsackBenchmark(String testDirectory) {
        this.testDirectory = testDirectory;
    }

    public record BenchmarkResult(
            String testName,
            int numItems,
            int capacity,
            String algorithm,
            double timeSeconds,
            int value,
            boolean skipped) {
    }

    public record TestData(int capacity, int[] weights, int[] values) {
    }

    public TestData readTestFile(String filePath) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(Path.of(filePath))) {
            String[] firstLine = reader.readLine().split(" ");
            int n = Integer.parseInt(firstLine[0]);
            int capacity = Integer.parseInt(firstLine[1]);

            int[] weights = new int[n];
            int[] values = new int[n];

            for (int i = 0; i < n; i++) {
                String[] parts = reader.readLine().split(" ");
                weights[i] = Integer.parseInt(parts[0]);
                values[i] = Integer.parseInt(parts[1]);
            }

            return new TestData(capacity, weights, values);
        }
    }

    public void runTest(String testFile) throws IOException {
        String testName = Path.of(testFile).getFileName().toString();

        System.out.println("\n" + "=".repeat(60));
        System.out.println("Test: " + testName);
        System.out.println("=".repeat(60));

        TestData data = readTestFile(testFile);
        int n = data.weights().length;
        int capacity = data.capacity();

        System.out.printf("Numar obiecte: %d, Capacitate: %d%n", n, capacity);

        KnapsackSolver solver = new KnapsackSolver(data.weights(), data.values(), capacity);

        // BACKTRACKING
        System.out.println("\n--- Backtracking ---");
        if (n < MAX_ITEMS_FOR_BACKTRACKING) {
            long startTime = System.nanoTime();
            KnapsackResult btResult = solver.solveBacktracking();
            long endTime = System.nanoTime();
            double timeSeconds = (endTime - startTime) / 1_000_000_000.0;

            System.out.printf("Valoare maxima: %d%n", btResult.maxValue());
            System.out.printf("Obiecte selectate: %s%n", btResult.selectedIndices());
            System.out.printf("Timp: %.6f s (%.3f ms)%n", timeSeconds, timeSeconds * 1000);

            results.add(new BenchmarkResult(testName, n, capacity, "Backtracking", timeSeconds, btResult.maxValue(),
                    false));
        } else {
            System.out.printf("SKIP: Prea multe obiecte (%d > %d)%n", n, MAX_ITEMS_FOR_BACKTRACKING);
            results.add(new BenchmarkResult(testName, n, capacity, "Backtracking", -1, -1, true));
        }

        // DP
        System.out.println("\n--- Programare Dinamica ---");
        long dpMemoryNeeded = (long) (n + 1) * (capacity + 1) * 4;
        if (capacity <= MAX_CAPACITY_FOR_DP && dpMemoryNeeded < 2_000_000_000L) {
            long startTime = System.nanoTime();
            KnapsackResult dpResult = solver.solveDynamicProgramming();
            long endTime = System.nanoTime();
            double timeSeconds = (endTime - startTime) / 1_000_000_000.0;

            System.out.printf("Valoare maxima: %d%n", dpResult.maxValue());
            System.out.printf("Obiecte selectate: %s%n", dpResult.selectedIndices());
            System.out.printf("Timp: %.6f s (%.3f ms)%n", timeSeconds, timeSeconds * 1000);

            results.add(new BenchmarkResult(testName, n, capacity, "DP", timeSeconds, dpResult.maxValue(), false));
        } else {
            System.out.printf("SKIP: Capacitate prea mare (%d)%n", capacity);
            results.add(new BenchmarkResult(testName, n, capacity, "DP", -1, -1, true));
        }

        // GREEDY
        System.out.println("\n--- Greedy ---");
        long startTime = System.nanoTime();
        KnapsackResult grResult = solver.solveGreedy();
        long endTime = System.nanoTime();
        double timeSeconds = (endTime - startTime) / 1_000_000_000.0;

        System.out.printf("Valoare aprox: %d%n", grResult.maxValue());
        System.out.printf("Obiecte selectate: %s%n", grResult.selectedIndices());
        System.out.printf("Timp: %.6f s (%.3f ms)%n", timeSeconds, timeSeconds * 1000);

        results.add(new BenchmarkResult(testName, n, capacity, "Greedy", timeSeconds, grResult.maxValue(), false));
    }

    public void exportToCsv() throws IOException {
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(Path.of(CSV_FILE)))) {
            writer.println("Test,NumarObiecte,Capacitate,Algoritm,Timp_Secunde,Timp_Ms,Valoare,Skipped");

            for (BenchmarkResult r : results) {
                writer.printf("%s,%d,%d,%s,%.6f,%.3f,%d,%s%n",
                        r.testName(),
                        r.numItems(),
                        r.capacity(),
                        r.algorithm(),
                        r.skipped() ? 0 : r.timeSeconds(),
                        r.skipped() ? 0 : r.timeSeconds() * 1000,
                        r.skipped() ? 0 : r.value(),
                        r.skipped() ? "DA" : "NU");
            }
        }
        System.out.println("\nRezultate exportate in: " + CSV_FILE);
    }

    public void runAllTests() throws IOException {
        Path testDir = Path.of(testDirectory);

        if (!Files.exists(testDir)) {
            System.err.println("Nu exista directorul: " + testDirectory);
            System.err.println("Ruleaza: java Main generate");
            return;
        }

        List<Path> testFiles = new ArrayList<>();
        try (var stream = Files.list(testDir)) {
            stream.filter(p -> p.toString().endsWith(".txt"))
                    .forEach(testFiles::add);
        }

        testFiles.sort(Comparator.comparingInt(p -> {
            String name = p.getFileName().toString();
            return Integer.parseInt(name.replace("test_", "").replace(".txt", ""));
        }));

        System.out.println("========================================");
        System.out.println("  BENCHMARK KNAPSACK - Proiect AA");
        System.out.println("========================================");
        System.out.printf("Gasite %d teste%n", testFiles.size());

        results.clear();

        for (Path testFile : testFiles) {
            runTest(testFile.toString());
        }

        System.out.println("\n" + "=".repeat(60));
        System.out.println("BENCHMARK FINALIZAT");
        System.out.println("=".repeat(60));

        exportToCsv();
        printSummaryTable();
    }

    private void printSummaryTable() {
        System.out.println("\n========================================");
        System.out.println("TABEL REZUMAT");
        System.out.println("========================================");
        System.out.printf("%-12s | %-6s | %-10s | %-12s | %-10s%n",
                "Test", "N", "Capacitate", "Algoritm", "Timp (ms)");
        System.out.println("-".repeat(60));

        for (BenchmarkResult r : results) {
            if (!r.skipped()) {
                System.out.printf("%-12s | %-6d | %-10d | %-12s | %-10.3f%n",
                        r.testName().replace("test_", "").replace(".txt", ""),
                        r.numItems(),
                        r.capacity(),
                        r.algorithm(),
                        r.timeSeconds() * 1000);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String testDir = args.length > 0 ? args[0] : "knapsack_tests";
        KnapsackBenchmark benchmark = new KnapsackBenchmark(testDir);
        benchmark.runAllTests();
    }
}
