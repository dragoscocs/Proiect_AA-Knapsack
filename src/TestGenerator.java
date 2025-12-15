import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

/**
 * Generator de teste pentru problema rucsacului.
 * Generează fișiere de test cu diverse configurații pentru evaluarea
 * algoritmilor.
 */
public class TestGenerator {

    private final Random random;
    private final String outputDirectory;

    /**
     * Constructor.
     * 
     * @param outputDirectory Directorul unde vor fi salvate testele
     */
    public TestGenerator(String outputDirectory) {
        this.outputDirectory = outputDirectory;
        this.random = new Random();
    }

    /**
     * Generează un singur test cu parametrii specificați.
     * 
     * @param n         Numărul de obiecte
     * @param maxWeight Greutatea maximă per obiect
     * @param maxValue  Valoarea maximă per obiect
     * @param capacity  Capacitatea rucsacului
     * @return Stringul cu datele de test
     */
    public String generateTest(int n, int maxWeight, int maxValue, int capacity) {
        StringBuilder sb = new StringBuilder();
        sb.append(n).append(" ").append(capacity).append("\n");

        for (int i = 0; i < n; i++) {
            int weight = random.nextInt(maxWeight) + 1; // 1 to maxWeight
            int value = random.nextInt(maxValue) + 1; // 1 to maxValue
            sb.append(weight).append(" ").append(value).append("\n");
        }

        return sb.toString();
    }

    /**
     * Generează setul complet de 20 de teste (identic cu versiunea Python).
     * Include cazuri normale, cazuri extreme și cazuri aleatorii.
     */
    public void generateAllTests() throws IOException {
        // Creăm directorul dacă nu există
        Path outputPath = Path.of(outputDirectory);
        Files.createDirectories(outputPath);

        // Cazuri normale: (n, maxWeight, maxValue, capacity)
        int[][] normalCases = {
                { 10, 20, 100, 50 }, // număr mic de obiecte, capacitate medie
                { 50, 100, 1000, 500 }, // număr moderat de obiecte, capacitate mare
                { 100, 50, 500, 1000 } // număr mare de obiecte, capacitate foarte mare
        };

        // Cazuri extreme
        int[][] edgeCases = {
                { 1, 1, 1, 1 }, // un singur obiect cu greutatea și valoarea minimă
                { 1, 1000000, 1000000, 1000000 }, // un singur obiect mare
                { 10000, 1, 1, 1000000 }, // multe obiecte foarte ușoare
                { 10000, 1000000, 1000000, 1 }, // capacitate foarte mică
                { 10, 1000000, 1000000, 1000000 } // număr maxim de obiecte și valori
        };

        int testNumber = 1;

        // Generăm cazurile normale
        for (int[] testCase : normalCases) {
            String testData = generateTest(testCase[0], testCase[1], testCase[2], testCase[3]);
            writeTestFile(testNumber++, testData);
        }

        // Generăm cazurile extreme
        for (int[] testCase : edgeCases) {
            String testData = generateTest(testCase[0], testCase[1], testCase[2], testCase[3]);
            writeTestFile(testNumber++, testData);
        }

        // Generăm cazuri aleatorii până la 20 de teste
        while (testNumber <= 20) {
            int n = random.nextInt(1000) + 1; // 1 - 1000 obiecte
            int maxWeight = random.nextInt(1000000) + 1;
            int maxValue = random.nextInt(1000000) + 1;
            int capacity = random.nextInt(1000000) + 1;

            String testData = generateTest(n, maxWeight, maxValue, capacity);
            writeTestFile(testNumber++, testData);
        }

        System.out.println("20 de teste au fost generate în directorul '" + outputDirectory + "'.");
    }

    /**
     * Scrie un fișier de test.
     */
    private void writeTestFile(int testNumber, String data) throws IOException {
        Path filePath = Path.of(outputDirectory, "test_" + testNumber + ".txt");
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(filePath))) {
            writer.print(data);
        }
    }

    /**
     * Main pentru rulare standalone.
     */
    public static void main(String[] args) throws IOException {
        String outputDir = args.length > 0 ? args[0] : "knapsack_tests";
        TestGenerator generator = new TestGenerator(outputDir);
        generator.generateAllTests();
    }
}
