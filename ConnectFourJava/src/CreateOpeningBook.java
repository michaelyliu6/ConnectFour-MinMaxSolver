import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;

public class CreateOpeningBook {
    private static String outputFileName = "ConnectFourJava/ConnectFourJava/src/OpeningBook.txt";
    private Solver solver = new Solver();
    public static void main(String args[]) {
        PrintStream out = System.out;
        
        try {
            System.setOut(new PrintStream(outputFileName));
            CreateOpeningBook book = new CreateOpeningBook();
            book.print(3);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        System.setOut(out);
        System.out.println("Done.");
    }

    public void print(int goalDepth) {
        ArrayList<Integer> moves = new ArrayList<Integer>();
        int depth = 0;
        Board board = new Board();
        makeTree(moves, board, depth, goalDepth);
    }

    public void makeTree(ArrayList<Integer> moves, Board board, int depth, int goalDepth) {
        if (depth <= goalDepth) {
            for (int i = 0; i < 7; i++) {
                ArrayList<Integer> updatedMoves = (ArrayList<Integer>) moves.clone();
                updatedMoves.add(i);
                Board newBoard = board.cloneBoard();
                newBoard.play(i);
                int score = solver.solve(newBoard);
                System.out.println(depth + " " + newBoard.key() + " " + score);
                makeTree(updatedMoves, newBoard, depth + 1, goalDepth);
            }
        }
        
        
    }

    public static String toString(ArrayList<Integer> lst) {
        String str = "";
        for (Integer i : lst) {
            str += i;
        }
        return str;
    }
}
