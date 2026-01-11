import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

// Genereaza teste pentru problema rucsacului
public class TestGenerator {

    private final Random random;
    private final String outputDirectory;

    public TestGenerator(String outputDirectory) {
        this.outputDirectory = outputDirectory;
        this.random = new Random();
    }

    // Un test = n obiecte random
    public String generateTest(int n, int maxWeight, int maxValue, int capacity) {
        StringBuilder sb = new StringBuilder();
        sb.append(n).append(" ").append(capacity).append("\n");

        for (int i = 0; i < n; i++) {
            int weight = random.nextInt(maxWeight) + 1;
            int value = random.nextInt(maxValue) + 1;
            sb.append(weight).append(" ").append(value).append("\n");
        }

        return sb.toString();
    }

    // Genereaza 20 de teste - normale, extreme si random
    public void generateAllTests() throws IOException {
        Path outputPath = Path.of(outputDirectory);
        Files.createDirectories(outputPath);

        // Cazuri normale: (n, maxWeight, maxValue, capacity)
        int[][] normalCases = {
                { 10, 20, 100, 50 },
                { 50, 100, 1000, 500 },
                { 100, 50, 500, 1000 }
        };

        // Cazuri extreme
        int[][] edgeCases = {
                { 1, 1, 1, 1 },
                { 1, 1000000, 1000000, 1000000 },
                { 10000, 1, 1, 1000000 },
                { 10000, 1000000, 1000000, 1 },
                { 10, 1000000, 1000000, 1000000 }
        };

        int testNumber = 1;

        for (int[] testCase : normalCases) {
            String testData = generateTest(testCase[0], testCase[1], testCase[2], testCase[3]);
            writeTestFile(testNumber++, testData);
        }

        for (int[] testCase : edgeCases) {
            String testData = generateTest(testCase[0], testCase[1], testCase[2], testCase[3]);
            writeTestFile(testNumber++, testData);
        }

        // Random pana la 50, dar introducem si teste cu valori corelate
        while (testNumber <= 50) {
            int n = random.nextInt(200) + 10; // 10-210 obiecte
            int capacity = random.nextInt(10000) + 100;

            String testData;
            // Pentru testele 21-35, facem teste "corelate" (greutate ~ valoare)
            // Asta e greu pentru Greedy
            if (testNumber <= 35) {
                StringBuilder sb = new StringBuilder();
                sb.append(n).append(" ").append(capacity).append("\n");
                for (int i = 0; i < n; i++) {
                    int weight = random.nextInt(1000) + 1;
                    int value = weight + random.nextInt(20) - 10; // Valoare foarte apropiata de greutate
                    if (value < 1)
                        value = 1;
                    sb.append(weight).append(" ").append(value).append("\n");
                }
                testData = sb.toString();
            } else {
                // Complet random pentru restul
                int maxWeight = random.nextInt(100000) + 1;
                int maxValue = random.nextInt(100000) + 1;
                testData = generateTest(n, maxWeight, maxValue, capacity);
            }

            writeTestFile(testNumber++, testData);
        }

        System.out.println("50 de teste generate in '" + outputDirectory + "'.");
    }

    private void writeTestFile(int testNumber, String data) throws IOException {
        Path filePath = Path.of(outputDirectory, "test_" + testNumber + ".txt");
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(filePath))) {
            writer.print(data);
        }
    }

    public static void main(String[] args) throws IOException {
        String outputDir = args.length > 0 ? args[0] : "knapsack_tests";
        TestGenerator generator = new TestGenerator(outputDir);
        generator.generateAllTests();
    }
}
