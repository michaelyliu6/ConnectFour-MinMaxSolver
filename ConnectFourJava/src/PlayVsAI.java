import java.util.Scanner;

public class PlayVsAI {
    public static void main(String args[]) {
        Board board = new Board();
        Solver solver = new Solver();
        System.out.println(getBestMove(solver, board));
    }

    public static int getBestMove(Solver solver, Board board) {
        int bestScore = Integer.MIN_VALUE;
        int bestMove = 0;
        for (int i = 0; i < 7; i++) {
            
            Board newBoard = board.cloneBoard();
            newBoard.play(i);
            int score = solver.solve(newBoard);
            if (score > bestScore) {
                bestScore = score;
                bestMove = i;
            }
            System.out.println("Move: " + i);
            System.out.println("Score: " + score);
        }
        return bestMove;
    }
}