import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

public class Tester {
    private static final boolean USE_STD_IO = false;
    private static String test = "End-Easy";
    private static String inputFileName = "ConnectFourJava/ConnectFourJava/src/tests/" + test + ".txt";
    private static String outputFileName = "ConnectFourJava/ConnectFourJava/src/tests/" + test + "-output.txt";

    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();
        Solver solver = new Solver();
        PrintStream out = System.out;

        // configure to read from file rather than standard input/output
        if (!USE_STD_IO) {
            try {
                System.setIn(new FileInputStream(inputFileName));
                // System.setOut(new PrintStream(outputFileName));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        try {
            Scanner scanner = new Scanner(System.in); // input scanner
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine(); // input next line
                String[] arr = line.split(" ");
                Board board = new Board();
                int[] input = new int[arr[0].length()];
                for (int i = 0; i < arr[0].length(); i++) {
                    input[i] = arr[0].charAt(i) - '0';
                    System.out.print(input[i]);
                }
                board.tests(input);
                int score = solver.solve(board);
                System.out.println(" " + score);
            }
            scanner.close();
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace(System.err);
        }

        long endTime = System.currentTimeMillis();
        System.setOut(out);
        System.out.println("That took " + (endTime - startTime) + " millieconds");
    }
}
