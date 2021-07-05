import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Tester {
    private static final boolean USE_STD_IO = false;
    private static String test = "Begin-Medium";
    private static String inputFileName = "ConnectFourJava/ConnectFourJava/src/tests/" + test + ".txt";
    private static String outputFileName = "ConnectFourJava/ConnectFourJava/src/tests/" + test + "-output.txt";

    public static void main(String[] args) throws Exception {
        Solver solver = new Solver();
        long startingTime = System.currentTimeMillis();
        PrintStream out = System.out;

        // configure to read from file rather than standard input/output
        if (!USE_STD_IO) {
            try {
                System.setIn(new FileInputStream(inputFileName));
                System.setOut(new PrintStream(outputFileName));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        try {
            Scanner scanner = new Scanner(System.in); // input scanner
            int testNumber = 1;
            ArrayList<Integer> failedTests = new ArrayList<Integer>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine(); // input next line
                String[] arr = line.split(" ");
                Board board = new Board();
                int[] input = new int[arr[0].length()];
                System.out.println("Test Number: " + testNumber);
                for (int i = 0; i < arr[0].length(); i++) {
                    input[i] = arr[0].charAt(i) - '0';
                    System.out.print(input[i]);
                }
                System.out.println(" " + arr[1]);
                board.tests(input);
                long startTime = System.currentTimeMillis();
                int score = solver.solve(board);
                long endTime = System.currentTimeMillis();
                System.out.println("Solver Output: " + score);
                System.out.println("Nodes Explored: " + solver.getNodeCount());
                System.out.println("Time expended:  " + (endTime - startTime) + " millieconds");
                System.out.println();
                if (Integer.valueOf(arr[1]) != score) {
                    failedTests.add(testNumber);
                }
                testNumber++;
            }
            scanner.close();
            System.out.println();
            if (failedTests.size() == 0) {
                System.out.println("All test passed!");
            } else {
                System.out.println("Number of Failed Tests: " + failedTests.size());
                System.out.println("Failed Tests: " + failedTests.toString());
            }
            long endingTime = System.currentTimeMillis();
            System.out.println("Total Time expended:  " + (endingTime - startingTime) + " millieconds");
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace(System.err);
        }
        System.setOut(out);
        System.out.println("Done.");
    }
}
