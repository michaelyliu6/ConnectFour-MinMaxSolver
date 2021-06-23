

public class Solver {
    private int[] colOrder = new int[]{ 3, 2, 4, 1, 5, 0, 6};
    private int nodeCount = 0;
    private TranspositionTable table;

    public Solver() {
        this.nodeCount = 0;
        table = new TranspositionTable(8388593);
    }

    public int getNodeCount() {
        return this.nodeCount;
    }

    public int solve(Board board) {
        int min = -1 * (42 - board.getNumMoves()) / 2;
        int max = (48 - board.getNumMoves()) / 2;

        while (min < max) {
            int med = min + (max - min) / 2;
            if (med <= 0 && min / 2 < med) {
                med = min / 2;
            } else if (med >= 0 && max / 2 > med) {
                med = max / 2;
            }
            int score = minMax(board, med, med +  1);
            if (score <= med) {
                max = score;
            } else {
                min = score;
            }
        }
        return min;
        // return minMax(board, -21, 21);
    }

    public int minMax(Board board, int alpha, int beta) {
        nodeCount++;

        if (board.getNumMoves() == 42) {
            return 0;
        }

        for (int i = 0; i < 7; i++) {
            if (board.canPlay(i) && board.isWinningMove(i)) {
                return (43 - board.getNumMoves())/2;
            }
        }
        int best = (41 - board.getNumMoves()) / 2;
        int val = table.get(board.key());
        if (val != 0) {
            best = val - 19;
        }

        if (beta > best) {
            beta = best;
            if (alpha >= beta) {
                return beta;
            }
        }

        for (int i = 0; i < 7; i++) {
            if (board.canPlay(colOrder[i])) {
                Board newboard = board.cloneBoard();
                newboard.play(colOrder[i]);
                int score = -1 * (minMax(newboard, -1 * beta, -1 * alpha));
                if (score >= beta) {
                    return score;
                }
                if (score > alpha) {
                    alpha = score;
                }
            }
        }
        table.put(board.key(), alpha + 19);
        return alpha;

    }
}
